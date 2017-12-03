
package org.skyllias.alomatia.filter.hsb;

/** Filter that quantizes the saturation of each pixel. */

public class SaturationPosterizerFilter extends BasicHSBFilter
{
  private UnitQuantizer quantizer;

//==============================================================================

  public SaturationPosterizerFilter(int amountOfBuckets, boolean centerThem)
  {
    quantizer = new UnitQuantizer(amountOfBuckets, centerThem);
  }

//==============================================================================

  @Override
  protected float getNewSaturation(float hue, float saturation, float brightness)
  {
    return quantizer.getQuantized(saturation);
  }

//------------------------------------------------------------------------------

}
