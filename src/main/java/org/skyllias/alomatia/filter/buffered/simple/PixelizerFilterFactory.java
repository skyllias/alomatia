
package org.skyllias.alomatia.filter.buffered.simple;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;

/** Instantiator of filters that pixelize. */

public class PixelizerFilterFactory
{
//==============================================================================

  public static ImageFilter forPixelSize(int pixelSize)
  {
    PixelizerOperation pixelizerOperation = new PixelizerOperation(pixelSize);

    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(pixelizerOperation));
  }

//------------------------------------------------------------------------------

}
