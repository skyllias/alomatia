
package org.skyllias.alomatia.filter.rgb;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class MaxOnlyConverterTest
{
  @Test
  public void shouldLeaveRedOnlyWhenDifferenceAboveThreshold()
  {
    MaxOnlyConverter converter = new MaxOnlyConverter(5);
    assertEquals(new Color(120, 0, 0),
                 converter.convertColour(new Color(120, 110, 15)));
  }

  @Test
  public void shouldLeaveGreenOnlyWhenDifferenceAboveThreshold()
  {
    MaxOnlyConverter converter = new MaxOnlyConverter(10);
    assertEquals(new Color(0, 230, 0),
                 converter.convertColour(new Color(220, 230, 215)));
  }

  @Test
  public void shouldLeaveBlueOnlyWhenDifferenceAboveThreshold()
  {
    MaxOnlyConverter converter = new MaxOnlyConverter(10);
    assertEquals(new Color(0, 0, 215),
                 converter.convertColour(new Color(195, 130, 215)));
  }

  @Test
  public void shouldLeaveRedAndGreenWhenDifferenceAboveThreshold()
  {
    MaxOnlyConverter converter = new MaxOnlyConverter(35);
    assertEquals(new Color(120, 90, 0),
                 converter.convertColour(new Color(120, 90, 85)));
  }

  @Test
  public void shouldLeaveGreenAndBlueWhenDifferenceAboveThreshold()
  {
    MaxOnlyConverter converter = new MaxOnlyConverter(10);
    assertEquals(new Color(0, 30, 25),
                 converter.convertColour(new Color(20, 30, 25)));
  }

  @Test
  public void shouldLeaveBlueAndRedWhenDifferenceAboveThreshold()
  {
    MaxOnlyConverter converter = new MaxOnlyConverter(100);
    assertEquals(new Color(100, 0, 175),
                 converter.convertColour(new Color(100, 70, 175)));
  }

  @Test
  public void shouldLeaveAllWhenDifferenceBelowThreshold()
  {
    MaxOnlyConverter converter = new MaxOnlyConverter(50);
    assertEquals(new Color(250, 230, 215),
                 converter.convertColour(new Color(250, 230, 215)));
  }

}
