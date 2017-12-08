
package org.skyllias.alomatia.filter.hsb;

/** {@link HueFunction} that returns a zero for hues inside an interval and a
 *  big negative number for the rest.
 *  "Big" could be the as much as infinite if it weren't that the result of this
 *  this function are passed to a function that applies exponential functions
 *  to it. So, a reasonable value is used so that finally it provides equivalent
 *  results without overflow problems. */

public class PitStepHueFunction extends BasicStepHueFunction
{
  private static final double PIT_VALUE = -10;

//==============================================================================

  /** Creates an instance that will return zerp between stepStart and stepEnd
   *  (both inclusive).
   *  If stepStart > stepEnd, then the circular property of the hue is applied as
   *  if the step stretches from stepStart to 1 + stepEnd. */

  public PitStepHueFunction(float stepStart, float stepEnd)
  {
    super(0, PIT_VALUE, stepStart, stepEnd);
  }

//==============================================================================

}
