
package org.skyllias.alomatia.filter.hsl;

import static org.junit.Assert.fail;

import java.awt.Color;

import org.junit.Test;
import org.skyllias.alomatia.filter.ColourConverter;
import org.skyllias.alomatia.filter.factor.ComposedUnitFactor;
import org.skyllias.alomatia.filter.hsb.HsbColourConverter;
import org.skyllias.alomatia.filter.hsb.HsbConverter;
import org.skyllias.alomatia.filter.hsb.function.FlatStepHueFunction;
import org.skyllias.alomatia.filter.hsb.function.HueFunction;

public class HslVersusHsbDifferenceTest
{
  @Test
  public void shouldDifferFromHsbWhenSaturationIsIncreased()
  {
    assertDifferentResults(new SaturationConverter(1),
                           new org.skyllias.alomatia.filter.hsb.SaturationConverter(1));
  }

  @Test
  public void shouldDifferFromHsbWhenSaturationIsDecreased()
  {
    assertDifferentResults(new SaturationConverter(-1),
                           new org.skyllias.alomatia.filter.hsb.SaturationConverter(-1));
  }

  @Test
  public void shouldDifferFromHsbWhenLightnessIsModified()
  {
    assertDifferentResults(new LightnessConverter(1),
                           new org.skyllias.alomatia.filter.hsb.BrightnessConverter(1));
  }

  @Test
  public void shouldDifferFromHsbWhenContrastIsModified()
  {
    assertDifferentResults(new ContrastConverter(1),
                           new org.skyllias.alomatia.filter.hsb.ContrastConverter(1));
  }

  @Test
  public void shouldDifferFromHsbWhenSaturationIsPosterized()
  {
    assertDifferentResults(new SaturationPosterizerConverter(2, false),
                           new org.skyllias.alomatia.filter.hsb.SaturationPosterizerConverter(2, false));
  }

  @Test
  public void shouldDifferFromHsbWhenLightnessIsPosterized()
  {
    assertDifferentResults(new LightnessPosterizerConverter(2, false),
                           new org.skyllias.alomatia.filter.hsb.BrightnessPosterizerConverter(2, false));
  }

  @Test
  public void shouldDifferFromHsbWhenSaturationDependsOnHue()
  {
    HueFunction hueFunction = new FlatStepHueFunction(0.5, 0.95f, 0.05f);

    assertDifferentResults(new HueDependingSaturationFactorConverter(hueFunction),
                           new org.skyllias.alomatia.filter.hsb.HueDependingSaturationFactorConverter(hueFunction));
  }

  @Test
  public void shouldDifferFromHsbWhenLightnessDependsOnHue()
  {
    HueFunction hueFunction = new FlatStepHueFunction(0.5, 0.95f, 0.05f);

    assertDifferentResults(new HueDependingLightnessConverter(hueFunction),
                           new org.skyllias.alomatia.filter.hsb.HueDependingBrightnessConverter(hueFunction));
  }

  @Test
  public void shouldDifferFromHsbWhenHueDependsOnLightness()
  {
    assertDifferentResults(new LightnessDependingHueConverter(0.33f, 1f, new ComposedUnitFactor(0)),
                           new org.skyllias.alomatia.filter.hsb.BrightnessDependingHueConverter(0.33f, 1f, new ComposedUnitFactor(0)));
  }

  /* Some colours are fixed points of both colour-spaces (fully saturated
   * colours, greys, black and white), so it is enough that any of the sample
   * colours is transformed differently. */

  private void assertDifferentResults(HslConverter hslConverter, HsbConverter hsbConverter)
  {
    final Color[] SOME_COLOURS = {new Color(180, 40, 40), new Color(10, 20, 30),
                                  new Color(200, 180, 240), new Color(255, 128, 0)};

    ColourConverter hslColourConverter = new HslColourConverter(hslConverter);
    ColourConverter hsbColourConverter = new HsbColourConverter(hsbConverter);

    int i = 0;
    boolean different = false;
    while (i < SOME_COLOURS.length && !different)
    {
      Color colour = SOME_COLOURS[i];
      Color hslResult = hslColourConverter.convertColour(colour);
      Color hsbResult = hsbColourConverter.convertColour(colour);
      if (!hslResult.equals(hsbResult)) different = true;

      i++;
    }

    if (!different) fail("Same results as the HSB counterpart for all the sample colours");
  }

}
