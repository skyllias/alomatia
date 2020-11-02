
package org.skyllias.alomatia.filter.buffered.surround;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

public class MinMaxChannelCalculatorTest
{
  @Test
  public void shouldReturnSameColourWhenOnlyOne()
  {
    Color anyColour = new Color(23, 155, 66);
    MinMaxChannelCalculator calculator = new MinMaxChannelCalculator(true, false, true);
    assertEquals("The min or max of a single colour should be the same colour",
                 anyColour, calculator.getColour(Arrays.asList(anyColour)));
  }

  @Test
  public void shouldReturnMaxRedChannelsWhenThree()
  {
    Collection<Color> inputColours = Arrays.asList(new Color(23, 155, 66),
                                                   new Color(200, 125, 0),
                                                   new Color(140, 15, 166));
    MinMaxChannelCalculator calculator = new MinMaxChannelCalculator(true, false, false);
    assertEquals("The max red should be returned",
                 new Color(200, 15, 0), calculator.getColour(inputColours));
  }

  @Test
  public void shouldReturnMaxGreenChannelsWhenThree()
  {
    Collection<Color> inputColours = Arrays.asList(new Color(23, 155, 66),
                                                   new Color(200, 125, 0),
                                                   new Color(140, 15, 166));
    MinMaxChannelCalculator calculator = new MinMaxChannelCalculator(false, true, false);
    assertEquals("The max green should be returned",
                 new Color(23, 155, 0), calculator.getColour(inputColours));
  }

  @Test
  public void shouldReturnMaxBlueChannelsWhenThree()
  {
    Collection<Color> inputColours = Arrays.asList(new Color(23, 155, 66),
                                                   new Color(200, 125, 0),
                                                   new Color(140, 15, 166));
    MinMaxChannelCalculator calculator = new MinMaxChannelCalculator(false, false, true);
    assertEquals("The max green should be returned",
                 new Color(23, 15, 166), calculator.getColour(inputColours));
  }
}
