package eu32k.rocketScience;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;

public class Menu extends NinePatch {
   private static Menu instance;

   private Menu() {
      super(new Texture(Gdx.files.internal("data/menuskin.png")), 8, 8, 8, 8);
   }

   public static Menu getInstance() {
      if (instance == null) {
         instance = new Menu();
      }
      return instance;
   }
}