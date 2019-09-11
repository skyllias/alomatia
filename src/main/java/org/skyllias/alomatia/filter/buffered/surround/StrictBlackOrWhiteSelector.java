
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;

/** Selector of black when lightness is below 0.5 and of white when it is above 0.5. */

public class StrictBlackOrWhiteSelector implements BlackOrWhiteSelector
{
  private static final float THRESHOLD = 0.5f;

//==============================================================================

  @Override
  public Color chooseBlackOrWhite(float lightness)
  {
    return (lightness < THRESHOLD) ? Color.BLACK : Color.WHITE;
  }

//------------------------------------------------------------------------------

}
