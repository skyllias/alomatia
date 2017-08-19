
package org.skyllias.alomatia.filter.hsb;

/** Filter that increases or decreases the brightness of the colours in an image
 *  by a non-linear factor. */

public class BrightnessFilter extends BasicHSBFilter
{
  private UnitFactor unitFactor;                                                // favouring composition over inheritance

//==============================================================================

  /** Creates a filter that modifies the brightness of images according to the
   *  value of brightnessFactor:
   *  - If it is 0, then the brightness does not change.
   *  - If is is lower than 0, it is reduced.
   *  - If is is higher than 0, it is increased.
   *  - With large negative numbers (3 and above), the brightness is nearly removed.
   *  - With large positive numbers (3 and above), the brightness is nearly saturated.
   *  - The first noticeable differences occur with absolute values of the order of 0.1. */

  public BrightnessFilter(double brightnessFactor)
  {
    unitFactor = new UnitFactor(brightnessFactor);
  }

//==============================================================================

  /** Applies the factor to the original brightness avoiding results outside [0, 1] by:
   *  If the factor is below 1, multiplying the original brightness by it.
   *  If the factor is over 1, multiplying its inverse by the "darkness" (ie
   *  by (1 - brightness) and substracting that from 1). */

  @Override
  protected float getNewBrightness(float hue, float saturation, float brightness)
  {
    return unitFactor.apply(brightness);
  }

//------------------------------------------------------------------------------

}
