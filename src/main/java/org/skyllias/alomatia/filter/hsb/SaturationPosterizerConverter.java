
package org.skyllias.alomatia.filter.hsb;

/** Converter that quantizes the saturation of each pixel. */

public class SaturationPosterizerConverter implements HsbConverter
{
  private final UnitQuantizer quantizer;

//==============================================================================

  public SaturationPosterizerConverter(int amountOfBuckets, boolean centerThem)
  {
    quantizer = new UnitQuantizer(amountOfBuckets, centerThem);
  }

//==============================================================================

  @Override
  public float getNewSaturation(float hue, float saturation, float brightness)
  {
    return quantizer.getQuantized(saturation);
  }

//------------------------------------------------------------------------------

}
