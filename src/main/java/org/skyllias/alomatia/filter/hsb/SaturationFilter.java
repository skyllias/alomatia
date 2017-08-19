
package org.skyllias.alomatia.filter.hsb;

/** Filter that increases or decreases the saturation of the colours in an image
 *  by a non-linear factor. */

public class SaturationFilter extends BasicHSBFilter
{
  private double factor;                                                        // this goes from 0 to infinite, with 1 producing no change, lower values reducing the saturation and higher values increasing it

//==============================================================================

  /** Creates a filter that modifies the saturation of images according to the
   *  value of saturationFactor:
   *  - If it is 0, then the saturation does not change.
   *  - If is is lower than 0, it is reduced.
   *  - If is is higher than 0, it is increased.
   *  - With large negative numbers (3 and above), the saturation is nearly removed.
   *  - With large positive numbers (3 and above), the saturation is nearly saturated.
   *  - The first noticeable differences occur with absolute values of the order of 0.1. */

  public SaturationFilter(double saturationFactor)
  {
    factor = Math.exp(saturationFactor);                                        // this pre-calculation avoids a lot of redundant exponentials later on
  }

//==============================================================================

  /** Applies the factor to the original saturation avoiding results outside [0, 1] by:
   *  If the factor is below 1, multiplying the original saturation by it.
   *  If the factor is over 1, multiplying its inverse by the "insaturation" (ie
   *  by (1 - saturation) and substracting that from 1). */

  @Override
  protected float getNewSaturation(float hue, float saturation, float brightness)
  {
    if (factor <= 1) return (float) (factor * saturation);
    else             return (float) (1 - (1 - saturation) / factor);
  }

//------------------------------------------------------------------------------

}
