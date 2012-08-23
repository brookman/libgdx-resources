package eu32k.libgdx.example;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

import eu32k.libgdx.SimpleGame;
import eu32k.libgdx.geometry.GeometricObject;
import eu32k.libgdx.geometry.PrimitivesFactory;
import eu32k.libgdx.rendering.MultiPassRenderer;
import eu32k.libgdx.rendering.Renderer;
import eu32k.libgdx.rendering.TextureRenderer;

public class BeatCity extends SimpleGame {

   private ShaderProgram defaultShader;
   // private TextureRenderer postRenderer;
   private MultiPassRenderer multiPass;

   private ArrayList<Cube> cubes = new ArrayList<Cube>();
   private GeometricObject light;

   private Music music;

   private Vector3 cameraPosition = new Vector3(0.0f, 10.0f, 10.0f);
   private float lightPosition = 0;
   private int counter = 0;
   private boolean boom = false;
   private long lastMove = 0;
   private long time = 0;
   private float angleY = 0;
   private float angleX = 0;
   private float touchStartX = 0;
   private float touchStartY = 0;

   public BeatCity() {
      super(true);
   }

   @Override
   public void init() {
      defaultShader = tag(new ShaderProgram(Gdx.files.internal("data/light.vsh").readString(), Gdx.files.internal("data/light.fsh").readString()));

      ShaderProgram verticalBlur = tag(new ShaderProgram(Gdx.files.internal("data/simple.vsh").readString(), Gdx.files.internal("data/blur_v.fsh").readString()));
      ShaderProgram horizontalBlur = tag(new ShaderProgram(Gdx.files.internal("data/simple.vsh").readString(), Gdx.files.internal("data/blur_h.fsh").readString()));

      List<Renderer> renderStack = new ArrayList<Renderer>();
      renderStack.add(tag(new TextureRenderer(400, 300, verticalBlur)));
      renderStack.add(tag(new TextureRenderer(400, 300, horizontalBlur)));
      multiPass = new MultiPassRenderer(renderStack);

      Mesh sphereMesh = tag(ObjLoader.loadObj(Gdx.files.internal("data/sphere.obj").read(), true, false));
      Mesh cubeMesh = tag(PrimitivesFactory.makeCube());

      light = new GeometricObject(defaultShader, sphereMesh, GL20.GL_TRIANGLES);
      light.setUseLighting(false);

      Texture cubeTexture = tag(new Texture(Gdx.files.internal("data/cube.png"), true));

      for (int x = 0; x < 8; x++) {
         for (int y = 0; y < 8; y++) {
            Cube cube = new Cube(defaultShader, cubeMesh, GL20.GL_TRIANGLES, cubeTexture);
            cube.getPos().x = x * 2.0f - 7.0f;
            cube.getPos().y = y * 2.0f - 7.0f;
            cubes.add(cube);
         }
      }

      music = tag(Gdx.audio.newMusic(Gdx.files.internal("data/template.ogg")));
      music.setLooping(true);
   }

   @Override
   public void draw(float delta) {
      if (!music.isPlaying()) {
         music.play();
         time = System.currentTimeMillis();
      }

      // update values ------------------------------------------------------------------------------------------------------------------------------------------
      lightPosition += delta * 3.0f;
      Vector3 lightVector = new Vector3((float) (Math.cos(lightPosition) * 5.0f), (float) (Math.cos(lightPosition * 0.3f) * 5.0f), (float) (Math.sin(lightPosition) * 5.0f));

      if (System.currentTimeMillis() - lastMove > 2000) {
         angleY += delta * 30.0f;
      }

      cameraPosition.y = angleX * 0.1f;
      cameraPosition.x = (float) Math.cos(angleY * 0.01f) * 15.0f;
      cameraPosition.z = (float) Math.sin(angleY * 0.01f) * 15.0f;
      camera.position.set(cameraPosition);
      camera.lookAt(0.0f, 0.0f, 0.0f);
      camera.update();

      light.setPos(lightVector);
      light.setScale(new Vector3(0.1f, 0.1f, 0.1f));

      // render -------------------------------------------------------------------------------------------------------------------------------------------------
      multiPass.begin();

      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
      Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);

      defaultShader.begin();

      defaultShader.setUniformf("uAmbientColor", 0.1f, 0.1f, 0.1f);
      defaultShader.setUniformf("uPointLightingColor", 1.0f, 1.0f, 1.0f);
      defaultShader.setUniformf("uPointLightingLocation", lightVector.x, lightVector.y, lightVector.z);
      defaultShader.setUniformMatrix("uPMatrix", camera.combined);

      // float currentTime = music.getPosition() + 0.5f;
      float currentTime = (System.currentTimeMillis() - time) / 1000.0f;

      while (MusicData.data[counter++] <= currentTime) {
         boom = true;
      }
      counter--;

      for (Cube cube : cubes) {
         // float add = (float) (Math.random() * 0.3 * (8.0 - Math.abs(object.getxPos())));
         // add += (float) (Math.random() * 0.3 * (8.0 - Math.abs(object.getzPos())));
         if (boom) {
            if (Math.random() < 0.75) {
               cube.speed = 0.6f * (float) Math.random() + 0.2f;
            }
         }
         cube.speed -= Gdx.graphics.getDeltaTime() * 3.0f;

         float newPos = cube.getPos().y + cube.speed;
         if (newPos < 0) {
            newPos = 0;
            cube.speed = 0;
         }

         cube.getPos().y = newPos;
         cube.render();
      }
      boom = false;

      light.render();
      defaultShader.end();

      multiPass.endAndRender();
   }

   @Override
   public boolean touchDown(int x, int y, int pointer, int button) {
      touchStartX = x;
      touchStartY = y;
      return false;
   }

   @Override
   public boolean touchDragged(int x, int y, int pointer) {
      angleY += x - touchStartX;
      angleX += y - touchStartY;
      angleX = Math.max(-400, Math.min(400, angleX));
      touchStartX = x;
      touchStartY = y;
      lastMove = System.currentTimeMillis();
      return false;
   }
}
