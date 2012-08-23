package eu32k.libgdx.math;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Interpolation {

   private double start;
   private double end;

   private Vector2 start2;
   private Vector2 end2;

   private Vector3 start3;
   private Vector3 end3;

   private Type type;

   public Interpolation(double start, double end, Type type) {
      this.start = start;
      this.end = end;
      this.type = type;
   }

   public Interpolation(Vector2 start, Vector2 end, Type type) {
      start2 = new Vector2(start);
      end2 = new Vector2(end);
      this.type = type;
   }

   public Interpolation(Vector3 start, Vector3 end, Type type) {
      start3 = new Vector3(start);
      end3 = new Vector3(end);
      this.type = type;
   }

   public double getValue(double pos) {
      return getValue(start, end, pos);
   }

   public Vector2 getValue2(double pos) {
      return new Vector2((float) getValue(start2.x, end2.x, pos), (float) getValue(start2.y, end2.y, pos));
   }

   public Vector3 getValue3(double pos) {
      return new Vector3((float) getValue(start3.x, end3.x, pos), (float) getValue(start3.y, end3.y, pos), (float) getValue(start3.z, end3.z, pos));
   }

   public double getValue(double start, double end, double pos) {
      double v = 0;
      if (type == Type.LINEAR) {
         v = pos;
      } else if (type == Type.SMOOTH_STEP) {
         v = pos * pos * (3.0 - 2.0 * pos);
      } else if (type == Type.SQUARED) {
         v = pos * pos;
      } else if (type == Type.INV_SQUARED) {
         v = 1.0 - (1.0 - pos) * (1.0 - pos);
      } else if (type == Type.CUBED) {
         v = pos * pos * pos;
      } else if (type == Type.INV_CUBED) {
         v = 1.0 - (1.0 - pos) * (1.0 - pos) * (1.0 - pos);
      } else if (type == Type.SIN) {
         v = Math.sin(pos * Math.PI / 2.0);
      } else if (type == Type.INV_SIN) {
         v = Math.sin((1.0 - pos) * Math.PI / 2.0);
      } else if (type == Type.SMOOTH_COS) {
         v = 0.5 - Math.cos(-pos * Math.PI) * 0.5;
      }

      return end * v + start * (1 - v);
   }

   public static enum Type {
      LINEAR, SMOOTH_STEP, SQUARED, INV_SQUARED, CUBED, INV_CUBED, SIN, INV_SIN, SMOOTH_COS
   }

}
