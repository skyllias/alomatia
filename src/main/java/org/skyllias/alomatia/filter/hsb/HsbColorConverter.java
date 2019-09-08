
package org.skyllias.alomatia.filter.hsb;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColorConverter;

/** Converter that works in the hue-saturation-brightness colour-space,
 *  delegating transformations to an HsbConverter. */

public class HsbColorConverter implements ColorConverter
{
  private final HsbConverter hsbConverter;

//==============================================================================

  public HsbColorConverter(HsbConverter hsbConverter)
  {
    this.hsbConverter = hsbConverter;
  }

//==============================================================================

  @Override
  public Color convertColor(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    float[] originalHsb      = Color.RGBtoHSB(red, green, blue, null);
    float originalHue        = originalHsb[0];
    float originalSaturation = originalHsb[1];
    float originalBrightness = originalHsb[2];

    float newHue        = hsbConverter.getNewHue(originalHue, originalSaturation, originalBrightness);
    float newSaturation = hsbConverter.getNewSaturation(originalHue, originalSaturation, originalBrightness);
    float newBrightness = hsbConverter.getNewBrightness(originalHue, originalSaturation, originalBrightness);
    return Color.getHSBColor(newHue, newSaturation, newBrightness);
  }

//------------------------------------------------------------------------------

}
