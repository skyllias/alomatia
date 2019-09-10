
package org.skyllias.alomatia.filter.rgb;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.ColorFilter;

/** Instantiator of filters that play with the RGB components of colours. */

public class RgbFilterFactory
{
//==============================================================================

  public static ImageFilter forRedless() {return new ColorFilter(new RedlessConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forGreenless() {return new ColorFilter(new GreenlessConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forBlueless() {return new ColorFilter(new BluelessConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forRedChannelOnly() {return new ColorFilter(new RedChannelOnlyConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forGreenChannelOnly() {return new ColorFilter(new GreenChannelOnlyConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forBlueChannelOnly() {return new ColorFilter(new BlueChannelOnlyConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forRtoGtoBtoR() {return new ColorFilter(new RtoGtoBtoRConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forBtoGtoRtoB() {return new ColorFilter(new BtoGtoRtoBConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forRedAndGreenSwap() {return new ColorFilter(new SwapRedAndGreenConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forGreenAndBlueSwap() {return new ColorFilter(new SwapGreenAndBlueConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forBlueAndRedSwap() {return new ColorFilter(new SwapBlueAndRedConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forNegative() {return new ColorFilter(new NegativeConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forEqualGreyScale() {return new ColorFilter(new WeighedGreyScaleConverter(new EqualChannelWeights()));}

//------------------------------------------------------------------------------

  public static ImageFilter forHumanSensitiveGreyScale() {return new ColorFilter(new WeighedGreyScaleConverter(new HumanSensitiveChannelWeights()));}

//------------------------------------------------------------------------------

  public static ImageFilter forVoid() {return new ColorFilter(new VoidConverter());}

//------------------------------------------------------------------------------

  public static ImageFilter forColourContrast(double contrastFactor) {return new ColorFilter(new ColourContrastConverter(contrastFactor));}

//------------------------------------------------------------------------------

  public static ImageFilter forPosterizer(int amountOfBuckets) {return new ColorFilter(new RgbPosterizer(amountOfBuckets));}

//------------------------------------------------------------------------------

}
