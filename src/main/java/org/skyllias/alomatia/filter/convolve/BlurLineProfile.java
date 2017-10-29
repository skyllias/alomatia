
package org.skyllias.alomatia.filter.convolve;

/** Definition of how much the pixels in a line contribute to a linear blur. */

public interface BlurLineProfile
{
  /** Returns an array of length items whose sum is 1.
   *  The first item corresponds to the first pixel along the line. */

  float[] getProfile(int length);
}
