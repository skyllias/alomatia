
package org.skyllias.alomatia.filter.buffered.diffusion;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.convolve.BlurFilterFactory;

/** Provider of instances of diffusion filters. */

public class DiffusionFilterFactory
{
  private static final FilteredBufferedImageGenerator filteredImageGenerator = new FilteredBufferedImageGenerator(new FilteredImageGenerator());

//==============================================================================

  public static ImageFilter forHueDiffusion(int blurLength)
  {
    return new SingleFrameBufferedImageFilter(new HueDiffusionOp(BlurFilterFactory.forGaussian(blurLength), filteredImageGenerator));
  }

//------------------------------------------------------------------------------

}
