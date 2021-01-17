
package org.skyllias.alomatia.filter.buffered.hdr.naive;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;
import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.compose.ComposedFilter;
import org.skyllias.alomatia.filter.convolve.BlurFilterFactory;

/** Provider of instances of HDR filters, composed by a blurring naïve HDR
 *  filter followed by a sharpening filter. */

public class NaiveHdrFilterFactory
{
  private static final FilteredBufferedImageGenerator filteredImageGenerator = new FilteredBufferedImageGenerator(new FilteredImageGenerator());

//==============================================================================

  public static ImageFilter forSmallBlur(int blurLength)
  {
    NaiveHdrOperation naiveHdrOperation = new NaiveHdrOperation(BlurFilterFactory.forParaboloid(blurLength),
                                                                filteredImageGenerator);

    return new ComposedFilter(new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(naiveHdrOperation)),
                              BlurFilterFactory.forSharpening());
  }

//------------------------------------------------------------------------------

}
