
package org.skyllias.alomatia.filter.hsl;

import org.skyllias.alomatia.filter.hsb.UnitQuantizer;

/** Converter that quantizes the lightness of each pixel. */

public class LightnessPosterizerConverter implements HslConverter
{
  private final UnitQuantizer quantizer;

//==============================================================================

  public LightnessPosterizerConverter(int amountOfBuckets, boolean centerThem)
  {
    quantizer = new UnitQuantizer(amountOfBuckets, centerThem);
  }

//==============================================================================

  @Override
  public float getNewLightness(float hue, float saturation, float lightness)
  {
    return quantizer.getQuantized(lightness);
  }

//------------------------------------------------------------------------------

}
