
package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class GreenlessConverterTest
{
  private GreenlessConverter converter = new GreenlessConverter();

  @Test
  public void shouldConvertWhite()
  {
    assertEquals(new Color(255, 0, 255), converter.convertColour(new Color(255, 255, 255)));
  }

  @Test
  public void shouldConvertBlack()
  {
    assertEquals(new Color(0, 0, 0), converter.convertColour(new Color(0, 0, 0)));
  }

  @Test
  public void shouldConvertAny()
  {
    assertEquals(new Color(132, 0, 201), converter.convertColour(new Color(132, 80, 201)));
  }

}
