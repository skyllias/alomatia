
package org.skyllias.alomatia.filter.buffered.simple;

import java.awt.Color;
import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;

/** Instantiator of filters that paint images with some dye. */

public class DyeFilterFactory
{
  private static final float DEFAULT_ALPHA = 0.2f;

//==============================================================================

  public static ImageFilter forDefaultAlpha(Color dye)
  {
    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(new DyeOperation(dye, DEFAULT_ALPHA)));
  }

//------------------------------------------------------------------------------

}
