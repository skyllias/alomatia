
package org.skyllias.alomatia.filter.buffered.surround;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class StrictBlackOrWhiteSelectorTest
{

  @Test
  public void shouldReturnBlackWhenLowLightBelowThreshold()
  {
    assertEquals(Color.RED, new StrictBlackOrWhiteSelector(0.5f, Color.RED, Color.BLUE).chooseBlackOrWhite(0.4f));
  }

  @Test
  public void shouldReturnBlackWhenHighLightBelowThreshold()
  {
    assertEquals(Color.RED, new StrictBlackOrWhiteSelector(0.7f, Color.RED, Color.BLUE).chooseBlackOrWhite(0.6f));
  }

  @Test
  public void shouldReturnWhiteWhenHighLightAboveThreshold()
  {
    assertEquals(Color.WHITE, new StrictBlackOrWhiteSelector(0.5f, Color.BLACK, Color.WHITE).chooseBlackOrWhite(0.6f));
  }

  @Test
  public void shouldReturnWhiteWhenLowLightAboveThreshold()
  {
    assertEquals(Color.WHITE, new StrictBlackOrWhiteSelector(0.3f, Color.BLACK, Color.WHITE).chooseBlackOrWhite(0.4f));
  }

}
