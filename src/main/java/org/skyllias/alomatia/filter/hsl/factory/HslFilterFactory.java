
package org.skyllias.alomatia.filter.hsl.factory;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.ColourFilter;
import org.skyllias.alomatia.filter.factor.ComposedUnitFactor;
import org.skyllias.alomatia.filter.hsb.function.HueFunction;
import org.skyllias.alomatia.filter.hsl.ContrastConverter;
import org.skyllias.alomatia.filter.hsl.HslColourConverter;
import org.skyllias.alomatia.filter.hsl.HslConverter;
import org.skyllias.alomatia.filter.hsl.HueDependingLightnessConverter;
import org.skyllias.alomatia.filter.hsl.HueDependingSaturationFactorConverter;
import org.skyllias.alomatia.filter.hsl.LightnessConverter;
import org.skyllias.alomatia.filter.hsl.LightnessDependingHueConverter;
import org.skyllias.alomatia.filter.hsl.LightnessPosterizerConverter;
import org.skyllias.alomatia.filter.hsl.SaturationConverter;
import org.skyllias.alomatia.filter.hsl.SaturationPosterizerConverter;

/** Instantiator of filters that play with the HSL components of colours.
 *  <p>
 *  Only the transformations that yield a different result than their HSB
 *  counterpart are offered here. Since the hue is the same in both
 *  colour-spaces, and the saturation and the lightness of a colour do not
 *  depend on its hue, any filter that only modifies the hue produces exactly
 *  the same image in both colour-spaces. */

public class HslFilterFactory
{
//==============================================================================

  public static ImageFilter forLightness(double lightnessFactor) {return forHslConverter(new LightnessConverter(lightnessFactor));}

//------------------------------------------------------------------------------

  public static ImageFilter forSaturation(double saturationFactor) {return forHslConverter(new SaturationConverter(saturationFactor));}

//------------------------------------------------------------------------------

  public static ImageFilter forContrast(double contrastFactor) {return forHslConverter(new ContrastConverter(contrastFactor));}

//------------------------------------------------------------------------------

  public static ImageFilter forHueDependingSaturation(HueFunction function) {return forHslConverter(new HueDependingSaturationFactorConverter(function));}

//------------------------------------------------------------------------------

  public static ImageFilter forHueDependingLightness(HueFunction function) {return forHslConverter(new HueDependingLightnessConverter(function));}

//------------------------------------------------------------------------------

  public static ImageFilter forSaturationPosterizer(int amountOfBuckets, boolean centerThem) {return forHslConverter(new SaturationPosterizerConverter(amountOfBuckets, centerThem));}

//------------------------------------------------------------------------------

  public static ImageFilter forLightnessPosterizer(int amountOfBuckets, boolean centerThem) {return forHslConverter(new LightnessPosterizerConverter(amountOfBuckets, centerThem));}

//------------------------------------------------------------------------------

  public static ImageFilter forLightnessDependingHue(float lowestLightnessHue, float highestLightnessHue, double openFactor)
  {
    return forHslConverter(new LightnessDependingHueConverter(lowestLightnessHue, highestLightnessHue, new ComposedUnitFactor(openFactor)));
  }

//------------------------------------------------------------------------------

  private static ImageFilter forHslConverter(HslConverter hslConverter) {return new ColourFilter(new HslColourConverter(hslConverter));}

//------------------------------------------------------------------------------

}
