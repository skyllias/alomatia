
package org.skyllias.alomatia.filter.compose;

import org.skyllias.alomatia.filter.ColorFilter;
import org.skyllias.alomatia.filter.buffered.MedianChannelCalculator;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.buffered.SurroundingColoursOp;
import org.skyllias.alomatia.filter.buffered.map.HueMap;
import org.skyllias.alomatia.filter.buffered.patch.ColourEquality;
import org.skyllias.alomatia.filter.buffered.patch.HueMappedPatchBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.patch.SimilarPatchesFinder;
import org.skyllias.alomatia.filter.rgb.RgbPosterizer;

/** Posterizes colours and then changes their hue, emulating to a certain extend
 *  what Axe Colours does in his paintings. */

public class AxeColoursFilter extends ComposedFilter
{
//==============================================================================

  /** Creates a filter that applies a blur to soften edges, posterizes to
   *  rgbBuckets and applies hueMap to the resulting patches. */

  public AxeColoursFilter(int blurWidth, int rgbBuckets, HueMap hueMap)
  {
    super(new SingleFrameBufferedImageFilter(new SurroundingColoursOp(blurWidth, new MedianChannelCalculator())),
          new ColorFilter(new RgbPosterizer(rgbBuckets, true)),                 // avoid pure blacks and whites
          new SingleFrameBufferedImageFilter(new HueMappedPatchBufferedImageOp(new SimilarPatchesFinder(new ColourEquality()),
                                                                               hueMap)));
  }

//==============================================================================

}