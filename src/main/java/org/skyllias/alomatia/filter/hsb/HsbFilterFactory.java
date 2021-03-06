
package org.skyllias.alomatia.filter.hsb;

import java.awt.Color;
import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.ColourFilter;
import org.skyllias.alomatia.filter.hsb.pole.Attraction;
import org.skyllias.alomatia.filter.hsb.pole.ClosestPoleHueConverter;
import org.skyllias.alomatia.filter.hsb.pole.CombinedPoleHueConverter;

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

  public static ImageFilter forClosestPole(Attraction attraction, Color... colourPoles) {return forHsbConverter(new ClosestPoleHueConverter(attraction, colourPoles));}

//------------------------------------------------------------------------------

  public static ImageFilter forCombinedPole(Attraction attraction, Color... colourPoles) {return forHsbConverter(new CombinedPoleHueConverter(attraction, colourPoles));}

//------------------------------------------------------------------------------

  public static ImageFilter forHuePosterizer(int amountOfBuckets, float startingHue) {return forHsbConverter(new HuePosterizerConverter(amountOfBuckets, startingHue));}

//------------------------------------------------------------------------------

  public static ImageFilter forSaturationPosterizer(int amountOfBuckets, boolean centerThem) {return forHsbConverter(new SaturationPosterizerConverter(amountOfBuckets, centerThem));}

//------------------------------------------------------------------------------

  public static ImageFilter forBrightnessPosterizer(int amountOfBuckets, boolean centerThem) {return forHsbConverter(new BrightnessPosterizerConverter(amountOfBuckets, centerThem));}

//------------------------------------------------------------------------------

  public static ImageFilter forHueReverser(float centralHue) {return forHsbConverter(new HueReverserConverter(centralHue));}

//------------------------------------------------------------------------------

  private static ImageFilter forHsbConverter(HsbConverter hsbConverter) {return new ColourFilter(new HsbColourConverter(hsbConverter));}

//------------------------------------------------------------------------------

}
