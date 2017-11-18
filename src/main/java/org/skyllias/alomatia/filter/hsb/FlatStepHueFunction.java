
package org.skyllias.alomatia.filter.hsb;

/** {@link HueFunction} that returns a fixed value for hues inside an interval
 *  and 0 for the rest. */

public class FlatStepHueFunction implements HueFunction
{
  private double value;
  private boolean crossingZero;                                                 // if false, the step begins at the lower limit and ends at the upper; if true, it's the other way round
  private float lowerLimit, upperLimit;

//==============================================================================

  /** Creates an instance that will return height between stepStart and stepEnd
   *  (both inclusive), and zero anywhere else.
   *  If stepStart > stepEnd, then the circular property of the hue is applied as
   *  if the step stretches from stepStart to 1 + stepEnd.
   *  height may be negative. */

  public FlatStepHueFunction(double height, float stepStart, float stepEnd)
  {
    value = height;

    if (stepStart <= stepEnd)
    {
      crossingZero = false;
      lowerLimit   = stepStart;
      upperLimit   = stepEnd;
    }
    else
    {
      crossingZero = true;
      lowerLimit   = stepEnd;
      upperLimit   = stepStart;
    }
  }

//==============================================================================

  @Override
  public double getValue(float hue)
  {
    boolean insideLimits = lowerLimit <= hue && hue <= upperLimit;
    if (insideLimits ^ crossingZero) return value;
    else                             return 0;
  }

//------------------------------------------------------------------------------

}
