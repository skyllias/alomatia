
package org.skyllias.alomatia.filter.buffered.spectrum;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;
import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;

/** Instantiator of filters that work with the spectrum of images. */

public class SpectrumFilterFactory
{
  private static final MostRelevantHueCalculator mostRelevantHueCalculator           = new MostRelevantHueCalculator();
  private static final FilteredBufferedImageGenerator filteredBufferedImageGenerator = new FilteredBufferedImageGenerator(new FilteredImageGenerator());
  private static final HueShiftFilterFactory hueShiftFilterFactory                   = new HueShiftFilterFactory();

//==============================================================================

  public static ImageFilter forDominantHue(float targetHue)
  {
    DominantHueShiftOperation dominantHueShiftOperation = new DominantHueShiftOperation(mostRelevantHueCalculator,
                                                                                        filteredBufferedImageGenerator,
                                                                                        hueShiftFilterFactory,
                                                                                        targetHue);
    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(dominantHueShiftOperation));
  }

//------------------------------------------------------------------------------

}
