
package org.skyllias.alomatia.filter.hsb.factory;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.ColourFilter;
import org.skyllias.alomatia.filter.compose.ComposedFilter;
import org.skyllias.alomatia.filter.factor.ComposedUnitFactor;
import org.skyllias.alomatia.filter.hsb.BrightnessConverter;
import org.skyllias.alomatia.filter.hsb.BrightnessDependingHueConverter;
import org.skyllias.alomatia.filter.hsb.BrightnessPosterizerConverter;
import org.skyllias.alomatia.filter.hsb.ContrastConverter;
import org.skyllias.alomatia.filter.hsb.FixedHueConverter;
import org.skyllias.alomatia.filter.hsb.HsbColourConverter;
import org.skyllias.alomatia.filter.hsb.HsbConverter;
import org.skyllias.alomatia.filter.hsb.HueDependingBrightnessConverter;
import org.skyllias.alomatia.filter.hsb.HueDependingSaturationFactorConverter;
import org.skyllias.alomatia.filter.hsb.HuePosterizerConverter;
import org.skyllias.alomatia.filter.hsb.HueReverserConverter;
import org.skyllias.alomatia.filter.hsb.HueShiftConverter;
import org.skyllias.alomatia.filter.hsb.SaturationConverter;
import org.skyllias.alomatia.filter.hsb.SaturationPosterizerConverter;
import org.skyllias.alomatia.filter.hsb.function.FangHueFunction;
import org.skyllias.alomatia.filter.hsb.function.HueFunction;
import org.skyllias.alomatia.filter.hsb.pole.Attraction;
import org.skyllias.alomatia.filter.hsb.pole.ClosestPoleHueConverter;
import org.skyllias.alomatia.filter.hsb.pole.CombinedPoleHueConverter;
import org.skyllias.alomatia.filter.hsb.pole.LinearRepulsion;

/** Instantiator of filters that play with the HSB components of colours. */

public class HsbFilterFactory
{
//==============================================================================

  public static ImageFilter forHueShift(float hueShift) {return forHsbConverter(new HueShiftConverter(hueShift));}

//------------------------------------------------------------------------------

  public static ImageFilter forBrightness(double brightnessFactor) {return forHsbConverter(new BrightnessConverter(brightnessFactor));}

//------------------------------------------------------------------------------

  public static ImageFilter forSaturation(double saturationFactor) {return forHsbConverter(new SaturationConverter(saturationFactor));}

//------------------------------------------------------------------------------

  public static ImageFilter forContrast(double contrastFactor) {return forHsbConverter(new ContrastConverter(contrastFactor));}

//------------------------------------------------------------------------------

  public static ImageFilter forHueDependingSaturation(HueFunction function) {return forHsbConverter(new HueDependingSaturationFactorConverter(function));}

//------------------------------------------------------------------------------

  public static ImageFilter forHueDependingBrightness(HueFunction function) {return forHsbConverter(new HueDependingBrightnessConverter(function));}

//------------------------------------------------------------------------------

  public static ImageFilter forClosestPole(Attraction attraction, float... huePoles) {return forHsbConverter(new ClosestPoleHueConverter(attraction, huePoles));}

//------------------------------------------------------------------------------

  public static ImageFilter forCombinedPole(Attraction attraction, float... huePoles) {return forHsbConverter(new CombinedPoleHueConverter(attraction, huePoles));}

//------------------------------------------------------------------------------

  public static ImageFilter forSeamlessRepulsion(float repulsedHue, float suppressionHalfWidth, float repulsionStrength, float repulsionRange)
  {
    return new ComposedFilter(forHueDependingSaturation(new FangHueFunction(repulsedHue, suppressionHalfWidth)),
                              forCombinedPole(new LinearRepulsion(repulsionStrength, repulsionRange), repulsedHue));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forHuePosterizer(int amountOfBuckets, float startingHue) {return forHsbConverter(new HuePosterizerConverter(amountOfBuckets, startingHue));}

//------------------------------------------------------------------------------

  public static ImageFilter forSaturationPosterizer(int amountOfBuckets, boolean centerThem) {return forHsbConverter(new SaturationPosterizerConverter(amountOfBuckets, centerThem));}

//------------------------------------------------------------------------------

  public static ImageFilter forBrightnessPosterizer(int amountOfBuckets, boolean centerThem) {return forHsbConverter(new BrightnessPosterizerConverter(amountOfBuckets, centerThem));}

//------------------------------------------------------------------------------

  public static ImageFilter forHueReverser(float centralHue) {return forHsbConverter(new HueReverserConverter(centralHue));}

//------------------------------------------------------------------------------

  public static ImageFilter forFixedHue(float fixedHue) {return forHsbConverter(new FixedHueConverter(fixedHue));}

//------------------------------------------------------------------------------

  public static ImageFilter forBrightnessDependingHue(float lowestBrightnessHue, float highestBrightnessHue, double openFactor)
  {
    return forHsbConverter(new BrightnessDependingHueConverter(lowestBrightnessHue, highestBrightnessHue, new ComposedUnitFactor(openFactor)));
  }

//------------------------------------------------------------------------------

  private static ImageFilter forHsbConverter(HsbConverter hsbConverter) {return new ColourFilter(new HsbColourConverter(hsbConverter));}

//------------------------------------------------------------------------------

}
