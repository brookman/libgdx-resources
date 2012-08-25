package eu32k.libgdx.common;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;

import eu32k.libgdx.SimpleGame;
import eu32k.libgdx.example.Cube;
import eu32k.libgdx.geometry.PrimitivesFactory;
import eu32k.libgdx.math.AnimationBundle;
import eu32k.libgdx.math.InterpolatedAnimation;
import eu32k.libgdx.math.Interpolation.Type;
import eu32k.libgdx.rendering.Mixer;
import eu32k.libgdx.rendering.MultiPassRenderer;
import eu32k.libgdx.rendering.Renderer;
import eu32k.libgdx.rendering.TextureRenderer;

public class Intro extends SimpleGame {

   private ShaderProgram defaultShader;
   private ArrayList<Cube> cubes = new ArrayList<Cube>();

   private AnimationBundle animations;

   private TextureRenderer normalRenderer;
   private MultiPassRenderer blurRenderer;
   private Mixer mixer;

   private InterpolatedAnimation glow;
   private InterpolatedAnimation noise;

   public Intro() {
      super(true);
   }

   @Override
   public void init() {
      defaultShader = tag(new ShaderProgram(Gdx.files.internal("shaders/light.vsh").readString(), Gdx.files.internal("shaders/light.fsh").readString()));

      ShaderProgram simpleShader = tag(new ShaderProgram(Gdx.files.internal("shaders/simple.vsh").readString(), Gdx.files.internal("shaders/simple.fsh").readString()));

      normalRenderer = tag(new TextureRenderer(1920, 1080, simpleShader));

      ShaderProgram verticalBlur = tag(new ShaderProgram(Gdx.files.internal("shaders/simple.vsh").readString(), Gdx.files.internal("shaders/blur_v.fsh").readString()));
      ShaderProgram horizontalBlur = tag(new ShaderProgram(Gdx.files.internal("shaders/simple.vsh").readString(), Gdx.files.internal("shaders/blur_h.fsh").readString()));

      List<Renderer> renderStack = new ArrayList<Renderer>();
      renderStack.add(tag(new TextureRenderer(512, 512, verticalBlur)));
      renderStack.add(tag(new TextureRenderer(512, 512, horizontalBlur)));
      renderStack.add(tag(new TextureRenderer(512, 512, simpleShader)));
      blurRenderer = new MultiPassRenderer(renderStack);

      mixer = tag(new Mixer(normalRenderer, blurRenderer, true));

      Mesh cubeMesh = tag(PrimitivesFactory.makeCube());
      Texture cubeTexture = tag(new Texture(Gdx.files.internal("textures/white.png"), true));

      animations = new AnimationBundle(Gdx.files.internal("animations/cubes.ani"), 8);

      for (int i = 0; i < animations.getNumberOfObjects() - 2; i++) {
         Cube cube = new Cube(defaultShader, cubeMesh, GL20.GL_TRIANGLES, cubeTexture);
         cube.setUseLighting(true);
         cubes.add(cube);
      }

      glow = new InterpolatedAnimation(0.8, 1.3, Type.SMOOTH_STEP, 8000);
      glow.start();

      noise = new InterpolatedAnimation(0.3, 3.0, Type.SMOOTH_STEP, 3000);
      noise.startDelayed(5800);

      animations.start();
   }

   private void renderScene() {
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
      Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);

      defaultShader.begin();

      defaultShader.setUniformf("uAmbientColor", 0.2f, 0.2f, 0.2f);
      defaultShader.setUniformf("uPointLightingColor", 1.0f, 1.0f, 1.0f);

      Vector3 tempPos = animations.getPos(cubes.size());
      defaultShader.setUniformf("uPointLightingLocation", tempPos.x, tempPos.y, tempPos.z);
      System.out.println(tempPos);
      defaultShader.setUniformMatrix("uPMatrix", camera.combined);

      for (Cube cube : cubes) {
         cube.render();
      }

      defaultShader.end();
   }

   @Override
   public void draw(float delta) {
      if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
         dispose();
         System.exit(0);
      }

      animations.update();

      for (int i = 0; i < cubes.size(); i++) {
         cubes.get(i).setPos(animations.getPos(i));
         cubes.get(i).setRot(animations.getRot(i));
         cubes.get(i).setScale(animations.getScale(i).cpy().mul(2.0f));
      }

      camera.far = 100.0f;
      camera.position.set(animations.getPos(cubes.size() + 1));

      camera.lookAt(0, 0, 0);
      camera.update();

      // NORMAL
      // -------------------------------------------------------------------------------
      normalRenderer.begin();
      renderScene();
      normalRenderer.endAndRender();
      // NORMAL FINISHED
      // ----------------------------------------------------------------------

      // BLUR
      // ---------------------------------------------------------------------------------
      // blurRenderer.begin();
      // normalRenderer.render();
      // blurRenderer.end();
      // BLUR
      // FINISHED-------------------------------------------------------------------------

      // float glowValue = (float) glow.getValue();
      // mixer.setFactor1(glowValue + 0.05f);
      // mixer.setFactor2(glowValue);
      // mixer.noise = (float) noise.getValue();
      //
      // mixer.render();
   }
}
