
package org.skyllias.alomatia.filter.hsl;

/** Converter of coordinates in the HSL colour-space.
 *
 *  Implementations only have to override the methods that do not return the
 *  original component unaltered. */

public interface HslConverter
{
  /** Returns the transformed hue for a colour with the passed HSL values. */

  default float getNewHue(float hue, float saturation, float lightness)
  {
    return hue;
  }

  /** Returns the transformed saturation for a colour with the passed HSL values. */

  default float getNewSaturation(float hue, float saturation, float lightness)
  {
    return saturation;
  }

  /** Returns the transformed lightness for a colour with the passed HSL values. */

  default float getNewLightness(float hue, float saturation, float lightness)
  {
    return lightness;
  }

}
