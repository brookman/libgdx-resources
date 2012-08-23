package eu32k.rocketScience;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import eu32k.rocketScience.entity.Entity;
import eu32k.rocketScience.entity.Ground;
import eu32k.rocketScience.entity.Movable;
import eu32k.rocketScience.entity.Rocket;

public class RocketScience implements ApplicationListener {

   private float aspectRatio;
   private float targetZoom = 20.0f;
   private float zoom = targetZoom;

   private Rocket rocket;
   private List<Entity> entities;

   private World world;
   private Camera camera;
   private Camera camera2;

   private Menu menu;
   private RunText text1;
   private RunText text2;
   private ShaderProgram shader;

   private SpriteBatch batch;
   private SpriteBatch hudBatch;
   private ParticleEffect explosion;
   private ParticleEffect fire;

   boolean canReset = true;

   private Stage stage;

   @Override
   public void create() {

      String vertexShader = "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + ";\n" //
            + "uniform mat4 u_projectionViewMatrix;\n" //
            + "varying vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "\n" //
            + "void main()\n" //
            + "{\n" //
            + "   v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" //
            + "   v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + ";\n" //
            + "   gl_Position =  u_projectionViewMatrix * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" //
            + "}\n";
      String fragmentShader = "#ifdef GL_ES\n" //
            + "#define LOWP lowp\n" //
            + "precision mediump float;\n" //
            + "#else\n" //
            + "#define LOWP \n" //
            + "#endif\n" //
            + "varying LOWP vec4 v_color;\n" //
            + "varying vec2 v_texCoords;\n" //
            + "uniform sampler2D u_texture;\n" //
            + "void main()\n"//
            + "{\n" //
            + "  gl_FragColor = texture2D(u_texture, v_texCoords);\n" //
            + "}";
      shader = new ShaderProgram(vertexShader, fragmentShader);

      entities = new ArrayList<Entity>();

      makeWorld();

      menu = Menu.getInstance();
      text1 = new RunText("Welcome to Rocket Science!                 ", 2.0f);
      text2 = new RunText("Controls: Press arrow keys to control and R to reset.", 0.8f);

      batch = new SpriteBatch();
      hudBatch = new SpriteBatch();
      stage = new Stage();
      explosion = new ParticleEffect();
      explosion.load(Gdx.files.internal("data/effects/explosion2"), Gdx.files.internal("data/effects"));

      fire = new ParticleEffect();
      fire.load(Gdx.files.internal("data/effects/fire"), Gdx.files.internal("data/effects"));
      fire.start();
   }

   private void makeWorld() {
      if (world != null) {
         world.dispose();
      }
      world = new World(new Vector2(0.0f, -10.0f), true);
      entities = new ArrayList<Entity>();
      GeometryFactory gf = new GeometryFactory(world);

      rocket = new Rocket(gf, shader);
      entities.add(rocket);

      entities.add(new Movable(gf, "base1.png", shader));
      entities.add(new Movable(gf, "base2.png", shader));

      entities.add(new Movable(gf, "box.png", shader, 20.0f, -1.0f));
      entities.add(new Movable(gf, "box.png", shader, 23.0f, -1.0f));
      entities.add(new Movable(gf, "box.png", shader, 26.0f, -1.0f));
      entities.add(new Movable(gf, "box.png", shader, 29.0f, -1.0f));
      entities.add(new Movable(gf, "box.png", shader, 32.0f, -1.0f));
      entities.add(new Movable(gf, "box.png", shader, 21.5f, 1.0f));
      entities.add(new Movable(gf, "box.png", shader, 24.5f, 1.0f));
      entities.add(new Movable(gf, "box.png", shader, 27.5f, 1.0f));
      entities.add(new Movable(gf, "box.png", shader, 30.5f, 1.0f));
      entities.add(new Movable(gf, "box.png", shader, 23.0f, 3.0f));
      entities.add(new Movable(gf, "box.png", shader, 26.0f, 3.0f));
      entities.add(new Movable(gf, "box.png", shader, 29.0f, 3.0f));
      entities.add(new Movable(gf, "box.png", shader, 24.5f, 5.0f));
      entities.add(new Movable(gf, "box.png", shader, 27.5f, 5.0f));
      entities.add(new Movable(gf, "box.png", shader, 26.0f, 7.0f));

      entities.add(new Entity(gf, "ramp.png", shader));

      for (int i = 1; i < 40; i++) {
         entities.add(new Ground(gf, "ground.png", shader, 8.0f * i, -6.0f));
         entities.add(new Ground(gf, "ground.png", shader, -8.0f * i, -6.0f));
      }
   }

   @Override
   public void resize(int width, int height) {
      stage.setViewport(width, height, true);
      aspectRatio = (float) width / (float) height;
      updateCam();
   }

   private void updateCam() {
      camera = new OrthographicCamera(2.0f * aspectRatio * zoom, 2.0f * zoom);
      camera2 = new OrthographicCamera(2.0f * aspectRatio * zoom * 20.0f, 2.0f * zoom * 20.0f);
   }

