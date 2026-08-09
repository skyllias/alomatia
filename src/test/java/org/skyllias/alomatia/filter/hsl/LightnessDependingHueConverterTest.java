
package org.skyllias.alomatia.filter.hsl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.filter.factor.ComposedUnitFactor;

@RunWith(MockitoJUnitRunner.class)
public class LightnessDependingHueConverterTest
{
  private static final float ERROR_TOLERANCE = 0.001f;

  @Mock
  private ComposedUnitFactor composedUnitFactor;

  @Test
  public void shouldNotModifySaturation()
  {
    HslConverter converter = new LightnessDependingHueConverter(0.25f, 0.5f, composedUnitFactor);
    assertEquals(0.5f, converter.getNewSaturation(0.1f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotModifyLightness()
  {
    HslConverter converter = new LightnessDependingHueConverter(0.25f, 0.5f, composedUnitFactor);
    assertEquals(0.75f, converter.getNewLightness(0.1f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToLowerLimitWhenLightnessIsZero()
  {
    when(composedUnitFactor.apply(0f)).thenReturn(0f);

    HslConverter converter = new LightnessDependingHueConverter(0.25f, 0.75f, composedUnitFactor);
    assertEquals(0.25f, converter.getNewHue(0.1f, 0.5f, 0f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToUpperLimitWhenLightnessIsOne()
  {
    when(composedUnitFactor.apply(1f)).thenReturn(1f);

    HslConverter converter = new LightnessDependingHueConverter(0.33f, 0.66f, composedUnitFactor);
    assertEquals(0.66f, converter.getNewHue(0.1f, 0.5f, 1f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToMediumLimitWhenLightnessIsMedium()
  {
    when(composedUnitFactor.apply(0.4f)).thenReturn(0.6f);

    HslConverter converter = new LightnessDependingHueConverter(0f, 0.5f, composedUnitFactor);
    assertEquals(0.3f, converter.getNewHue(0.1f, 0.5f, 0.4f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToLowerLimitWhenLightnessIsZeroAndLimitsAreInverted()
  {
    when(composedUnitFactor.apply(0f)).thenReturn(0f);

    HslConverter converter = new LightnessDependingHueConverter(0.5f, 0f, composedUnitFactor);
    assertEquals(0.5f, converter.getNewHue(0.1f, 0.5f, 0f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToUpperLimitWhenLightnessIsOneAndLimitsAreInverted()
  {
    when(composedUnitFactor.apply(1f)).thenReturn(1f);

    HslConverter converter = new LightnessDependingHueConverter(0.8f, 0.6f, composedUnitFactor);
    assertEquals(0.6f, converter.getNewHue(0.1f, 0.5f, 1f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldWorkTheSameWithLimitsOutsideUnit()
  {
    when(composedUnitFactor.apply(0.5f)).thenReturn(0.5f);

    HslConverter converter = new LightnessDependingHueConverter(-0.5f, 2.5f, composedUnitFactor);
    assertEquals(1f, converter.getNewHue(0.1f, 0.5f, 0.5f), ERROR_TOLERANCE);
  }

}
