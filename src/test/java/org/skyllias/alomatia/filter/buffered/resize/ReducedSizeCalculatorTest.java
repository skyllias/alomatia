
package org.skyllias.alomatia.filter.buffered.resize;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;

import org.junit.Test;

public class ReducedSizeCalculatorTest
{
  @Test
  public void shouldReduceShortestDimensionToRequestedLength()
  {
    assertEquals(new Dimension(133, 100), new ReducedSizeCalculator(100).getReducedSize(1600, 1200));
  }

  @Test
  public void shouldReduceShortestDimensionWhenItIsTheWidth()
  {
    assertEquals(new Dimension(100, 133), new ReducedSizeCalculator(100).getReducedSize(1200, 1600));
  }

  @Test
  public void shouldRoundToTheNearestPixel()
  {
    assertEquals(new Dimension(5, 3), new ReducedSizeCalculator(3).getReducedSize(15, 10));
  }

  @Test
  public void shouldKeepAspectRatioOfElongatedImages()
  {
    assertEquals(new Dimension(1000, 25), new ReducedSizeCalculator(25).getReducedSize(4000, 100));
  }

  @Test
  public void shouldApplyMinimumWhenRequestedLengthIsTooSmall()
  {
    assertEquals(new Dimension(2, 2), new ReducedSizeCalculator(1).getReducedSize(300, 300));
  }

  @Test
  public void shouldNotEnlargeImagesShorterThanRequested()
  {
    assertEquals(new Dimension(300, 300), new ReducedSizeCalculator(400).getReducedSize(300, 300));
  }

  @Test
  public void shouldNotEnlargeImagesSmallerThanTheMinimum()
  {
    assertEquals(new Dimension(20, 1), new ReducedSizeCalculator(10).getReducedSize(20, 1));
  }

  @Test
  public void shouldKeepOriginalSizeWhenRequestedLengthMatchesShortestDimension()
  {
    assertEquals(new Dimension(7, 3), new ReducedSizeCalculator(3).getReducedSize(7, 3));
  }
}
