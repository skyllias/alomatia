
package org.skyllias.alomatia.filter.hsb.function;

/** Superclass for the {@link HueFunction}s that return a fixed value for hues
 *  inside an interval and another fixed value for the rest. */

public abstract class BasicStepHueFunction implements HueFunction
{
  private double valueInsideStep, valueOutsideStep;
  private boolean crossingZero;                                                 // if false, the step begins at the lower limit and ends at the upper; if true, it's the other way round
  private float lowerLimit, upperLimit;

//==============================================================================

  /** Creates an instance that will return heightInsideStep between stepStart
   *  and stepEnd (both inclusive), and heightOutsideStep anywhere else.
   *  If stepStart > stepEnd, then the circular property of the hue is applied
   *  as if the step stretches from stepStart to 1 + stepEnd.
   *  Both heightInsideStep and heightOutsideStep may be negative. */

  protected BasicStepHueFunction(double heightInsideStep, double heightOutsideStep,
                                 float stepStart, float stepEnd)
  {
    valueInsideStep  = heightInsideStep;
    valueOutsideStep = heightOutsideStep;

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
    if (insideLimits ^ crossingZero) return valueInsideStep;
    else                             return valueOutsideStep;
  }

//------------------------------------------------------------------------------

}
