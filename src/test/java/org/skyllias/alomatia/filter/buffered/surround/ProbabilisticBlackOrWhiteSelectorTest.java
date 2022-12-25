
package org.skyllias.alomatia.filter.buffered.surround;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProbabilisticBlackOrWhiteSelectorTest
{
  @Mock
  private Random random;

  @InjectMocks
  private ProbabilisticBlackOrWhiteSelector selector;

  @Before
  public void setUp()
  {
    when(random.nextFloat()).thenReturn(0.1f, 0.4f, 0.6f, 0.9f);
  }

  @Test
  public void shouldApplyThresholdWithLowLightness()
  {
    assertEquals(Color.WHITE, selector.chooseBlackOrWhite(0.3f));
    assertEquals(Color.BLACK, selector.chooseBlackOrWhite(0.3f));
    assertEquals(Color.BLACK, selector.chooseBlackOrWhite(0.3f));
    assertEquals(Color.BLACK, selector.chooseBlackOrWhite(0.3f));
  }

  @Test
  public void shouldApplyThresholdWithHighLightness()
  {
    assertEquals(Color.WHITE, selector.chooseBlackOrWhite(0.7f));
    assertEquals(Color.WHITE, selector.chooseBlackOrWhite(0.7f));
    assertEquals(Color.WHITE, selector.chooseBlackOrWhite(0.7f));
    assertEquals(Color.BLACK, selector.chooseBlackOrWhite(0.7f));
  }

}
