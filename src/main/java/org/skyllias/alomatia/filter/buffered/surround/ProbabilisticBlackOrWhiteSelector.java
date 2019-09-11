
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;

import org.apache.commons.lang.math.RandomUtils;

/** Selector of white with a probability equal to lightness. */

public class ProbabilisticBlackOrWhiteSelector implements BlackOrWhiteSelector
{
//==============================================================================

  @Override
  public Color chooseBlackOrWhite(float lightness)
  {
    float threshold = RandomUtils.nextFloat();

    return (lightness < threshold) ? Color.BLACK : Color.WHITE;
  }

//------------------------------------------------------------------------------

}
