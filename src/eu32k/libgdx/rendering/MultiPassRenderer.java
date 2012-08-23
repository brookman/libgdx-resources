package eu32k.libgdx.rendering;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;

public class MultiPassRenderer extends Renderer {

   private List<Renderer> renderers;

   public MultiPassRenderer(List<Renderer> renderers) {
      this.renderers = renderers;
      if (renderers.size() < 2) {
         throw new IllegalArgumentException("List size must be > 1");
      }
   }

   @Override
   public void begin() {
      renderers.get(0).begin();
   }

   @Override
   public void end() {
      renderers.get(0).end();
      for (int i = 1; i < renderers.size(); i++) {
         renderers.get(i).begin();
         renderers.get(i - 1).render();
         renderers.get(i).end();
      }
   }

   @Override
   public void render() {
      renderers.get(renderers.size() - 1).render();
   }

   @Override
   public Texture getTexture() {
      return renderers.get(renderers.size() - 1).getTexture();
   }
}
