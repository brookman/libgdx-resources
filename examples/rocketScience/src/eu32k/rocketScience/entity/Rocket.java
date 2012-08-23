package eu32k.rocketScience.entity;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;

import eu32k.rocketScience.GeometryFactory;

public class Rocket extends Entity {

   public Fixture head;

   public Rocket(GeometryFactory factory, ShaderProgram shader) {
      super(factory, "rocket.png", shader);
   }

   @Override
   protected Body createBody(float x, float y) {
      Body body = factory.loadModel(name, 0.0f, 0.0f, 8.0f, 6.0f, BodyType.DynamicBody);
      head = body.getFixtureList().get(3);
      head.setSensor(true);
      return body;
   }
}
