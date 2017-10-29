
package org.skyllias.alomatia.filter.convolve;

/** {@link BlurLineProfile} that makes all the pixels contribute the same. */

public class UniformedBlurLineProfile implements BlurLineProfile
{
//==============================================================================

  @Override
  public float[] getProfile(int length)
  {
    float[] result = new float[length];
    float weight   = 1f / length;
    for (int i = 0; i < length; i++) result[i] = weight;
    return result;
  }

//------------------------------------------------------------------------------

}
