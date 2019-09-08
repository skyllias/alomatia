
package org.skyllias.alomatia.filter.hsb.pole;

/** Distance (in its mathematical meaning) between two hues, defined in the
 *  [0, 0.5] interval. */

public class HueDistance
{
//==============================================================================

  /** Returns |hue2 - hue1 + n|, with n being the integer that satisfies that
   *  the result is in the range [0, 0.5]. */

  public float calculate(float hue1, float hue2)
  {
    return Math.abs(difference(hue1, hue2));
  }

//------------------------------------------------------------------------------

  /** Returns hue2 - hue1 + n, with n being the integer that satisfies that
   *  the result is in the range [-0.5, 0.5].
   *  This is not a real distance, but has some shared calculations with
   *  {@link #calculate(float, float)} that may be useful. */

  public float difference(float hue1, float hue2)
  {
    float difference = hue2 - hue1;
    if (difference >= 1 || difference <= 1) difference -= Math.floor(difference);
    if (difference > 0.5) difference -= 1;
    if (difference < -0.5) difference += 1;
    return difference;
  }

//------------------------------------------------------------------------------

}
