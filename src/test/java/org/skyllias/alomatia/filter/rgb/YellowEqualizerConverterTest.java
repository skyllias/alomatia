
package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class YellowEqualizerConverterTest
{
  private YellowEqualizerConverter converter = new YellowEqualizerConverter();

  @Test
  public void shouldKeepBlue()
  {
    assertEquals(new Color(100, 100, 150), converter.convertColour(new Color(100, 100, 150)));
  }

  @Test
  public void shouldAverageHigherGreen()
  {
    assertEquals(new Color(150, 150, 50), converter.convertColour(new Color(100, 200, 50)));
  }

  @Test
  public void shouldAverageHigherRed()
  {
    assertEquals(new Color(150, 150, 250), converter.convertColour(new Color(200, 100, 250)));
  }

}
