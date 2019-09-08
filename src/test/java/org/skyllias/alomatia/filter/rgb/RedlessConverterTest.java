
package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class RedlessConverterTest
{
  private RedlessConverter converter = new RedlessConverter();

  @Test
  public void shouldConvertWhite()
  {
    assertEquals(new Color(0, 255, 255), converter.convertColor(new Color(255, 255, 255)));
  }

  @Test
  public void shouldConvertBlack()
  {
    assertEquals(new Color(0, 0, 0), converter.convertColor(new Color(0, 0, 0)));
  }

  @Test
  public void shouldConvertAny()
  {
    assertEquals(new Color(0, 80, 201), converter.convertColor(new Color(132, 80, 201)));
  }

}