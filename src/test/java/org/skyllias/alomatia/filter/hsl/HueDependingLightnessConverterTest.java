
package org.skyllias.alomatia.filter.hsl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.filter.hsb.function.HueFunction;

@RunWith(MockitoJUnitRunner.class)
public class HueDependingLightnessConverterTest
{
  private static final float ERROR_TOLERANCE = 0.001f;

  private static final float SOME_HUE = 0.1f;

  @Mock
  private HueFunction hueFunction;

  @Test
  public void shouldNotModifyHue()
  {
    HslConverter converter = new HueDependingLightnessConverter(hueFunction);
    assertEquals(SOME_HUE, converter.getNewHue(SOME_HUE, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotModifySaturation()
  {
    HslConverter converter = new HueDependingLightnessConverter(hueFunction);
    assertEquals(0.5f, converter.getNewSaturation(SOME_HUE, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotModifyLightnessWhenFunctionIsZero()
  {
    when(hueFunction.getValue(SOME_HUE)).thenReturn(0d);

    HslConverter converter = new HueDependingLightnessConverter(hueFunction);
    assertEquals(0.5f, converter.getNewLightness(SOME_HUE, 1f, 0.5f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotModifyLightnessWhenSaturationIsZero()
  {
    when(hueFunction.getValue(SOME_HUE)).thenReturn(1d);

    HslConverter converter = new HueDependingLightnessConverter(hueFunction);
    assertEquals(0.5f, converter.getNewLightness(SOME_HUE, 0f, 0.5f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotModifyLightnessWhenLightnessIsHighest()
  {
    when(hueFunction.getValue(SOME_HUE)).thenReturn(1d);

    HslConverter converter = new HueDependingLightnessConverter(hueFunction);
    assertEquals(1f, converter.getNewLightness(SOME_HUE, 1f, 1f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldIncreaseLightnessWhenFunctionIsPositive()
  {
    when(hueFunction.getValue(SOME_HUE)).thenReturn(1d);

    HslConverter converter = new HueDependingLightnessConverter(hueFunction);
    assertEquals(0.816f, converter.getNewLightness(SOME_HUE, 1f, 0.5f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldDecreaseLightnessWhenFunctionIsNegative()
  {
    when(hueFunction.getValue(SOME_HUE)).thenReturn(-1d);

    HslConverter converter = new HueDependingLightnessConverter(hueFunction);
    assertEquals(0.184f, converter.getNewLightness(SOME_HUE, 1f, 0.5f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyLightnessLessWhenChromaIsLower()
  {
    when(hueFunction.getValue(SOME_HUE)).thenReturn(1d);

    HslConverter converter = new HueDependingLightnessConverter(hueFunction);
    float lowChromaLightness = converter.getNewLightness(SOME_HUE, 0.5f, 0.5f);
    assertTrue(lowChromaLightness > 0.5f);
    assertTrue(lowChromaLightness < 0.816f);
  }

}
