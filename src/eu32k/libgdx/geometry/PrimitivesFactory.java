package eu32k.libgdx.geometry;

import java.io.ByteArrayInputStream;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.loaders.obj.ObjLoader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PrimitivesFactory {

   public static Mesh makeQuad() {
      return makeQuad(false);
   }

   public static Mesh makeQuad(boolean inverted) {
      Mesh quad = new Mesh(true, 4, 0, new VertexAttribute(Usage.Position, 3, ShaderProgram.POSITION_ATTRIBUTE), new VertexAttribute(Usage.TextureCoordinates, 2, ShaderProgram.TEXCOORD_ATTRIBUTE));
      quad.setVertices(new float[] { -1f, 1f, 0f, 0f, inverted ? 1f : 0f, -1f, -1, 0f, 0f, inverted ? 0f : 1f, 1f, -1f, 0f, 1f, inverted ? 0f : 1f, 1f, 1f, 0f, 1f, inverted ? 1f : 0f });
      return quad;
   }

   public static Mesh makeCube() {
      String cubeObj = "" + //
            "v -0.5 -0.5 -0.5\n" + //
            "v -0.5 -0.5 0.5\n" + //
            "v -0.5 0.5 -0.5\n" + //
            "v -0.5 0.5 0.5\n" + //
            "v 0.5 -0.5 -0.5\n" + //
            "v 0.5 -0.5 0.5\n" + //
            "v 0.5 0.5 -0.5\n" + //
            "v 0.5 0.5 0.5\n" + //
            "vn 0.0 0.0 1.0\n" + //
            "vn 0.0 0.0 -1.0\n" + //
            "vn 0.0 1.0 0.0\n" + //
            "vn 0.0 -1.0 0.0\n" + //
            "vn 1.0 0.0 0.0\n" + //
            "vn -1.0 0.0 0.0\n" + //
            "vt 0.0 0.0\n" + //
            "vt 1.0 0.0\n" + //
            "vt 1.0 1.0\n" + //
            "vt 0.0 1.0\n" + //
            "f 1/1/2 7/3/2 5/2/2\n" + //
            "f 1/1/2 3/4/2 7/3/2\n" + //
            "f 1/1/6 4/3/6 3/4/6\n" + //
            "f 1/1/6 2/2/6 4/3/6\n" + //
            "f 3/1/3 8/3/3 7/4/3\n" + //
            "f 3/1/3 4/2/3 8/3/3\n" + //
            "f 5/2/5 7/3/5 8/4/5\n" + //
            "f 5/2/5 8/4/5 6/1/5\n" + //
            "f 1/1/4 5/2/4 6/3/4\n" + //
            "f 1/1/4 6/3/4 2/4/4\n" + //
            "f 2/1/1 6/2/1 8/3/1\n" + //
            "f 2/1/1 8/3/1 4/4/1";
      return ObjLoader.loadObj(new ByteArrayInputStream(cubeObj.getBytes()), true, false);
   }
}
