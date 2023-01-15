
package org.skyllias.alomatia.filter.hsb.function;

/** {@link HueFunction} that follows a full cosine phase centered at a certain hue. */

public class CosineHueFunction implements HueFunction
{
  private final float center;
  private final double max, min;

//==============================================================================

  /** Creates an instance that will return maxValue at centre and minValue at
   *  ceter +/- 0.5, using  a trigonometric function for the remaining domain.
   *  Despite its name, maxValue may be smaller than minValue. */

  public CosineHueFunction(float centre, double maxValue, double minValue)
  {
    center = centre;
    max    = maxValue;
    min    = minValue;
  }

//==============================================================================

  @Override
  public double getValue(float hue)
  {
    final double PHASE = 2 * Math.PI;

    double amplitude = (max - min) / 2;
    double middle    = (max + min) / 2;
    return middle + amplitude * Math.cos((hue - center) * PHASE);
  }

//------------------------------------------------------------------------------

}
