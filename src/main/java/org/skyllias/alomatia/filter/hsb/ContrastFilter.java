
package org.skyllias.alomatia.filter.hsb;

/** Filter that increases the difference in brightness of the colours in an image.
 *  <p>
 *  Of course brightness cannot be greater than 1 or smaller than 0, so the
 *  difference cannot grow homogeneously for all colour pairs. If it increases
 *  in some region, it must decrease in some other. So this filter focuses in
 *  the differences either in the middle range of brightnesses or in the lowest
 *  and highest brightnesses. */

public class ContrastFilter extends BasicHSBFilter
{
  private UnitFactor directFactor;                                              // the factor applied to the half of brightness values that have to increase with positive values
  private UnitFactor inverseFactor;                                             // the factor applied to the half of brightness values that have to increase with negative values... these explanations suck

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
double contrast;
  public ContrastFilter(double contrastFactor)
  {
    directFactor  = new UnitFactor(contrastFactor);
    inverseFactor = new UnitFactor(-contrastFactor);
  }

//==============================================================================

  /** Applies the factor to the original brightness avoiding results outside [0, 1].
   *  If the brightness graph is divided in quadrants, the lowert left and the
   *  upper right get a difeerent {@link UnitFactor} applied. */

  @Override
  protected float getNewBrightness(float hue, float saturation, float brightness)
  {
    final float MID_BRIGHTNESS = 0.5f;

    boolean isHighBrightness = (brightness > MID_BRIGHTNESS);
    UnitFactor factorToApply = isHighBrightness? directFactor: inverseFactor;

    if (isHighBrightness) return MID_BRIGHTNESS * (1 + factorToApply.apply(true, brightness / MID_BRIGHTNESS - 1));
    else                  return MID_BRIGHTNESS * factorToApply.apply(true, brightness / MID_BRIGHTNESS);
  }

//------------------------------------------------------------------------------

}
