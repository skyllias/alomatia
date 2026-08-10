
package org.skyllias.alomatia.filter.hsl;

import java.awt.Color;

import org.skyllias.alomatia.filter.ColourConverter;

/** Converter that works in the hue-saturation-lightness colour-space,
 *  delegating transformations to an HslConverter.
 *  <p>
 *  Since {@link Color} only offers conversions to and from HSB, and the hue is
 *  the same in both colour-spaces, the HSL coordinates are derived from the HSB
 *  ones: the lightness is the middle point between the highest and the lowest
 *  RGB components, and the saturation is the chroma relative to the maximum
 *  chroma available at that lightness. */

public class HslColourConverter implements ColourConverter
{
  private final HslConverter hslConverter;

//==============================================================================

  public HslColourConverter(HslConverter hslConverter)
  {
    this.hslConverter = hslConverter;
  }

//==============================================================================

  @Override
  public Color convertColour(Color original)
  {
    int red   = original.getRed();
    int green = original.getGreen();
    int blue  = original.getBlue();

    float[] originalHsb      = Color.RGBtoHSB(red, green, blue, null);
    float originalHue        = originalHsb[0];
    float originalBrightness = originalHsb[2];
    float originalLightness  = calculateLightness(originalHsb[1], originalBrightness);
    float originalSaturation = calculateSaturation(originalBrightness, originalLightness);

    float newHue        = hslConverter.getNewHue(originalHue, originalSaturation, originalLightness);
    float newSaturation = hslConverter.getNewSaturation(originalHue, originalSaturation, originalLightness);
    float newLightness  = hslConverter.getNewLightness(originalHue, originalSaturation, originalLightness);

    float newBrightness    = calculateBrightness(newSaturation, newLightness);
    float newHsbSaturation = calculateHsbSaturation(newBrightness, newLightness);
    return Color.getHSBColor(newHue, newHsbSaturation, newBrightness);
  }

//------------------------------------------------------------------------------

  private float calculateLightness(float hsbSaturation, float brightness)
  {
    return brightness * (1 - hsbSaturation / 2);
  }

//------------------------------------------------------------------------------

  private float calculateSaturation(float brightness, float lightness)
  {
    float halfMaxChroma = getHalfMaxChroma(lightness);
    if (halfMaxChroma == 0) return 0;

    return (brightness - lightness) / halfMaxChroma;
  }

//------------------------------------------------------------------------------

  private float calculateBrightness(float saturation, float lightness)
  {
    return lightness + saturation * getHalfMaxChroma(lightness);
  }

//------------------------------------------------------------------------------

  private float calculateHsbSaturation(float brightness, float lightness)
  {
    if (brightness == 0) return 0;

    return 2 * (1 - lightness / brightness);
  }

//------------------------------------------------------------------------------

  private float getHalfMaxChroma(float lightness)
  {
    return Math.min(lightness, 1 - lightness);
  }

//------------------------------------------------------------------------------

}
