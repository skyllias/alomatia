
package org.skyllias.alomatia.filter.hsb;

import org.skyllias.alomatia.filter.factor.*;

/** Filter that increases the difference in brightness of the colours in an image.
 *  <p>
 *  Of course brightness cannot be greater than 1 or smaller than 0, so the
 *  difference cannot grow homogeneously for all colour pairs. If it increases
 *  in some region, it must decrease in some other. So this filter focuses in
 *  the differences either in the middle range of brightnesses or in the lowest
 *  and highest brightnesses. */

public class ContrastFilter extends BasicHSBFilter
{
  private ComposedUnitFactor factor;

//==============================================================================

  /** Creates a filter that modifies the contrast of images according to the
   *  value of contrastFactor:
   *  - If it is 0, then the brightness does not change.
   *  - If is is lower than 0, the contrast of very dark and very bright colours
   *    is increased and the contrast of middle values is decreased.
   *  - If is is higher than 0, the contrast of very dark and very bright colours
   *    is decreased and the contrast of middle values is increased.
   *  - With large negative numbers (3 and above), everything becomes solarized.
   *  - With large positive numbers (3 and above), everything becomes almost black or white.
   *  - The first noticeable differences occur with absolute values of the order of 0.1. */

  public ContrastFilter(double contrastFactor)
  {
    factor = new ComposedUnitFactor(contrastFactor);
  }

//==============================================================================

  /** Applies the factor to the original brightness avoiding results outside [0, 1].
   *  If the brightness graph is divided in quadrants, the lowert left and the
   *  upper right get a different {@link UnitFactor} applied. */

  @Override
  protected float getNewBrightness(float hue, float saturation, float brightness)
  {
    return factor.apply(brightness);
  }

//------------------------------------------------------------------------------

}
