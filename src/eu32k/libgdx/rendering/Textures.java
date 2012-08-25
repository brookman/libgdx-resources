package eu32k.libgdx.rendering;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Textures {

   private static HashMap<String, Texture> textures = new HashMap<String, Texture>();

   public static Texture get(String path) {
      Texture texture = textures.get(path);
      if (texture == null) {
         texture = new Texture(Gdx.files.internal(path), true);
      }
      return texture;
   }

   public static void dispose() {
      for (String key : textures.keySet()) {
         textures.get(key).dispose();
      }
   }
}
