
package org.skyllias.alomatia.filter.rgb;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.ColourFilter;

/** Instantiator of filters that play with the RGB components of colours. */

public class RgbFilterFactory
{
//==============================================================================

  public static ImageFilter forRedless() {return new ColourFilter(new RedlessConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forGreenless() {return new ColourFilter(new GreenlessConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forBlueless() {return new ColourFilter(new BluelessConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forRedChannelOnly() {return new ColourFilter(new RedChannelOnlyConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forGreenChannelOnly() {return new ColourFilter(new GreenChannelOnlyConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forBlueChannelOnly() {return new ColourFilter(new BlueChannelOnlyConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forRtoGtoBtoR() {return new ColourFilter(new RtoGtoBtoRConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forBtoGtoRtoB() {return new ColourFilter(new BtoGtoRtoBConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forRedAndGreenSwap() {return new ColourFilter(new SwapRedAndGreenConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forGreenAndBlueSwap() {return new ColourFilter(new SwapGreenAndBlueConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forBlueAndRedSwap() {return new ColourFilter(new SwapBlueAndRedConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forYellowEqualizer() {return new ColourFilter(new YellowEqualizerConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forMagentaEqualizer() {return new ColourFilter(new MagentaEqualizerConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forCyanEqualizer() {return new ColourFilter(new CyanEqualizerConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forNegative() {return new ColourFilter(new NegativeConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forEqualGreyScale() {return new ColourFilter(new WeighedGreyScaleConverter(new EqualChannelWeights()));}

//------------------------------------------------------------------------------

  public static ImageFilter forHumanSensitiveGreyScale() {return new ColourFilter(new WeighedGreyScaleConverter(new HumanSensitiveChannelWeights()));}

//------------------------------------------------------------------------------

  public static ImageFilter forVoid() {return new ColourFilter(new VoidConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forColourContrast(double contrastFactor) {return new ColourFilter(new ColourContrastConverter(contrastFactor));}

//------------------------------------------------------------------------------

  public static ImageFilter forPosterizer(int amountOfBuckets) {return new ColourFilter(new RgbPosterizerConverter(amountOfBuckets));}

//------------------------------------------------------------------------------

  public static ImageFilter forMaxOnly(int threshold) {return new ColourFilter(new MaxOnlyConverter(threshold));}

//------------------------------------------------------------------------------

}
