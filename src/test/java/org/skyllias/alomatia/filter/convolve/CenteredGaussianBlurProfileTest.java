
package org.skyllias.alomatia.filter.convolve;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class CenteredGaussianBlurProfileTest
{
  private static final float ERROR_TOLERANCE = 0.00001f;

  @Test
  public void shouldGenerateSmallGaussian()
  {
    float[] profile = new CenteredGaussianBlurProfile().getProfile(5);

    assertArrayEquals(new float[] {0.006646036f, 0.19422556f, 0.5982568f,
                                   0.19422556f, 0.006646036f},
                      profile, ERROR_TOLERANCE);
  }

  @Test
  public void shouldGenerateBigGaussian()
  {
    float[] profile = new CenteredGaussianBlurProfile().getProfile(15);

    assertArrayEquals(new float[] {0.0019016457f, 0.0062751486f, 0.01723257f,
                                   0.03938291f, 0.07490264f, 0.118554495f,
                                   0.1561603f, 0.1711807f, 0.1561603f,
                                   0.118554495f, 0.07490264f, 0.03938291f,
                                   0.01723257f, 0.0062751486f, 0.0019016457f},
                      profile, ERROR_TOLERANCE);
  }

}
