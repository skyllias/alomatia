
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.awt.image.ImageFilter;
import java.util.Random;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.compose.ComposedFilter;
import org.skyllias.alomatia.filter.convolve.BlurFilterFactory;

/** Instantiator of filters that work with the surroundings of each pixel. */

public class SurroundingFilterFactory
{
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

  public static ImageFilter forProbabilisticBlackOrWhite(int blurSize)
  {
    return forPreBlurredBlackAndWhite(blurSize, new ProbabilisticBlackOrWhiteSelector(new Random()));
  }

//------------------------------------------------------------------------------

  private static ImageFilter forPreBlurredBlackAndWhite(int blurSize, BlackOrWhiteSelector blackOrWhiteSelector)
  {
    LightCalculator lightCalculator = new LightCalculator(blackOrWhiteSelector);
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
