
package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class MagentaEqualizerConverterTest
{
  private MagentaEqualizerConverter converter = new MagentaEqualizerConverter();

  @Test
  public void shouldKeepGreen()
  {
    assertEquals(new Color(100, 150, 100), converter.convertColour(new Color(100, 150, 100)));
  }

  @Test
  public void shouldAverageHigherBlue()
  {
    assertEquals(new Color(150, 50, 150), converter.convertColour(new Color(100, 50, 200)));
  }

  @Test
  public void shouldAverageHigherRed()
  {
    assertEquals(new Color(150, 250, 150), converter.convertColour(new Color(200, 250, 100)));
  }

}
