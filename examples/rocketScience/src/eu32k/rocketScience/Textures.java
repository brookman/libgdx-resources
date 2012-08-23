package eu32k.rocketScience;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Textures {

   private static HashMap<String, Texture> textures = new HashMap<String, Texture>();

   public static void init(String... paths) {
      for (String path : paths) {
         tex(path);
      }
   }

   public static Texture tex(String path) {
      Texture tex = null;
      if (textures.containsKey(path)) {
         tex = textures.get(path);
      } else {
         tex = new Texture(Gdx.files.internal("data/" + path));
         tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
         textures.put(path, tex);
      }
      return tex;
   }

   public static void dispose() {
      for (String key : textures.keySet()) {
         textures.get(key).dispose();
      }
   }
}
