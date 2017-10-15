
package org.skyllias.alomatia.filter.hsb;

/** Filter that "rotates" the hue by a factor. */

public class HueShiftFilter extends BasicHSBFilter
{
  private float shift;

//==============================================================================

  /** Creates a filter that modifies the hue of images according to the value
   *  of hueShift:
   *  - If it is 0, then the hue does not change.
   *  - If is is lower than 0, it is reduced (red becomes purple).
   *  - If is is higher than 0, it is increased (red becomes yellow).
   *  - All x + n, where n is any integer, yield te same results.
   *  Shifts with absolute value over 0.1 begin to become unrealistic; below
   *  0.01 are difficult to notice. */

  public HueShiftFilter(float hueShift)
  {
    shift = hueShift;
  }

//==============================================================================

  /** Applies the linear shift to the original hue. */

  @Override
  protected float getNewHue(float hue, float saturation, float brightness)
  {
    return hue + shift;
  }

//------------------------------------------------------------------------------

}
