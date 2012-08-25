package eu32k.libgdx.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;

import eu32k.libgdx.geometry.PrimitivesFactory;

public class Mixer implements Disposable {

   private Renderer renderer1;
   private Renderer renderer2;
   private Mesh quad;
   private ShaderProgram shader;

   private float factor1 = 1.0f;
   private float factor2 = 1.0f;

   public float noise = 0.0f;

   public Mixer(Renderer renderer1, Renderer renderer2, boolean inverted) {
      this.renderer1 = renderer1;
      this.renderer2 = renderer2;
      quad = PrimitivesFactory.makeQuad(inverted);
      shader = new ShaderProgram(Gdx.files.internal("shaders/simple.vsh").readString(), Gdx.files.internal("shaders/mixer.fsh").readString());
      System.out.println(shader.getLog());
   }

   public void render() {
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

      Gdx.gl.glActiveTexture(GL20.GL_TEXTURE1);
      renderer1.getTexture().bind();

      Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
      renderer2.getTexture().bind();

      shader.begin();
      shader.setUniformi("uTexture1", 1);
      shader.setUniformi("uTexture2", 0);

      shader.setUniformf("uFactor1", factor1);
      shader.setUniformf("uFactor2", factor2);
      shader.setUniformf("uNoise", noise);

      quad.render(shader, GL20.GL_TRIANGLE_FAN);
      shader.end();
   }

   public float getFactor1() {
      return factor1;
   }

   public void setFactor1(float factor1) {
      this.factor1 = factor1;
   }

   public float getFactor2() {
      return factor2;
   }

   public void setFactor2(float factor2) {
      this.factor2 = factor2;
   }

   @Override
   public void dispose() {
      quad.dispose();
      shader.dispose();
   }
}
