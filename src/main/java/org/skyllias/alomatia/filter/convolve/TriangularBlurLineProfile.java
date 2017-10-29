
package org.skyllias.alomatia.filter.convolve;

/** {@link BlurLineProfile} that makes each pixel contribute a fixed amount less
 *  than the previous one. */

public class TriangularBlurLineProfile implements BlurLineProfile
{
//==============================================================================

  @Override
  public float[] getProfile(int length)
  {
    float[] result = new float[length];
    float weight   = 2f / (length * (length + 1));                              // thank you Carl Friedrich Gauss
    for (int i = 0; i < length; i++) result[i] = (length - i) * weight;
    return result;
  }

//------------------------------------------------------------------------------

}
