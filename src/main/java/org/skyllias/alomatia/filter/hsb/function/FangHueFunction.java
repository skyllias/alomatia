
package org.skyllias.alomatia.filter.hsb.function;

/** {@link HueFunction} that returns a very low value for some central hue,
 *  grows linearly around both sides until it reaches zero at some distance and
 *  then returns zero for all other values. */

public class FangHueFunction implements HueFunction
{
  private static final double LOW_VALUE = -2;

  private float centre;
  private float halfWidth;

//==============================================================================

  /** Centre should be inside [0, 1).
   * Width should be inside (0, 0.5). */

  public FangHueFunction(float centre, float halfWidth)
  {
    this.centre = centre;
    this.halfWidth  = halfWidth;
  }

//==============================================================================

  @Override
  public double getValue(float hue)
  {
    if (hue < centre)
    {
      float lowerLimit = centre - halfWidth;
      if (hue > lowerLimit) return LOW_VALUE * (hue - lowerLimit) / halfWidth;

      float upperLimit = centre + halfWidth - 1;
      if (upperLimit > 0 && hue < upperLimit) return LOW_VALUE * (upperLimit - hue) / halfWidth;

      return 0;
    }

    float upperLimit = centre + halfWidth;
    if (hue < upperLimit) return LOW_VALUE * (upperLimit - hue) / halfWidth;

    float lowerLimit = centre - halfWidth + 1;
    if (lowerLimit < 1 && hue > lowerLimit) return LOW_VALUE * (hue - lowerLimit) / halfWidth;

    return 0;
  }

//------------------------------------------------------------------------------

}
