
package org.skyllias.alomatia.filter.buffered.surround;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LightCalculatorTest
{
  @Mock
  private BlackOrWhiteSelector blackOrWhiteSelector;

  @InjectMocks
  private LightCalculator lightCalculator;

  @Before
  public void setUp()
  {
    when(blackOrWhiteSelector.chooseBlackOrWhite(0.25f)).thenReturn(Color.BLACK);
    when(blackOrWhiteSelector.chooseBlackOrWhite(0.75f)).thenReturn(Color.WHITE);
  }

  @Test
  public void shouldReturnBlackWhenDark()
  {
    Collection<Color> surroundingColours = Arrays.asList(new Color(255, 0, 0),
                                                         new Color(0, 0, 0),
                                                         new Color(0, 255, 0),
                                                         new Color(0, 0, 255));

    assertEquals(Color.BLACK, lightCalculator.getColour(surroundingColours));
  }

  @Test
  public void shouldReturnWhiteWhenLight()
  {
    Collection<Color> surroundingColours = Arrays.asList(new Color(255, 255, 0),
                                                         new Color(255, 255, 255),
                                                         new Color(0, 255, 255),
                                                         new Color(255, 0, 255));

    assertEquals(Color.WHITE, lightCalculator.getColour(surroundingColours));
  }

}
