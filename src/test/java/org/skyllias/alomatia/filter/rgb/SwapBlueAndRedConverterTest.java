package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class SwapBlueAndRedConverterTest
{
  private SwapBlueAndRedConverter converter = new SwapBlueAndRedConverter();

  @Test
  public void shouldNotModifyGrey()
  {
    assertEquals(new Color(150, 150, 150), converter.convertColor(new Color(150, 150, 150)));
  }

  @Test
  public void shouldTurnBlueToRed()
  {
    assertEquals(new Color(255, 0, 0), converter.convertColor(new Color(0, 0, 255)));
  }

  @Test
  public void shouldTurnRedToBlue()
  {
    assertEquals(new Color(0, 0, 255), converter.convertColor(new Color(255, 0, 0)));
  }

  @Test
  public void shouldKeepGreenChannel()
  {
    assertEquals(new Color(54, 209, 100), converter.convertColor(new Color(100, 209, 54)));
  }
}
