
package org.skyllias.alomatia.filter.hsb;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.filter.factor.ComposedUnitFactor;

@RunWith(MockitoJUnitRunner.class)
public class BrightnessDependentHueConverterTest
{
  private static final float ERROR_TOLERANCE = 0.001f;

  @Mock
  private ComposedUnitFactor composedUnitFactor;

  @Test
  public void shouldNotModifySaturation()
  {
    HsbConverter converter = new BrightnessDependingHueConverter(0.25f, 0.5f, composedUnitFactor);
    assertEquals(0.5f, converter.getNewSaturation(0.1f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotModifyBrightness()
  {
    HsbConverter converter = new BrightnessDependingHueConverter(0.25f, 0.5f, composedUnitFactor);
    assertEquals(0.75f, converter.getNewBrightness(0.1f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToLowerLimitWhenBrightnessIsZero()
  {
    when(composedUnitFactor.apply(0f)).thenReturn(0f);

    HsbConverter converter = new BrightnessDependingHueConverter(0.25f, 0.75f, composedUnitFactor);
    assertEquals(0.25f, converter.getNewHue(0.1f, 0.5f, 0f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToUpperLimitWhenBrightnessIsOne()
  {
    when(composedUnitFactor.apply(1f)).thenReturn(1f);

    HsbConverter converter = new BrightnessDependingHueConverter(0.33f, 0.66f, composedUnitFactor);
    assertEquals(0.66f, converter.getNewHue(0.1f, 0.5f, 1f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToMediumLimitWhenBrightnessIsMedium()
  {
    when(composedUnitFactor.apply(0.4f)).thenReturn(0.6f);

    HsbConverter converter = new BrightnessDependingHueConverter(0f, 0.5f, composedUnitFactor);
    assertEquals(0.3f, converter.getNewHue(0.1f, 0.5f, 0.4f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToLowerLimitWhenBrightnessIsZeroAndLimitsAreInverted()
  {
    when(composedUnitFactor.apply(0f)).thenReturn(0f);

    HsbConverter converter = new BrightnessDependingHueConverter(0.5f, 0f, composedUnitFactor);
    assertEquals(0.5f, converter.getNewHue(0.1f, 0.5f, 0f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToUpperLimitWhenBrightnessIsOneAndLimitsAreInverted()
  {
    when(composedUnitFactor.apply(1f)).thenReturn(1f);

    HsbConverter converter = new BrightnessDependingHueConverter(0.8f, 0.6f, composedUnitFactor);
    assertEquals(0.6f, converter.getNewHue(0.1f, 0.5f, 1f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHueToMediumLimitWhenBrightnessIsMediumAndLimitsAreInverted()
  {
    when(composedUnitFactor.apply(0.6f)).thenReturn(0.4f);

    HsbConverter converter = new BrightnessDependingHueConverter(1f, 0.5f, composedUnitFactor);
    assertEquals(0.8f, converter.getNewHue(0.1f, 0.5f, 0.6f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldWorkTheSameWithLimitsOutsideUnit()
  {
    when(composedUnitFactor.apply(0.5f)).thenReturn(0.5f);

    HsbConverter converter = new BrightnessDependingHueConverter(-0.5f, 2.5f, composedUnitFactor);
    assertEquals(1f, converter.getNewHue(0.1f, 0.5f, 0.5f), ERROR_TOLERANCE);
  }

}
