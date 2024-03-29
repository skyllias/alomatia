
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;

/** Selector of "black" when lightness is below 0.5 and of "white" when it is above 0.5.
 *  Colour names are quoted above because they need not really be block or white
 *  but some other colour provided in the constructor to be used as the nominal
 *  colour. */

public class StrictBlackOrWhiteSelector implements BlackOrWhiteSelector
{
  private final float threshold;
  private final Color blackColor;
  private final Color whiteColor;

//==============================================================================

  public StrictBlackOrWhiteSelector(float threshold, Color blackColor, Color whiteColor)
  {
    this.threshold  = threshold;
    this.blackColor = blackColor;
    this.whiteColor = whiteColor;
  }

//==============================================================================

  @Override
  public Color chooseBlackOrWhite(float lightness)
  {
    return (lightness < threshold) ? blackColor : whiteColor;
  }

//------------------------------------------------------------------------------

}
