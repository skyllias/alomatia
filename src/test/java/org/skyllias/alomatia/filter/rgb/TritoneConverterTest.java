
package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class TritoneConverterTest
{
  @Test
  public void shouldTakeDarkestColourWhenBrightnessIsZero()
  {
    TritoneConverter converter = new TritoneConverter(new Color(10, 0, 10),
                                                      new Color(110, 100, 100),
                                                      new Color(210, 210, 220));
    assertEquals(new Color(10, 0, 10),
                 converter.convertColour(new Color(0, 0, 0)));
  }

  @Test
  public void shouldTakeBrightestColourWhenBrightnessIsOne()
  {
    TritoneConverter converter = new TritoneConverter(new Color(0, 10, 10),
                                                      new Color(100, 100, 110),
                                                      new Color(210, 220, 210));
    assertEquals(new Color(210, 220, 210),
                 converter.convertColour(new Color(0, 255, 0)));
  }

  @Test
  public void shouldTakeMidColourWhenBrightnessIsHalf()
  {
    TritoneConverter converter = new TritoneConverter(new Color(10, 10, 0),
                                                      new Color(100, 110, 100),
                                                      new Color(220, 210, 210));
    assertEquals(new Color(100, 110, 100),
                 converter.convertColour(new Color(127, 127, 127)));
  }

  @Test
  public void shouldInterpolateColourWhenBrightnessIsLow()
  {
    TritoneConverter converter = new TritoneConverter(new Color(10, 0, 10),
                                                      new Color(100, 90, 110),
                                                      new Color(210, 210, 210));
    assertEquals(new Color(55, 45, 60),
                 converter.convertColour(new Color(Color.HSBtoRGB(0.2f, 0.5f, 0.25f))));
  }

  @Test
  public void shouldInterpolateColourWhenBrightnessIsHigh()
  {
    TritoneConverter converter = new TritoneConverter(new Color(10, 0, 10),
                                                      new Color(100, 90, 110),
                                                      new Color(200, 210, 220));
    assertEquals(new Color(150, 150, 165),
                 converter.convertColour(new Color(Color.HSBtoRGB(0.2f, 0.5f, 0.75f))));
  }

}
