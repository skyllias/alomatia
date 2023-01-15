
package org.skyllias.alomatia.filter.buffered.surround;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class StrictBlackOrWhiteSelectorTest
{

  @Test
  public void shouldReturnBlackWhenDark()
  {
    assertEquals(Color.RED, new StrictBlackOrWhiteSelector(Color.RED, Color.BLUE).chooseBlackOrWhite(0.4f));
  }

  @Test
  public void shouldReturnWhiteWhenLight()
  {
    assertEquals(Color.WHITE, new StrictBlackOrWhiteSelector(Color.BLACK, Color.WHITE).chooseBlackOrWhite(0.6f));
  }

}
