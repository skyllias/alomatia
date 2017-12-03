
package org.skyllias.alomatia.filter.hsb;

/** Filter that quantizes the brightness of each pixel. */

public class BrightnessPosterizerFilter extends BasicHSBFilter
{
  private UnitQuantizer quantizer;

//==============================================================================

  public BrightnessPosterizerFilter(int amountOfBuckets, boolean centerThem)
  {
    quantizer = new UnitQuantizer(amountOfBuckets, centerThem);
  }

//==============================================================================

  @Override
  protected float getNewBrightness(float hue, float saturation, float brightness)
  {
    return quantizer.getQuantized(brightness);
  }

//------------------------------------------------------------------------------

}
