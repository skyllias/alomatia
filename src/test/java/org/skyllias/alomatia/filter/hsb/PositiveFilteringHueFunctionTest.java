
package org.skyllias.alomatia.filter.hsb;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class PositiveFilteringHueFunctionTest
{
  @Test
  public void shouldReturnOriginalValueWhenPositive()
  {
    HueFunction delegate = mock(HueFunction.class);
    when(delegate.getValue(any(Float.class))).thenReturn(4.0);

    PositiveFilteringHueFunction function = new PositiveFilteringHueFunction(delegate);
    assertEquals("The function should return the original value if positive", 4.0, function.getValue(0.2f), 0.000001);
  }

  @Test
  public void shouldReturnZeroWhenNegative()
  {
    HueFunction delegate = mock(HueFunction.class);
    when(delegate.getValue(any(Float.class))).thenReturn(-4.0);

    PositiveFilteringHueFunction function = new PositiveFilteringHueFunction(delegate);
    assertEquals("The function should return zero if negative", 0, function.getValue(0.2f), 0.000001);
  }
}
