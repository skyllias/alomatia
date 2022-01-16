
package org.skyllias.alomatia.filter.hsb;

/** Converter that keeps the original saturation and brightness and always
 *  returns the same fixed hue. */

public class FixedHueConverter implements HsbConverter
{
  private final float fixedHue;

//==============================================================================

  public FixedHueConverter(float fixedHue)
  {
    this.fixedHue = fixedHue;
  }

//==============================================================================

  @Override
  public float getNewHue(float hue, float saturation, float brightness)
  {
    return fixedHue;
  }

//------------------------------------------------------------------------------

}
