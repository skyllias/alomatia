
package org.skyllias.alomatia.filter.daltonism;

/** Matrix-like data that defines how an LmsColour is projected (ie, how its
 *  components are linearly combined) in an abnormally functioning eye.
 *  Each anomaly is characterised by a different projector. Usually, the sum of
 *  values maintains the total energy, but that is not forced by this class.
 *  The xToY items are to be interpreted as the factor to multiply the original
 *  x to contribute to the resulting y. */

public class LmsProjector
{
  private double lToL, lToM, lToS, mToL, mToM, mToS, sToL, sToM, sToS;

//==============================================================================

  public LmsProjector(double lToL, double mToL, double sToL, double lToM,
                      double mToM, double sToM, double lToS, double mToS,
                      double sToS)
  {
    this.lToL = lToL;
    this.lToM = lToM;
    this.lToS = lToS;
    this.mToL = mToL;
    this.mToM = mToM;
    this.mToS = mToS;
    this.sToL = sToL;
    this.sToM = sToM;
    this.sToS = sToS;
  }

//==============================================================================

  public double getlToL()
  {
    return lToL;
  }

  public double getlToM()
  {
    return lToM;
  }

  public double getlToS()
  {
    return lToS;
  }

  public double getmToL()
  {
    return mToL;
  }

  public double getmToM()
  {
    return mToM;
  }

  public double getmToS()
  {
    return mToS;
  }

  public double getsToL()
  {
    return sToL;
  }

  public double getsToM()
  {
    return sToM;
  }

  public double getsToS()
  {
    return sToS;
  }

//------------------------------------------------------------------------------

}
