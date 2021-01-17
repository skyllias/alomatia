
package org.skyllias.alomatia.filter.buffered.patch;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.ColourFilter;
import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.buffered.map.HueMap;
import org.skyllias.alomatia.filter.buffered.surround.SurroundingFilterFactory;
import org.skyllias.alomatia.filter.compose.ComposedFilter;
import org.skyllias.alomatia.filter.rgb.RgbPosterizerConverter;

/** Filter that posterizes colours and then changes their hue, emulating to a
 *  certain extend what Axe Colours does in his paintings. */

public class AxeColoursFilterFactory
{
//==============================================================================

  /** Creates a filter that applies a blur to soften edges, posterizes to
   *  rgbBuckets and applies hueMap to the resulting patches. */

  public static ImageFilter forAxeColours(int blurWidth, int rgbBuckets, HueMap hueMap)
  {
    return new ComposedFilter(SurroundingFilterFactory.forMedian(blurWidth),
                              new ColourFilter(new RgbPosterizerConverter(rgbBuckets, true)),                 // avoid pure blacks and whites
                              forHueMappedPatch(hueMap));
  }

//------------------------------------------------------------------------------

  private static ImageFilter forHueMappedPatch(HueMap hueMap)
  {
    HueMappedPatchBufferedImageOperation hueMappedPatchOperation = new HueMappedPatchBufferedImageOperation(new SimilarPatchesFinder(new ColourEquality()),
                                                                                                            hueMap);

    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(hueMappedPatchOperation));
  }

//------------------------------------------------------------------------------

}
