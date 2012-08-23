package eu32k.libgdx.example;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import eu32k.libgdx.geometry.GeometricObject;

public class Cube extends GeometricObject {

   public float speed;

   public Cube(ShaderProgram shader, Mesh mesh, int type, Texture texture) {
      super(shader, mesh, type, texture);
      speed = 0;
   }

}
