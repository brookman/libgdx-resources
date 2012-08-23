package eu32k.libgdx.math;

import java.io.DataInputStream;
import java.io.IOException;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public class AnimationBundle {

   private int numberOfObjects;
   private int numberOfFrames;
   private double[][][] data;

   private int fps;
   private long startTime = 0;

   private int currentFloorIndex = 0;
   private int currentCeilIndex = 0;
   double currentRatio = 0;

   public AnimationBundle(FileHandle filehandle, int fps) {
      this.fps = fps;
      try {
         read(filehandle);
      } catch (IOException e) {
      }
   }

   public void start() {
      startTime = System.currentTimeMillis();
   }

   public void update() {
      double secondsPassed = (System.currentTimeMillis() - startTime) / 1000.0;
      double currentFrame = fps * secondsPassed;

      currentFloorIndex = (int) Math.floor(currentFrame);
      currentCeilIndex = (int) Math.ceil(currentFrame);

      currentFloorIndex = MathUtils.clamp(currentFloorIndex, 0, numberOfFrames - 1);
      currentCeilIndex = MathUtils.clamp(currentCeilIndex, 0, numberOfFrames - 1);

      currentRatio = currentFrame - currentFloorIndex;
   }

   private double linearInterpolation(int objectNumber, int dataIndex) {
      return (1.0 - currentRatio) * data[objectNumber][currentFloorIndex][dataIndex] + currentRatio * data[objectNumber][currentCeilIndex][dataIndex];
   }

   public double getXpos(int objectNumber) {
      return linearInterpolation(objectNumber, 0);
   }

   public double getYpos(int objectNumber) {
      return linearInterpolation(objectNumber, 1);
   }

   public double getZpos(int objectNumber) {
      return linearInterpolation(objectNumber, 2);
   }

   public double getXrot(int objectNumber) {
      return linearInterpolation(objectNumber, 3) / Math.PI * 180.0;
   }

   public double getYrot(int objectNumber) {
      return linearInterpolation(objectNumber, 4) / Math.PI * 180.0;
   }

   public double getZrot(int objectNumber) {
      return linearInterpolation(objectNumber, 5) / Math.PI * 180.0;
   }

   public double getXscale(int objectNumber) {
      return linearInterpolation(objectNumber, 6);
   }

   public double getYscale(int objectNumber) {
      return linearInterpolation(objectNumber, 7);
   }

   public double getZscale(int objectNumber) {
      return linearInterpolation(objectNumber, 8);
   }

   public Vector3 getPos(int objectNumber) {
      return new Vector3((float) getXpos(objectNumber), (float) getYpos(objectNumber), (float) getZpos(objectNumber));
   }

   public Vector3 getRot(int objectNumber) {
      return new Vector3((float) getXrot(objectNumber), (float) getYrot(objectNumber), (float) getZrot(objectNumber));
   }

   public Vector3 getScale(int objectNumber) {
      return new Vector3((float) getXscale(objectNumber), (float) getYscale(objectNumber), (float) getZscale(objectNumber));
   }

   private void read(FileHandle filehandle) throws IOException {
      DataInputStream din = new DataInputStream(filehandle.read());

      numberOfObjects = din.readInt();
      numberOfFrames = din.readInt();

      data = new double[numberOfObjects][numberOfFrames][9];

      for (int object = 0; object < numberOfObjects; object++) {
         for (int frame = 0; frame < numberOfFrames; frame++) {
            for (int dataPoint = 0; dataPoint < 9; dataPoint++) {
               data[object][frame][dataPoint] = din.readFloat();
            }
         }
      }
   }

   public int getNumberOfObjects() {
      return numberOfObjects;
   }

   public int getNumberOfFrames() {
      return numberOfFrames;
   }
}
