
package org.skyllias.alomatia.filter.hsl;

import org.skyllias.alomatia.filter.factor.ComposedUnitFactor;
import org.skyllias.alomatia.filter.factor.UnitFactor;

/** Converter that increases the difference in lightness of the colours in an image.
 *  <p>
 *  Of course lightness cannot be greater than 1 or smaller than 0, so the
 *  difference cannot grow homogeneously for all colour pairs. If it increases
 *  in some region, it must decrease in some other. So this filter focuses in
 *  the differences either in the middle range of lightnesses or in the lowest
 *  and highest lightnesses.
 *  <p>
 *  Unlike its HSB counterpart, the extreme values fade to white and to black
 *  instead of to fully saturated colours and to black. */

public class ContrastConverter implements HslConverter
{
  private final ComposedUnitFactor factor;

//==============================================================================

  /** Creates a filter that modifies the contrast of images according to the
   *  value of contrastFactor:
   *  - If it is 0, then the lightness does not change.
   *  - If is is lower than 0, the contrast of very dark and very light colours
   *    is increased and the contrast of middle values is decreased.
   *  - If is is higher than 0, the contrast of very dark and very light colours
   *    is decreased and the contrast of middle values is increased.
   *  - With large negative numbers (3 and above), everything becomes solarized.
   *  - With large positive numbers (3 and above), everything becomes almost black or white.
   *  - The first noticeable differences occur with absolute values of the order of 0.1. */

  public ContrastConverter(double contrastFactor)
  {
    factor = new ComposedUnitFactor(contrastFactor);
  }

//==============================================================================

  /** Applies the factor to the original lightness avoiding results outside [0, 1].
   *  If the lightness graph is divided in quadrants, the lower left and the
   *  upper right get a different {@link UnitFactor} applied. */

  @Override
  public float getNewLightness(float hue, float saturation, float lightness)
  {
    return factor.apply(lightness);
  }

//------------------------------------------------------------------------------

}
