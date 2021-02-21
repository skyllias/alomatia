
package org.skyllias.alomatia.filter.buffered.surround;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

public class AverageChannelCalculatorTest
{
  @Test
  public void shouldReturnOriginalColourIfOnlyOne()
  {
    Collection<Color> surroundingColours = Collections.singleton(new Color(23, 105, 255));

    Color result = new AverageChannelCalculator().getColour(surroundingColours);

    assertEquals(new Color(23, 105, 255), result);
  }

  @Test
  public void shouldReturnExactAverage()
  {
    Collection<Color> surroundingColours = Arrays.asList(new Color(23, 105, 255),
                                                         new Color(123, 115, 253),
                                                         new Color(223, 110, 254));

    Color result = new AverageChannelCalculator().getColour(surroundingColours);

    assertEquals(new Color(123, 110, 254), result);
  }

  @Test
  public void shouldRoundIfNotExactAverage()
  {
    Collection<Color> surroundingColours = Arrays.asList(new Color(23, 105, 56),
                                                         new Color(23, 116, 153),
                                                         new Color(23, 111, 153),
                                                         new Color(24, 110, 253));

    Color result = new AverageChannelCalculator().getColour(surroundingColours);

    assertEquals(new Color(23, 110, 153), result);
  }

}
