
package org.skyllias.alomatia.filter.rgb.grey;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MinChannelGreyShadeProviderTest
{
  @Test
  public void shouldReturnAnyChannelWhenAllEqual()
  {
    assertEquals(120, new MinChannelGreyShadeProvider().getShade(120,  120,  120));
  }

  @Test
  public void shouldReturnLowestChannelWhenTwoEquallyLow()
  {
    assertEquals(10, new MinChannelGreyShadeProvider().getShade(220,  10,  10));
  }

  @Test
  public void shouldReturnLowestChannelWhenTwoEquallyHigh()
  {
    assertEquals(140, new MinChannelGreyShadeProvider().getShade(150,  150,  140));
  }

  @Test
  public void shouldReturnLowestChannelWhenAllDifferent()
  {
    assertEquals(0, new MinChannelGreyShadeProvider().getShade(0,  10,  210));
  }
}
