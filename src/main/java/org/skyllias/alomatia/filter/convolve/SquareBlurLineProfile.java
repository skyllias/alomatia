
package org.skyllias.alomatia.filter.convolve;

/** {@link BlurLineProfile} that makes each pixel contribute like the square of
 *  its distance from the end. */

public class SquareBlurLineProfile implements BlurLineProfile
{
//==============================================================================

  @Override
  public float[] getProfile(int length)
  {
    float[] result = new float[length];
    float sum      = 0;
    for (int i = 0; i < length; i++)
    {
      float square = (length - i) * (length - i);
      sum         += square;
      result[i]    = square;
    }
    for (int i = 0; i < length; i++) result[i] = result[i] / sum;               // normalize it
    return result;
  }

//------------------------------------------------------------------------------

}