   @Override
   public void render() {
      Gdx.gl.glClearColor(1f / 255f * 90f, 1f / 255f * 115f, 0.5f, 1.0f);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      if (!text1.isFinished()) {
         hudBatch.begin();
         text1.draw(hudBatch, 30.0f, 400.0f);
         hudBatch.end();
         text2.reset();
         return;
      }

      world.step(Gdx.graphics.getDeltaTime(), 8, 3);

      if (Gdx.input.isKeyPressed(Input.Keys.R)) {
         if (canReset) {
            makeWorld();
            // text1.reset();
            text2.reset();
            canReset = false;
         }
      } else {
         canReset = true;
      }

      float rocketX = rocket.getBody().getPosition().x;
      float rocketY = rocket.getBody().getPosition().y;

      camera.position.x = rocketX;
      camera.position.y = rocketY;
      camera.update();
      Gdx.graphics.setVSync(true);

      Body b = rocket.getBody();
      Transform t = b.getTransform();

      if (Gdx.input.isKeyPressed(Input.Keys.UP) && b.isActive()) {
         Vector2 base = new Vector2(0.0f, -2.8f);
         t.mul(base);
         float strength = 200.0f;
         float alpha = b.getAngle() + MathUtils.PI / 2.0f;
         Vector2 force = new Vector2(MathUtils.cos(alpha) * strength, MathUtils.sin(alpha) * strength);
         b.applyForce(force, base);

         batch.setProjectionMatrix(camera.combined.cpy().translate(base.x, base.y, 0).scl(0.04f).rotate(0, 0, 1, b.getAngle() * 180.0f / MathUtils.PI - 90.0f));
         batch.begin();
         fire.draw(batch, Gdx.graphics.getDeltaTime());
         batch.end();
      }

      if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && b.isActive()) {
         Vector2 base = new Vector2(-0.8f, -1.1f);
         t.mul(base);
         float strength = 50.0f;
         float alpha = b.getAngle() + MathUtils.PI / 2.0f - 0.6f;
         Vector2 force = new Vector2(MathUtils.cos(alpha) * strength, MathUtils.sin(alpha) * strength);
         b.applyForce(force, base);

         batch.setProjectionMatrix(camera.combined.cpy().translate(base.x, base.y, 0).scl(0.02f).rotate(0, 0, 1, (b.getAngle() - 0.6f) * 180.0f / MathUtils.PI - 90.0f));
         batch.begin();
         fire.draw(batch, Gdx.graphics.getDeltaTime());
         batch.end();
      }
      if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && b.isActive()) {
         Vector2 base = new Vector2(0.8f, -1.1f);
         t.mul(base);
         float strength = 50.0f;
         float alpha = b.getAngle() + MathUtils.PI / 2 + 0.6f;
         Vector2 force = new Vector2(MathUtils.cos(alpha) * strength, MathUtils.sin(alpha) * strength);
         b.applyForce(force, base);

         batch.setProjectionMatrix(camera.combined.cpy().translate(base.x, base.y, 0).scl(0.02f).rotate(0.0f, 0.0f, 1.0f, (b.getAngle() + 0.6f) * 180.0f / MathUtils.PI - 90.0f));
         batch.begin();
         fire.draw(batch, Gdx.graphics.getDeltaTime());
         batch.end();
      }

      if (b.isActive()) {
         Fixture head = rocket.head;
         for (Contact contact : world.getContactList()) {
            if (contact.isTouching() && (contact.getFixtureA() == head || contact.getFixtureB() == head)) {
               b.setActive(false);
               explosion.start();
               for (Entity entity : entities) {
                  Body entityBody = entity.getBody();
                  if (entityBody.isActive()) {
                     Vector2 vec = new Vector2(entityBody.getPosition().x - rocketX, entityBody.getPosition().y - rocketY);

                     float force = 100.0f - vec.len();
                     force = MathUtils.clamp(force, 0.0f, 300.0f);

                     vec.nor().mul(force * 0.1f);
                     entityBody.applyLinearImpulse(vec, entityBody.getPosition());
                     // entityBody.applyForceToCenter(vec);
                  }
               }
            }
         }
      }

      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
      Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
      for (Entity entity : entities) {
         entity.draw(camera.combined);
      }

      if (!explosion.isComplete()) {
         batch.setProjectionMatrix(camera.combined.cpy().translate(rocketX, rocketY, 0.0f).scl(0.04f));
         batch.begin();
         explosion.draw(batch, Gdx.graphics.getDeltaTime());
         batch.end();
      }

      // draw hud
      // hudBatch.setProjectionMatrix(new Matrix4());
      // hudBatch.begin();

      stage.getSpriteBatch().begin();
      menu.draw(stage.getSpriteBatch(), 0.0f, 0.0f, 100.0f, 100.0f);
      stage.getSpriteBatch().end();
      // text2.draw(hudBatch, 30.0f, 60.0f);
      // hudBatch.end();

      camera2.update();
      for (Entity entity : entities) {
         Matrix4 mat = camera2.combined.cpy();
         mat.translate(360.0f, -370.0f, 0.0f);
         entity.draw(mat);
      }
   }

   @Override
   public void pause() {
   }

   @Override
   public void resume() {
   }

   @Override
   public void dispose() {
   }
}
