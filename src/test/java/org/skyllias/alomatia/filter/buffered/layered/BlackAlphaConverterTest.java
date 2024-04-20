
package org.skyllias.alomatia.filter.buffered.layered;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class BlackAlphaConverterTest
{
  private BlackAlphaConverter converter = new BlackAlphaConverter();

  @Test
  public void shouldConvertDark()
  {
    assertEquals(new Color(25, 25, 25, 255),
                 converter.convertColour(new Color(25, 25, 25)));
  }

  @Test
  public void shouldConvertLight()
  {
    assertEquals(new Color(200, 200, 200, 0),
                 converter.convertColour(new Color(200, 200, 200)));
  }

  @Test
  public void shouldConvertMedium()
  {
    assertEquals(new Color(100, 100, 100, 55),
                 converter.convertColour(new Color(100, 100, 100)));
  }

}
