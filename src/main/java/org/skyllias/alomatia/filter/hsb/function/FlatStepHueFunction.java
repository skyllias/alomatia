
package org.skyllias.alomatia.filter.hsb.function;

/** {@link HueFunction} that returns a fixed value for hues inside an interval
 *  and 0 for the rest. */

public class FlatStepHueFunction extends BasicStepHueFunction
{
//==============================================================================

  /** Creates an instance that will return height between stepStart and stepEnd
   *  (both inclusive), and zero anywhere else.
   *  If stepStart > stepEnd, then the circular property of the hue is applied as
   *  if the step stretches from stepStart to 1 + stepEnd.
   *  height may be negative. */

  public FlatStepHueFunction(double height, float stepStart, float stepEnd)
  {
    super(height, 0, stepStart, stepEnd);
  }

//==============================================================================

}
