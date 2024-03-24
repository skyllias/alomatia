
package org.skyllias.alomatia.filter.buffered.layered;

import java.awt.Color;
import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.ColourFilter;
import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.compose.ComposedFilter;
import org.skyllias.alomatia.filter.convolve.BlurFilterFactory;
import org.skyllias.alomatia.filter.convolve.EdgeDetectorFilterFactory;
import org.skyllias.alomatia.filter.operator.OperatorFilterFactory;
import org.skyllias.alomatia.filter.rgb.RgbFilterFactory;

/** Instantiator of filters that stack images to compose the result. */

public class LayeredFilterFactory
{
  private static final FilteredImageGenerator filteredImageGenerator = new FilteredImageGenerator();

  private static final ImageFilter blackAlphaFilter = new ColourFilter(new BlackAlphaConverter());

//==============================================================================

  public static ImageFilter forStreakRemover(int blurSize, Color target, int similarityFactor)
  {
    return fromOperations(fromFilter(BlurFilterFactory.forGaussian(blurSize)),
                          fromFilter(new ColourFilter(new SimilarColourTransparencyConverter(target, similarityFactor))));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forHeterogeneousBlur(int blurSize, float relativeRadius)
  {
    ImageFilter radialBlurFilter = fromSingleOperation(new RadialAlphaOperation(relativeRadius));
    return fromOperations(new NoOperation(),
                          fromFilter(new ComposedFilter(BlurFilterFactory.forGaussian(blurSize),
                                                        radialBlurFilter)));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forBordersOnImage(int blurSize, float intensity)
  {
    return fromOperations(new NoOperation(),
                          fromFilter(new ComposedFilter(BlurFilterFactory.forGaussian(blurSize),
                                                        OperatorFilterFactory.forScharr(),
                                                        EdgeDetectorFilterFactory.forDrawLikeEdgeDetection(intensity, RgbFilterFactory.forMinChannelGreyScale()),
                                                        blackAlphaFilter)));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forBordersOnImageBlurFirst(int blurSize, int amountOfBuckets)
  {
    final float intensity = 1f;

    return fromOperations(new NoOperation(),
                          fromFilter(new ComposedFilter(BlurFilterFactory.forGaussian(blurSize),
                                                        RgbFilterFactory.forPosterizer(amountOfBuckets),
                                                        OperatorFilterFactory.forScharr(),
                                                        EdgeDetectorFilterFactory.forDrawLikeEdgeDetection(intensity, RgbFilterFactory.forMinChannelGreyScale()),
                                                        blackAlphaFilter)));
  }

  public static ImageFilter forBordersOnImagePosterFirst(int blurSize, int amountOfBuckets)
  {
    final float intensity = 1f;

    return fromOperations(new NoOperation(),
                          fromFilter(new ComposedFilter(RgbFilterFactory.forPosterizer(amountOfBuckets),
                                                        BlurFilterFactory.forGaussian(blurSize),
                                                        OperatorFilterFactory.forScharr(),
                                                        EdgeDetectorFilterFactory.forDrawLikeEdgeDetection(intensity, RgbFilterFactory.forMinChannelGreyScale()),
                                                        blackAlphaFilter)));
  }

//------------------------------------------------------------------------------

  private static BufferedImageOperation fromFilter(ImageFilter imageFilter)
  {
    return new ImageFilterApplyingOperation(filteredImageGenerator, imageFilter);
  }

//------------------------------------------------------------------------------

  private static ImageFilter fromOperations(BufferedImageOperation... layerOperations)
  {
    return fromSingleOperation(new LayeredBufferedImageOperation(layerOperations));
  }

//------------------------------------------------------------------------------

  private static ImageFilter fromSingleOperation(BufferedImageOperation operation)
  {
    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(operation));
  }

//------------------------------------------------------------------------------

}
