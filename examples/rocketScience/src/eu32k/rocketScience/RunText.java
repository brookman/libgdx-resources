package eu32k.rocketScience;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class RunText extends BitmapFont {

   private String text;
   private long runTime;
   private long startTime;
   private boolean finished;

   public RunText(String text, float runTime) {
      super(Gdx.files.internal("data/fonts/calibri.fnt"), Gdx.files.internal("data/fonts/calibri.png"), false);
      this.text = text;
      this.runTime = (long) (runTime * 1000.0f);
      reset();
   }

   public void reset() {
      startTime = System.currentTimeMillis();
      finished = false;
   }

   public void draw(SpriteBatch batch, float x, float y) {
      if (startTime == 0) {
         return;
      }

      double progress = (double) (System.currentTimeMillis() - startTime) / (double) runTime;
      finished = progress >= 1.0;
      int end = (int) Math.round(text.length() * progress);
      end = MathUtils.clamp(end, 0, text.length());
      super.draw(batch, text, x, y, 0, end);
   }

   public boolean isFinished() {
      return finished;
   }
}
