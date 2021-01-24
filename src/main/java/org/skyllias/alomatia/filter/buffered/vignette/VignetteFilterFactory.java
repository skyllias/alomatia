
package org.skyllias.alomatia.filter.buffered.vignette;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;

/** Helper class to get the most usual vignette filters. */

public class VignetteFilterFactory
{
//==============================================================================

  public static ImageFilter forCross()
  {
    return getFilterForProfile(new CrossProfile());
  }

//------------------------------------------------------------------------------

  public static ImageFilter forEdges()
  {
    return getFilterForProfile(new EdgesProfile());
  }

//------------------------------------------------------------------------------

  public static ImageFilter forRound()
  {
    return getFilterForProfile(new RoundProfile());
  }

//------------------------------------------------------------------------------

  /** Returns a filter that applies a vignette effect with profile. */

  private static ImageFilter getFilterForProfile(VignetteProfile profile)
  {
    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(new ResizingVignetteOperation(profile)));
  }

//------------------------------------------------------------------------------

}
