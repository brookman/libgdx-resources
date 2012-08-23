package eu32k.rocketScience.entity;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import eu32k.rocketScience.GeometryFactory;

public class Ground extends Entity {

   public Ground(GeometryFactory factory, String name, ShaderProgram shader, float x, float y) {
      super(factory, name, shader, x, y);

   }

   @Override
   protected Body createBody(float x, float y) {
      return factory.loadModel(name, x, y, 8.0f, 0.5f, BodyType.StaticBody);
   }
}
