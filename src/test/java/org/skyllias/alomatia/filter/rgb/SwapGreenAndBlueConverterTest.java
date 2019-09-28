package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class SwapGreenAndBlueConverterTest
{
  private SwapGreenAndBlueConverter converter = new SwapGreenAndBlueConverter();

  @Test
  public void shouldNotModifyGrey()
  {
    assertEquals(new Color(150, 150, 150), converter.convertColour(new Color(150, 150, 150)));
  }

  @Test
  public void shouldTurnGreenToBlue()
  {
    assertEquals(new Color(0, 0, 255), converter.convertColour(new Color(0, 255, 0)));
  }

  @Test
  public void shouldTurnBlueToGreen()
  {
    assertEquals(new Color(0, 255, 0), converter.convertColour(new Color(0, 0, 255)));
  }

  @Test
  public void shouldKeepRedChannel()
  {
    assertEquals(new Color(54, 209, 100), converter.convertColour(new Color(54, 100, 209)));
  }
}
