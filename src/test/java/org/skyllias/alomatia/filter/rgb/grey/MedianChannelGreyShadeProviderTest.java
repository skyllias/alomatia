
package org.skyllias.alomatia.filter.rgb.grey;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MedianChannelGreyShadeProviderTest
{
  @Test
  public void shouldReturnAnyChannelWhenAllEqual()
  {
    assertEquals(120, new MedianChannelGreyShadeProvider().getShade(120,  120,  120));
  }

  @Test
  public void shouldReturnLowestChannelWhenTwoEquallyLow()
  {
    assertEquals(10, new MedianChannelGreyShadeProvider().getShade(220,  10,  10));
  }

  @Test
  public void shouldReturnHighestChannelWhenTwoEquallyHigh()
  {
    assertEquals(150, new MedianChannelGreyShadeProvider().getShade(150,  150,  140));
  }

  @Test
  public void shouldReturnMiddleChannelWhenAllDifferent()
  {
    assertEquals(10, new MedianChannelGreyShadeProvider().getShade(0,  10,  210));
  }
}
