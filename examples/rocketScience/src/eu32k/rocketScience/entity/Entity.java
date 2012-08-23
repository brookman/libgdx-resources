package eu32k.rocketScience.entity;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;

import eu32k.rocketScience.GeometryFactory;
import eu32k.rocketScience.Textures;

public class Entity {

   // private Texture texture;
   // private PolygonShape poly;

   private class FixtureData {
      public Vector2[] vertices;
      public Mesh mesh;
      public float[] meshesData;

      public FixtureData(Fixture fixture) {
         PolygonShape poly = (PolygonShape) fixture.getShape();

         // get vertices
         vertices = new Vector2[poly.getVertexCount()];
         for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vector2();
            poly.getVertex(i, vertices[i]);
         }

         mesh = new Mesh(true, poly.getVertexCount(), 0, new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.ColorPacked, 4,
               ShaderProgram.COLOR_ATTRIBUTE), new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE));

         meshesData = new float[vertices.length * 6];
      }

      public void updateData(Transform transform) {
         Vector2 before;
         Vector2 vector;
         Vector2 after;
         for (int i = 0; i < vertices.length; i++) {
            before = vertices[i == 0 ? vertices.length - 1 : i - 1];
            vector = vertices[i].cpy();
            after = vertices[i == vertices.length - 1 ? 0 : i + 1];

            // Vector2D vec1 = new Vector2D(new Point2D(after.x, after.y), new Point2D(vector.x, vector.y));
            // Vector2D vec2 = new Vector2D(new Point2D(before.x, before.y), new Point2D(vector.x, vector.y));
            //
            // double a1 = vec1.getAngle();
            // double a2 = vec2.getAngle();
            // double diff = a1 - a2;
            // double angle = (a1 + a2) / 2.0;
            //
            // if (diff > 0) {
            // angle -= Math.PI;
            // }
            //
            // Vector2D sum = new Vector2D(Math.cos(angle) * 0.2, Math.sin(angle) * 0.2);
            // sum.normalize();
            //
            // sum = sum.times(0.04);
            //
            // Vector2 newPoint = new Vector2(vector.x + (float) sum.getX(), vector.y + (float) sum.getY());
            //
            // vector = newPoint.cpy();

            transform.mul(vector);

            int dataIndex = i * 6;
            meshesData[dataIndex] = vector.x;
            meshesData[dataIndex + 1] = vector.y;
            meshesData[dataIndex + 2] = 0;
            meshesData[dataIndex + 3] = Color.toFloatBits(128, 128, 128, 255);
            meshesData[dataIndex + 4] = vertices[i].x / 8.0f + 0.5f;
            meshesData[dataIndex + 5] = 1.0f - (vertices[i].y / 8.0f + 0.5f);
         }
         mesh.setVertices(meshesData);
      }
   }

   protected GeometryFactory factory;
   protected String name;

   private Body body;
   private Texture texture;
   private ShaderProgram shader;

   private FixtureData[] fixtureData;

   public Entity(GeometryFactory factory, String name, ShaderProgram shader) {
      this(factory, name, shader, 0.0f, 0.0f);
   }

   public Entity(GeometryFactory factory, String name, ShaderProgram shader, float x, float y) {
      this.factory = factory;
      this.name = name;
      this.shader = shader;
      body = createBody(x, y);
      texture = Textures.tex("models/" + name);

      List<Fixture> fixtureList = body.getFixtureList();
      fixtureData = new FixtureData[fixtureList.size()];
      for (int i = 0; i < fixtureList.size(); i++) {
         fixtureData[i] = new FixtureData(fixtureList.get(i));
      }

   }

   protected Body createBody(float x, float y) {
      return factory.loadModel(name, x, y, 8.0f, 0.0f, BodyType.StaticBody);
   }

   public void draw(Matrix4 mat) {
      if (!body.isActive()) {
         return;
      }

      Transform transform = body.getTransform();

      texture.bind();
      shader.begin();
      shader.setUniformMatrix("u_projectionViewMatrix", mat);
      for (FixtureData data : fixtureData) {
         data.updateData(transform);
         data.mesh.render(shader, GL20.GL_TRIANGLE_FAN, 0, data.vertices.length);
      }
      shader.end();

      // chain.getVertex(0, v0);
      // transform.mul(v0);
      // chain.getVertex(1, v1);
      // transform.mul(v1);
      // chain.getVertex(2, v2);
      // transform.mul(v2);
      // chain.getVertex(3, v3);
      // transform.mul(v3);

      // mesh.setVertices(new float[] { -5f, -5f, 0, 0, 1, // bottom left
      // 5f, -5f, 0, 1, 1, // bottom right
      // 5f, 5f, 0, 1, 0, // top right
      // -5f, 5f, 0, 0, 0 }); // top left
      //
      // mesh.setVertices(new float[] { v0.x, v0.y, 0, 0, 1, // bottom left
      // v1.x, v1.y, 0, 1, 1, // bottom right
      // v2.x, v2.y, 0, 1, 0, // top right
      // v3.x, v3.y, 0, 0, 0 }); // top left

      // System.out.println("--");

      // Vector2 before = new Vector2();
      // Vector2 current = new Vector2();
      // Vector2 after = new Vector2();
      // for (int i = 0; i < meshes.length; i++) {
      // Fixture fixture = body.getFixtureList().get(i);
      // PolygonShape poly = (PolygonShape) fixture.getShape();
      //
      // for (int j = 0; j < meshesData[i].length; j += 6) {
      // int polyIndex = j / 6;
      // poly.getVertex(polyIndex, current);
      //
      // // if (scale) {
      // // poly.getVertex(polyIndex == 0 ? poly.getVertexCount() - 1 :
      // polyIndex - 1, before);
      // // poly.getVertex(polyIndex == poly.getVertexCount() - 1 ? 0 :
      // polyIndex + 1, after);
      // //
      // // Vector2D vec1 = new Vector2D(new Point2D(after.x, after.y), new
      // Point2D(current.x, current.y));
      // // Vector2D vec2 = new Vector2D(new Point2D(before.x, before.y), new
      // Point2D(current.x, current.y));
      // //
      // // double a1 = vec1.getAngle();
      // // double a2 = vec2.getAngle();
      // // double diff = a1 - a2;
      // // double angle = (a1 + a2) / 2.0;
      // //
      // // if (diff > 0) {
      // // angle -= Math.PI;
      // // }
      // //
      // // System.out.println("angle1 " + a1 * 180 / Math.PI);
      // // System.out.println("angle2 " + a2 * 180 / Math.PI);
      // // System.out.println("diff " + diff * 180 / Math.PI);
      // // System.out.println("center " + angle * 180 / Math.PI);
      // //
      // // Vector2D sum = new Vector2D(Math.cos(angle) * 0.2, Math.sin(angle) *
      // 0.2);
      // // sum.normalize();
      // //
      // // sum = sum.times(0.2);
      // //
      // // Vector2 newPoint = new Vector2(current.x + (float) sum.getX(),
      // current.y + (float) sum.getY());
      // //
      // // transform.mul(newPoint);
      // //
      // // transform.mul(current);
      // //
      // // // poly.getVertex(polyIndex, newPoint);
      // // // newPoint.mul(1.2f);
      // // // transform.mul(newPoint);
      // //
      // // // batch.begin();
      // // // batch.draw(texture, current.x * 20 + 200, current.y * 20 + 200);
      // // // batch.draw(texture2, newPoint.x * 20 + 200, newPoint.y * 20 +
      // 200);
      // // // batch.end();
      // // }
      //
      // transform.mul(current);
      //
      // meshesData[i][j] = current.x;
      // meshesData[i][j + 1] = current.y;
      // meshesData[i][j + 2] = 0;
      //
      // meshesData[i][j + 3] = colors[(color + i) % colors.length];
      //
      // meshesData[i][j + 4] = (float) Math.random();
      // meshesData[i][j + 5] = (float) Math.random();
      // }
      //
      // // render
      // meshes[i].setVertices(meshesData[i]);
      //
      // shader.begin();
      // shader.setUniformMatrix("u_projectionViewMatrix", mat);
      // texture.bind();
      // meshes[i].render(shader, GL20.GL_TRIANGLE_FAN, 0,
      // poly.getVertexCount());
      // shader.end();
      // }

   }

   public Body getBody() {
      return body;
   }
}
