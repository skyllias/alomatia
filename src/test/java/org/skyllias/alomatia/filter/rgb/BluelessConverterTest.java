
package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class BluelessConverterTest
{
  private BluelessConverter converter = new BluelessConverter();

  @Test
  public void shouldConvertWhite()
  {
    assertEquals(new Color(255, 255, 0), converter.convertColor(new Color(255, 255, 255)));
  }

  @Test
  public void shouldConvertBlack()
  {
    assertEquals(new Color(0, 0, 0), converter.convertColor(new Color(0, 0, 0)));
  }

  @Test
  public void shouldConvertAny()
  {
    assertEquals(new Color(132, 80, 0), converter.convertColor(new Color(132, 80, 201)));
  }

}
