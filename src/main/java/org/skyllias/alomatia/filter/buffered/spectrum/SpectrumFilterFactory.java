
package org.skyllias.alomatia.filter.buffered.spectrum;

import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.util.function.Function;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.buffered.dynamic.DynamicFilterOperation;
import org.skyllias.alomatia.filter.hsb.factory.HsbFilterFactory;

/** Instantiator of filters that work with the spectrum of images. */

public class SpectrumFilterFactory
{
  private static final MostRelevantHueCalculator mostRelevantHueCalculator = new MostRelevantHueCalculator();

//==============================================================================

  public static ImageFilter forDominantHue(float targetHue)
  {
    Function<BufferedImage, Float> dominantHueShiftFunction = image -> targetHue - mostRelevantHueCalculator.getMostRelevantHue(image);
    Function<Float, ImageFilter> hueShiftFilterFunction     = shift -> HsbFilterFactory.forHueShift(shift);

    DynamicFilterOperation<Float> dominantHueShiftOperation = new DynamicFilterOperation<>(dominantHueShiftFunction,
                                                                                           hueShiftFilterFunction);
    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(dominantHueShiftOperation));
  }

//------------------------------------------------------------------------------

}
