
package org.skyllias.alomatia.filter.hsb;

/** Converter of coordinates in the HSB colour-space.
 *
 *  Implementations only have to override the methods that do not return the
 *  original component unaltered. */

public interface HsbConverter
{
  /** Returns the transformed hue for a colour with the passed HSB values. */

  default float getNewHue(float hue, float saturation, float brightness)
  {
    return hue;
  }

  /** Returns the transformed saturation for a colour with the passed HSB values. */

  default float getNewSaturation(float hue, float saturation, float brightness)
  {
    return saturation;
  }

  /** Returns the transformed brightness for a colour with the passed HSB values. */

  default float getNewBrightness(float hue, float saturation, float brightness)
  {
    return brightness;
  }

}
