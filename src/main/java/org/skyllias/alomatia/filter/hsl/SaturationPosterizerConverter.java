
package org.skyllias.alomatia.filter.hsl;

import org.skyllias.alomatia.filter.hsb.UnitQuantizer;

/** Converter that quantizes the saturation of each pixel. */

public class SaturationPosterizerConverter implements HslConverter
{
  private final UnitQuantizer quantizer;

//==============================================================================

  public SaturationPosterizerConverter(int amountOfBuckets, boolean centerThem)
  {
    quantizer = new UnitQuantizer(amountOfBuckets, centerThem);
  }

//==============================================================================

  @Override
  public float getNewSaturation(float hue, float saturation, float lightness)
  {
    return quantizer.getQuantized(saturation);
  }

//------------------------------------------------------------------------------

}
