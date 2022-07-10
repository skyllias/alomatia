
package org.skyllias.alomatia.filter.hsb.function;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FangHueFunctionTest
{
  @Test
  public void shouldReturnZeroWhenHueSmallerThanLowerLimit()
  {
    FangHueFunction function = new FangHueFunction(0.5f, 0.2f);
    assertEquals(0, function.getValue(0.1f), 0.000001);
  }

  @Test
  public void shouldReturnZeroWhenHueSmallerThanLowerLimitAndGreaterThanUpperCircularLimit()
  {
    FangHueFunction function = new FangHueFunction(0.9f, 0.2f);
    assertEquals(0, function.getValue(0.2f), 0.000001);
  }

  @Test
  public void shouldReturnNegativeValueWhenHueGraterThanLowerLimit()
  {
    FangHueFunction function = new FangHueFunction(0.5f, 0.2f);
    assertEquals(-1, function.getValue(0.4f), 0.000001);
  }

  @Test
  public void shouldReturnNegativeValueWhenHueSmallerThanUpperCircularLimit()
  {
    FangHueFunction function = new FangHueFunction(0.9f, 0.2f);
    assertEquals(-1, function.getValue(0), 0.000001);
  }

  @Test
  public void shouldReturnLowestValueWhenHueEqualToCenter()
  {
    FangHueFunction function = new FangHueFunction(0.5f, 0.2f);
    assertEquals(-2, function.getValue(0.5f), 0.000001);
  }

  @Test
  public void shouldReturnZeroWhenHueGreaterThanUpperLimit()
  {
    FangHueFunction function = new FangHueFunction(0.5f, 0.2f);
    assertEquals(0, function.getValue(0.8f), 0.000001);
  }

  @Test
  public void shouldReturnZeroWhenHueGreaterThanUpperLimitAndSmallerThanLowerCircularLimit()
  {
    FangHueFunction function = new FangHueFunction(0.1f, 0.2f);
    assertEquals(0, function.getValue(0.8f), 0.000001);
  }

  @Test
  public void shouldReturnNegativeValueWhenHueSmallerThanUpperLimit()
  {
    FangHueFunction function = new FangHueFunction(0.5f, 0.2f);
    assertEquals(-1, function.getValue(0.6f), 0.000001);
  }

  @Test
  public void shouldReturnNegativeValueWhenHueGreaterThanLowerCircularLimit()
  {
    FangHueFunction function = new FangHueFunction(0.1f, 0.2f);
    assertEquals(-1, function.getValue(0), 0.000001);
  }
}
