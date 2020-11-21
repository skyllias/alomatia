
package org.skyllias.alomatia.filter.convolve;

/** {@link BlurLineProfile} with a Gaussian profile centered along the segment
 *  and with a standard deviation such that the contribution of the edges is
 *  just at the brink of nihilability. */

public class CenteredGaussianBlurProfile implements BlurLineProfile
{
//==============================================================================

  @Override
  public float[] getProfile(int length)
  {
    float[] result = new float[length];

    float center      = (length - 1f) / 2;
    float sigma       = center / 3;
    float twoVariance = 2 * sigma * sigma;

    float accumulatedWeight = 0;
    for (int i = 0; i < length; i++)
    {
      float x     = center - i;
      float value = (float) Math.exp(- x * x / twoVariance);

      result[i]          = value;
      accumulatedWeight += value;
    }

    for (int i = 0; i < length; i++) result[i] /= accumulatedWeight;            // normalize

    return result;
  }

//------------------------------------------------------------------------------

}
