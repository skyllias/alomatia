
package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class CyanEqualizerConverterTest
{
  private CyanEqualizerConverter converter = new CyanEqualizerConverter();

  @Test
  public void shouldKeepRed()
  {
    assertEquals(new Color(150, 100, 100), converter.convertColour(new Color(150, 100, 100)));
  }

  @Test
  public void shouldAverageHigherGreen()
  {
    assertEquals(new Color(50, 150, 150), converter.convertColour(new Color(50, 200, 100)));
  }

  @Test
  public void shouldAverageHigherBlue()
  {
    assertEquals(new Color(250, 150, 150), converter.convertColour(new Color(250, 100, 200)));
  }

}
