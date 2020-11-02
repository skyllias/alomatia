
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;
import java.util.Random;

/** Selector of white with a probability equal to lightness. */

public class ProbabilisticBlackOrWhiteSelector implements BlackOrWhiteSelector
{
  private Random random = new Random();

//==============================================================================

  @Override
  public Color chooseBlackOrWhite(float lightness)
  {
    float threshold = random.nextFloat();

    return (lightness < threshold) ? Color.BLACK : Color.WHITE;
  }

//------------------------------------------------------------------------------

  /** For testing purposes only. */

  protected void setRandom(Random random)
  {
    this.random = random;
  }

//------------------------------------------------------------------------------

}
