
package org.skyllias.alomatia.filter.hsb.function;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CosineHueFunctionTest
{
  @Test
  public void shouldReturnMaxWhenHueInCentre()
  {
    CosineHueFunction function = new CosineHueFunction(0.2f, 0.7, -0.7);
    assertEquals("The function should return the max for hue in the centre", 0.7, function.getValue(0.2f), 0.000001);
  }

  @Test
  public void shouldReturnMinWhenHueOppositeToCentre()
  {
    CosineHueFunction function = new CosineHueFunction(0.1f, 0.7, 0.3);
    assertEquals("The function should return the min for hue opposite to the centre", 0.3, function.getValue(0.6f), 0.000001);
  }

  @Test
  public void shouldReturnTheMiddleWhenHueHalfWayFromCentre()
  {
    CosineHueFunction function = new CosineHueFunction(0.9f, 1, 0);
    assertEquals("The function should return the middle for hue half way from the centre", 0.5, function.getValue(0.65f), 0.000001);
  }

  @Test
  public void shouldReturnTheMiddleWhenHueHalfWayToCentre()
  {
    CosineHueFunction function = new CosineHueFunction(0.9f, 1, 0);
    assertEquals("The function should return the middle for hue half way from the centre", 0.5, function.getValue(0.15f), 0.000001);
  }
}
