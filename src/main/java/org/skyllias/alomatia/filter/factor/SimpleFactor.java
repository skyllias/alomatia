
package org.skyllias.alomatia.filter.factor;

/** BasicExponentialFactor that maintains a constant slope in the function,
 *  fixing either the (0, 0) or the (1, 1) point depending on the initial open
 *  factor. */

public class SimpleFactor extends BasicExponentialFactor
{
//==============================================================================

  public SimpleFactor(double openFactor) {super(openFactor);}

//==============================================================================

  @Override
  public float apply(float magnitude)
  {
    double slope     = getSlope();
    boolean decrease = slope <= 1;
    if (decrease) return (float) (slope * magnitude);
    else          return (float) (1 - (1 - magnitude) / slope);
  }

//------------------------------------------------------------------------------

}
