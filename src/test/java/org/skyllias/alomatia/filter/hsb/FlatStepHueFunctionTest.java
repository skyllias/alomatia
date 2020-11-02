
package org.skyllias.alomatia.filter.hsb;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FlatStepHueFunctionTest
{
  @Test
  public void shouldReturnZeroWhenHueLtStepstartLtStepend()
  {
    FlatStepHueFunction function = new FlatStepHueFunction(10, 0.25f, 0.75f);
    assertEquals("The function should return zero for hue < step start < step end", 0, function.getValue(0.1f), 0.000001);
  }

  @Test
  public void shouldReturnHeightWhenStepstartLtHueLtStepend()
  {
    FlatStepHueFunction function = new FlatStepHueFunction(10, 0.25f, 0.75f);
    assertEquals("The function should return height for step start < hue < step end", 10, function.getValue(0.5f), 0.000001);
  }

  @Test
  public void shouldReturnZeroWhenStepstartLtStependLtHue()
  {
    FlatStepHueFunction function = new FlatStepHueFunction(10, 0.25f, 0.75f);
    assertEquals("The function should return zero for step start < step end < hue", 0, function.getValue(0.9f), 0.000001);
  }

  @Test
  public void shouldReturnZeroWhenStependLtHueLtStepstart()
  {
    FlatStepHueFunction function = new FlatStepHueFunction(10, 0.75f, 0.25f);
    assertEquals("The function should return zero for step end < hue < step start", 0, function.getValue(0.5f), 0.000001);
  }

  @Test
  public void shouldReturnHeightWhenHueLtStependLtStepstart()
  {
    FlatStepHueFunction function = new FlatStepHueFunction(10, 0.75f, 0.25f);
    assertEquals("The function should return height for hue < step end < step start", 10, function.getValue(0.1f), 0.000001);
  }

  @Test
  public void shouldReturnHeightWhenStependLtStepstartLtHue()
  {
    FlatStepHueFunction function = new FlatStepHueFunction(10, 0.75f, 0.25f);
    assertEquals("The function should return height for step end < step start < hue", 10, function.getValue(0.9f), 0.000001);
  }
}
