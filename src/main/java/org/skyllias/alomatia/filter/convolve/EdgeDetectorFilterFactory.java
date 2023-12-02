
package org.skyllias.alomatia.filter.convolve;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.compose.ComposedFilter;
import org.skyllias.alomatia.filter.rgb.RgbFilterFactory;

/** Factory of some filters based on edge detection. */

public class EdgeDetectorFilterFactory
{
  public static ImageFilter forStandardEdgeDetection()
  {
    EdgeConvolvingComposedFilter edgeConvolvingComposedFilter = new EdgeConvolvingComposedFilter(new EdgeDetectingKernelDataFactory());
    return new ComposedFilter(edgeConvolvingComposedFilter, RgbFilterFactory.forMaxChannelGreyScale());
  }

  public static ImageFilter forDrawLikeEdgeDetection(float intensity,
                                                     ImageFilter greyFilter)
  {
    ImageFilter edgeDetectorFilter = new EdgeConvolvingComposedFilter(new EdgeDetectingKernelDataFactory(), new WhiteStainKernelDataFactory(intensity));
    ImageFilter negativeFilter     = RgbFilterFactory.forNegative();

    return new ComposedFilter(edgeDetectorFilter, negativeFilter, greyFilter);
  }

}
