
package org.skyllias.alomatia.filter.rgb.grey;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MaxChannelGreyShadeProviderTest
{
  @Test
  public void shouldReturnAnyChannelWhenAllEqual()
  {
    assertEquals(120, new MaxChannelGreyShadeProvider().getShade(120,  120,  120));
  }

  @Test
  public void shouldReturnHighestChannelWhenTwoEquallyLow()
  {
    assertEquals(220, new MaxChannelGreyShadeProvider().getShade(220,  10,  10));
  }

  @Test
  public void shouldReturnHighestChannelWhenTwoEquallyHigh()
  {
    assertEquals(150, new MaxChannelGreyShadeProvider().getShade(150,  150,  140));
  }

  @Test
  public void shouldReturnHighestChannelWhenAllDifferent()
  {
    assertEquals(210, new MaxChannelGreyShadeProvider().getShade(0,  10,  210));
  }
}
