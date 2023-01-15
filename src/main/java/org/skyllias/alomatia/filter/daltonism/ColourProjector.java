
package org.skyllias.alomatia.filter.daltonism;

/** Matrix-like data that defines how a ProjectableColour is projected (ie, how
 *  its components are linearly combined) in an abnormally functioning eye.
 *  Each anomaly is characterized by a different projector. Usually, the sum of
 *  values maintains the total energy, but that is not forced by this class.
 *  <p>
 *  The oxToRy items are to be interpreted as the factor to multiply the original
 *  x component to contribute to the resulting y.
 *  <p>
 *  A projector works on a given colour space, and it should only be applied to
 *  a colour representation in the same space. Otherwise, results are impredictable. */

public class ColourProjector
{
  private final ColourSpace space;
  private final double o1ToR1, o1ToR2, o1ToR3, o2ToR1, o2ToR2, o2ToR3, o3ToR1, o3ToR2, o3ToR3;

//==============================================================================

  public ColourProjector(ColourSpace colourSpace, double o1ToR1, double o2ToR1,
                         double o3ToR1, double o1ToR2, double o2ToR2, double o3ToR2,
                         double o1ToR3, double o2ToR3, double o3ToR3)
  {
    space = colourSpace;

    this.o1ToR1 = o1ToR1;
    this.o1ToR2 = o1ToR2;
    this.o1ToR3 = o1ToR3;
    this.o2ToR1 = o2ToR1;
    this.o2ToR2 = o2ToR2;
    this.o2ToR3 = o2ToR3;
    this.o3ToR1 = o3ToR1;
    this.o3ToR2 = o3ToR2;
    this.o3ToR3 = o3ToR3;
  }

//==============================================================================

  public ColourSpace getSpace()
  {
    return space;
  }

  public double getO1ToR1()
  {
    return o1ToR1;
  }

  public double getO1ToR2()
  {
    return o1ToR2;
  }

  public double getO1ToR3()
  {
    return o1ToR3;
  }

  public double getO2ToR1()
  {
    return o2ToR1;
  }

  public double getO2ToR2()
  {
    return o2ToR2;
  }

  public double getO2ToR3()
  {
    return o2ToR3;
  }

  public double getO3ToR1()
  {
    return o3ToR1;
  }

  public double getO3ToR2()
  {
    return o3ToR2;
  }

  public double getO3ToR3()
  {
    return o3ToR3;
  }

//------------------------------------------------------------------------------

}
