
package org.skyllias.alomatia.filter.hsb.function;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class MultiplyingHueFactorTest
{
  @Test
  public void shouldReturnMultiplicationWhenPositive()
  {
    HueFunction delegate = mock(HueFunction.class);
    when(delegate.getValue(any(Float.class))).thenReturn(4.0);

    MultiplyingHueFactor function = new MultiplyingHueFactor(delegate, 2);
    assertEquals("The function should return the multiplication by the factor", 8.0, function.getValue(0.2f), 0.000001);
  }

  @Test
  public void shouldReturnMultiplicationWhenNegative()
  {
    HueFunction delegate = mock(HueFunction.class);
    when(delegate.getValue(any(Float.class))).thenReturn(-4.0);

    MultiplyingHueFactor function = new MultiplyingHueFactor(delegate, -0.5);
    assertEquals("The function should return the multiplication by the factor", 2.0, function.getValue(0.2f), 0.000001);
  }
}
