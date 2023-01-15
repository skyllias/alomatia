
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.util.Random;

/** Selector of white with a probability equal to lightness. */

public class ProbabilisticBlackOrWhiteSelector implements BlackOrWhiteSelector
{
  private final Random random;

//==============================================================================

  public ProbabilisticBlackOrWhiteSelector(Random random)
  {
    this.random = random;
  }

//==============================================================================

  @Override
  public Color chooseBlackOrWhite(float lightness)
  {
    float threshold = random.nextFloat();

    return (lightness < threshold) ? Color.BLACK : Color.WHITE;
  }

//------------------------------------------------------------------------------

}
