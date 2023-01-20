
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.awt.image.ImageFilter;
import java.util.Random;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;

/** Instantiator of filters that work with the surroundings of each pixel. */

public class SurroundingFilterFactory
{
//==============================================================================

  public static ImageFilter forMedian(int boxSize)
  {
    return forCalculator(boxSize, new MedianChannelCalculator());
  }

//------------------------------------------------------------------------------

  public static ImageFilter forStrictBlackOrWhite(int boxSize, Color blackColor, Color whiteColor)
  {
    return forStrictBlackOrWhite(boxSize,  0.5f, blackColor, whiteColor);
  }

//------------------------------------------------------------------------------

  public static ImageFilter forStrictBlackOrWhite(int boxSize, float threshold, Color blackColor, Color whiteColor)
  {
    return forCalculator(boxSize, new LightCalculator(new StrictBlackOrWhiteSelector(threshold, blackColor, whiteColor)));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forProbabilisticBlackOrWhite(int boxSize)
  {
    return forCalculator(boxSize, new LightCalculator(new ProbabilisticBlackOrWhiteSelector(new Random())));
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
