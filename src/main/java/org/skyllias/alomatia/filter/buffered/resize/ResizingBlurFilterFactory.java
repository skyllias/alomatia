
package org.skyllias.alomatia.filter.buffered.resize;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;

/** Instantiator of filters that blur by reducing the image resolution. */

public class ResizingBlurFilterFactory
{
//==============================================================================

  public static ImageFilter forShortestLength(int shortestLength)
  {
    ResizingBlurOperation resizingBlurOperation = new ResizingBlurOperation(new ReducedSizeCalculator(shortestLength));

    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(resizingBlurOperation));
  }

//------------------------------------------------------------------------------

}
