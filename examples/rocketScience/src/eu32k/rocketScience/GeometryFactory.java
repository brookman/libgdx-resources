package eu32k.rocketScience;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import eu32k.rocketScience.external.FixtureAtlas;

public class GeometryFactory {

   private World world;
   private FixtureAtlas fixtureAtlas;

   public GeometryFactory(World world) {
      this.world = world;
      fixtureAtlas = new FixtureAtlas(Gdx.files.internal("data/models/models.bin"));
   }

   public Body loadModel(String name, float x, float y, float size, float mass, BodyType type) {
      BodyDef bodyDef = makeBodyDef(type, false, false);

      Body body = makeBody(bodyDef, new FixtureDef[] {}, 4f, new Vector2(x, y), 0.0f, null);
      body.setLinearDamping(0.2f);
      body.setAngularDamping(0.5f);

      FixtureDef fixture = makeFixture(null, 1.0f, 0.8f, 0.1f, false);
      fixtureAtlas.createFixtures(body, name, size, size, fixture);
      body.setTransform(new Vector2(x, y), 0.0f);

      MassData massData = body.getMassData();
      massData.mass = mass;
      body.setMassData(massData);
      return body;
   }

   public Body makeRocket(float x, float y) {
      FixtureDef pipe = makeFixture(new Vector2[] { new Vector2(-0.5f, 2.0f), new Vector2(-0.5f, -2.0f), new Vector2(0.5f, -2.0f), new Vector2(0.5f, 2.0f), new Vector2(0.3f, 2.5f),
            new Vector2(-0.3f, 2.5f) }, 1.0f, 0.4f, 0.1f, false);

      FixtureDef head = makeFixture(new Vector2[] { new Vector2(-0.3f, 2.5f), new Vector2(0.3f, 2.5f), new Vector2(0.0f, 3.0f) }, 1.0f, 0.4f, 0.1f, true);

      FixtureDef engine = makeFixture(new Vector2[] { new Vector2(-0.3f, -2.4f), new Vector2(0.3f, -2.4f), new Vector2(0.0f, -1.8f) }, 1.0f, 0.4f, 0.1f, false);

      FixtureDef wing1 = makeFixture(new Vector2[] { new Vector2(-0.5f, -2.0f), new Vector2(-0.5f, -1.0f), new Vector2(-1.3f, -1.8f), new Vector2(-1.3f, -2.5f) }, 1.0f, 0.4f, 0.1f, false);
      FixtureDef wing2 = makeFixture(new Vector2[] { new Vector2(1.3f, -2.5f), new Vector2(1.3f, -1.8f), new Vector2(0.5f, -1.0f), new Vector2(0.5f, -2.0f) }, 1.0f, 0.4f, 0.1f, false);

      BodyDef bodyDef = makeBodyDef(BodyType.DynamicBody, false, false);

      Body body = makeBody(bodyDef, new FixtureDef[] { engine, head, wing1, wing2, pipe }, 4f, new Vector2(x, y), 0.0f, head);
      body.setLinearDamping(0.2f);
      body.setAngularDamping(0.5f);

      return body;
   }

   public Body makeGround(float x, float y) {
      FixtureDef ground = makeFixture(new Vector2[] { new Vector2(-100.0f, 1.0f), new Vector2(-100.0f, -1.0f), new Vector2(100.0f, -1.0f), new Vector2(100.0f, 1.0f) }, 1.0f, 0.4f, 0.1f, false);
      // FixtureDef head = makeFixture(vertices, 1.0f, 0.4f, 0.1f);

      BodyDef bodyDef = makeBodyDef(BodyType.StaticBody, false, false);

      Body body = makeBody(bodyDef, new FixtureDef[] { ground }, 0f, new Vector2(x, y), 0.0f, null);
      return body;
   }

   public Body makeRamp1(float x, float y) {
      FixtureDef base = makeFixture(new Vector2[] { new Vector2(-0.5f, -1f), new Vector2(0.5f, -1f), new Vector2(0.5f, 2f), new Vector2(-0.5f, 2f) }, 1.0f, 0.4f, 0.1f, false);

      FixtureDef top = makeFixture(new Vector2[] { new Vector2(-0.5f, 2f), new Vector2(0.5f, 2f), new Vector2(1.4f, 2.8f), new Vector2(1.4f, 3.2f) }, 1.0f, 0.4f, 0.1f, false);

      BodyDef bodyDef = makeBodyDef(BodyType.DynamicBody, false, false);

      Body body = makeBody(bodyDef, new FixtureDef[] { top, base }, 0.5f, new Vector2(x, y), 0.0f, null);

      body.setLinearDamping(0.2f);
      body.setAngularDamping(0.5f);
      return body;
   }

   public Body makeRamp2(float x, float y) {
      FixtureDef base = makeFixture(new Vector2[] { new Vector2(-0.5f, -1f), new Vector2(0.5f, -1f), new Vector2(0.5f, 2f), new Vector2(-0.5f, 2f) }, 1.0f, 0.4f, 0.1f, false);

      FixtureDef top = makeFixture(new Vector2[] { new Vector2(-0.5f, 2f), new Vector2(0.5f, 2f), new Vector2(-1.4f, 3.2f), new Vector2(-1.4f, 2.8f) }, 1.0f, 0.4f, 0.1f, false);

      BodyDef bodyDef = makeBodyDef(BodyType.DynamicBody, false, false);

      Body body = makeBody(bodyDef, new FixtureDef[] { top, base }, 0.5f, new Vector2(x, y), 0.0f, null);

      body.setLinearDamping(0.2f);
      body.setAngularDamping(0.5f);
      return body;
   }

   public Body makeBody(BodyDef bd, FixtureDef[] fds, float mass, Vector2 position, float angle, Object userData) {
      Body body = world.createBody(bd);

      for (FixtureDef fd : fds) {
         body.createFixture(fd);
      }

      MassData massData = body.getMassData();
      massData.mass = mass;
      body.setMassData(massData);
      body.setUserData(userData);
      body.setTransform(position, angle);

      return body;
   }

   public BodyDef makeBodyDef(BodyType type, boolean bullet, boolean fixedRotation) {
      BodyDef bd = new BodyDef();
      bd.type = type;
      bd.bullet = bullet;
      bd.fixedRotation = fixedRotation;
      return bd;
   }

   public FixtureDef makeFixture(Vector2[] vertices, float density, float friction, float restitution, boolean isSensor, short categoryBits, short maskBits, short groupIndex) {
      FixtureDef fd = makeFixture(vertices, density, friction, restitution, isSensor);
      fd.filter.categoryBits = categoryBits;
      fd.filter.maskBits = maskBits;
      fd.filter.groupIndex = groupIndex;
      return fd;
   }

   public FixtureDef makeFixture(Vector2[] vertices, float density, float friction, float restitution, boolean isSensor) {
      FixtureDef fd = new FixtureDef();
      if (vertices != null) {
         PolygonShape shape = new PolygonShape();
         shape.set(vertices);
         fd.shape = shape;
      }
      fd.density = density;
      fd.friction = friction;
      fd.restitution = restitution;
      fd.isSensor = isSensor;
      return fd;
   }
}
