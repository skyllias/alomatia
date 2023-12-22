
package org.skyllias.alomatia.filter.buffered.layered;

import java.awt.Color;
import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.ColourFilter;
import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.convolve.BlurFilterFactory;

/** Instantiator of filters that paint images with some dye. */

public class LayeredFilterFactory
{
  private static final FilteredImageGenerator filteredImageGenerator = new FilteredImageGenerator();

//==============================================================================

  public static ImageFilter forStreakRemover(int blurSize, Color target, int similarityFactor)
  {
    return fromOperations(fromFilter(BlurFilterFactory.forGaussian(blurSize)),
                          fromFilter(new ColourFilter(new SimilarColourTransparencyConverter(target, similarityFactor))));
  }

//------------------------------------------------------------------------------

  private static BufferedImageOperation fromFilter(ImageFilter imageFilter)
  {
    return new ImageFilterApplyingOperation(filteredImageGenerator, imageFilter);
  }

//------------------------------------------------------------------------------

  private static ImageFilter fromOperations(BufferedImageOperation... layerOperations)
  {
    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(new LayeredBufferedImageOperation(layerOperations)));
  }

//------------------------------------------------------------------------------

}
