
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.util.Random;
import java.util.function.Function;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.buffered.dynamic.DynamicFilterOperation;
import org.skyllias.alomatia.filter.compose.ComposedFilter;
import org.skyllias.alomatia.filter.convolve.BlurFilterFactory;

/** Instantiator of filters that work with the surroundings of each pixel. */

public class SurroundingFilterFactory
{
  private static final LightMeter lightMeter                                 = new LightMeter();
  private static final DynamicThresholdCalculator dynamicThresholdCalculator = new DynamicThresholdCalculator(lightMeter);

//==============================================================================

  public static ImageFilter forStrictBlackAndWhite(Color blackColor, Color whiteColor)
  {
    return forStrictBlackAndWhite(0, 0.5f, blackColor, whiteColor);
  }

//------------------------------------------------------------------------------

  public static ImageFilter forStrictBlackAndWhite(int blurSize, Color blackColor, Color whiteColor)
  {
    return forStrictBlackAndWhite(blurSize, 0.5f, blackColor, whiteColor);
  }

//------------------------------------------------------------------------------

  public static ImageFilter forStrictBlackAndWhite(int blurSize, float threshold,
                                                   Color blackColor, Color whiteColor)
  {
    return forPreBlurredBlackAndWhite(blurSize, new StrictBlackOrWhiteSelector(threshold, blackColor, whiteColor));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forDynamicThresholdBlackAndWhite(int blurSize,
                                                             Color blackColor, Color whiteColor)
  {
    Function<BufferedImage, Float> thresholdFunction = dynamicThresholdCalculator::getLight;
    Function<Float, ImageFilter> filterFunction      = threshold -> forPreBlurredBlackAndWhite(blurSize,
                                                                                               new StrictBlackOrWhiteSelector(threshold,
                                                                                                                              blackColor,
                                                                                                                              whiteColor));

    DynamicFilterOperation<Float> dynamicThresholdedOperation = new DynamicFilterOperation<>(thresholdFunction,
                                                                                             filterFunction);
    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(dynamicThresholdedOperation));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forProbabilisticBlackOrWhite(int blurSize)
  {
    return forPreBlurredBlackAndWhite(blurSize, new ProbabilisticBlackOrWhiteSelector(new Random()));
  }

//------------------------------------------------------------------------------

  private static ImageFilter forPreBlurredBlackAndWhite(int blurSize, BlackOrWhiteSelector blackOrWhiteSelector)
  {
    LightCalculator lightCalculator = new LightCalculator(lightMeter, blackOrWhiteSelector);
    HintlessBufferedImageOp imageOp = new HintlessBufferedImageOp(new SurroundingColoursOperation(0, lightCalculator));

    SingleFrameBufferedImageFilter blackOrWhiteFilter = new SingleFrameBufferedImageFilter(imageOp);
    if (blurSize == 0) return blackOrWhiteFilter;
    else               return new ComposedFilter(BlurFilterFactory.forGaussian(blurSize),
                                                 blackOrWhiteFilter);
  }

//------------------------------------------------------------------------------

  public static ImageFilter forMedian(int boxSize)
  {
    return forCalculator(boxSize, new MedianChannelCalculator());
  }

//------------------------------------------------------------------------------

  public static ImageFilter forMinMaxChannel(int boxSize, boolean maximumRed,
                                             boolean maximumGreen, boolean maximumBlue)
  {
    return forCalculator(boxSize, new MinMaxChannelCalculator(maximumRed, maximumGreen, maximumBlue));
  }

//------------------------------------------------------------------------------

  private static ImageFilter forCalculator(int boxSize, SurroundingColoursCalculator calculator)
  {
    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(new SurroundingColoursOperation(boxSize, calculator)));
  }

//------------------------------------------------------------------------------

}
