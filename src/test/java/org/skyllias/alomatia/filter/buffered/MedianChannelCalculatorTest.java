
package org.skyllias.alomatia.filter.buffered;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

public class MedianChannelCalculatorTest
{
  @Test
  public void shouldReturnSameColourWhenOnlyOne()
  {
    Color anyColour = new Color(23, 155, 66);
    MedianChannelCalculator calculator = new MedianChannelCalculator();
    assertEquals("The median of a single colour should be the same colour",
                 anyColour, calculator.getColour(Arrays.asList(anyColour)));
  }

  @Test
  public void shouldReturnMiddleChannelsWhenThree()
  {
    Collection<Color> inputColours = Arrays.asList(new Color(23, 155, 66),
                                                   new Color(200, 125, 0),
                                                   new Color(140, 15, 166));
    MedianChannelCalculator calculator = new MedianChannelCalculator();
    assertEquals("The median of three colours should take components from them",
                 new Color(140, 125, 66), calculator.getColour(inputColours));
  }

  @Test
  public void shouldReturnMiddleChannelsWhenNine()
  {
    Collection<Color> inputColours = Arrays.asList(new Color(23, 155, 66),
                                                   new Color(200, 125, 0),
                                                   new Color(140, 150, 166),
                                                   new Color(100, 15, 16),
                                                   new Color(40, 5, 200),
                                                   new Color(240, 105, 240),
                                                   new Color(14, 255, 210),
                                                   new Color(224, 250, 216),
                                                   new Color(30, 200, 150));
    MedianChannelCalculator calculator = new MedianChannelCalculator();
    assertEquals("The median of nine colours should take components from them",
                 new Color(100, 150, 166), calculator.getColour(inputColours));
  }

  @Test
  public void shouldTakeIntoAccountWhenDuplicateValues()
  {
    Collection<Color> inputColours = Arrays.asList(new Color(200, 155, 66),
                                                   new Color(200, 125, 0),
                                                   new Color(140, 125, 66));
    MedianChannelCalculator calculator = new MedianChannelCalculator();
    assertEquals("The median of repeated channels should take components from them",
                 new Color(200, 125, 66), calculator.getColour(inputColours));
  }

  @Test
  public void shouldBeBetweenWhenEvenAmount()
  {
    Collection<Color> inputColours = Arrays.asList(new Color(200, 201, 202),
                                                   new Color(100, 51, 2),
                                                   new Color(150, 101, 152),
                                                   new Color(50, 151, 102));
    MedianChannelCalculator calculator = new MedianChannelCalculator();
    Color result                       = calculator.getColour(inputColours);
    assertTrue("The median of even amounts should be between the two central values",
               result.getRed()   >= 100 && result.getRed()   <= 150 &&
               result.getGreen() >= 101 && result.getGreen() <= 151 &&
               result.getBlue()  >= 102 && result.getBlue()  <= 152);
  }
}
