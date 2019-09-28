package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class SwapRedAndGreenConverterTest
{
  private SwapRedAndGreenConverter converter = new SwapRedAndGreenConverter();

  @Test
  public void shouldNotModifyGrey()
  {
    assertEquals(new Color(150, 150, 150), converter.convertColour(new Color(150, 150, 150)));
  }

  @Test
  public void shouldTurnRedToGreen()
  {
    assertEquals(new Color(0, 255, 0), converter.convertColour(new Color(255, 0, 0)));
  }

  @Test
  public void shouldTurnGreenToRed()
  {
    assertEquals(new Color(255, 0, 0), converter.convertColour(new Color(0, 255, 0)));
  }

  @Test
  public void shouldKeepBlueChannel()
  {
    assertEquals(new Color(54, 209, 100), converter.convertColour(new Color(209, 54, 100)));
  }
}
