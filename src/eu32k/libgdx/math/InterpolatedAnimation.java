package eu32k.libgdx.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InterpolatedAnimation extends Interpolation {

   private long timeSpan;
   private long startTime;
   private boolean running = false;

   public InterpolatedAnimation(double start, double end, Type type, long timeSpan) {
      super(start, end, type);
      this.timeSpan = timeSpan;
   }

   public InterpolatedAnimation(Vector2 start, Vector2 end, Type type, long timeSpan) {
      super(start, end, type);
      this.timeSpan = timeSpan;
   }

   public InterpolatedAnimation(Vector3 start, Vector3 end, Type type, long timeSpan) {
      super(start, end, type);
      this.timeSpan = timeSpan;
   }

   public void start() {
      startAt(System.currentTimeMillis());
   }

   public void startAt(long time) {
      startTime = time;
      running = true;
   }

   public void startDelayed(long delay) {
      startTime = System.currentTimeMillis() + delay;
      running = true;
   }

   public void reset() {
      running = false;
   }

   public boolean isRunning() {
      return running;
   }

   private double getPos() {
      if (startTime > System.currentTimeMillis()) {
         return 0.0;
      }

      long diff = System.currentTimeMillis() - startTime;
      double pos = (double) diff / (double) timeSpan;

      return Math.max(Math.min(pos, 1.0), 0.0);
   }

   public double getValue() {
      if (!running) {
         return this.getValue(0.0);
      }

      return getValue(getPos());
   }

   public Vector2 getValue2() {
      if (!running) {
         return this.getValue2(0.0);
      }

      return getValue2(getPos());
   }

   public Vector3 getValue3() {
      if (!running) {
         return this.getValue3(0.0);
      }

      return getValue3(getPos());
   }
}
