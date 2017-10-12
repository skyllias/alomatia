
package org.skyllias.alomatia.filter.daltonism;

/** Provider of projector instances for the known anomalies in the LMS colour space. */

public class LmsProjectorFactory
{
  /* See https://stacks.stanford.edu/file/druid:yj296hj2790/Woods_Assisting_Color_Blind_Viewers.pdf */

  private static ColourSpace lmsColourSpace = new ColourSpace()
  {
    @Override
    public double[][] getConversionMatrix()
    {
      return new double[][] {{17.8824, 43.5161, 4.11935},
                             {3.45565, 27.1554, 3.86714},
                             {0.0299566, 0.184309, 1.46709}};
    }
    @Override
    public double[][] getInverseConversionMatrix()
    {
      return new double[][] {{0.0809444479, -0.130504409, 0.116721066},
                             {-0.0102485335, 0.0540193266, -0.113614708},
                             {-0.000365296938, -0.00412161469, 0.693511405}};
    }
  };

//==============================================================================

  /** Returns the projector for protanopia. */

  public ColourProjector getProtanopiaProjector()
  {
    return new ColourProjector(lmsColourSpace, 0, 2.02344, -2.52581, 0, 1, 0, 0, 0, 1);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for deuteranopia. */

  public ColourProjector getDeuteranopiaProjector()
  {
    return new ColourProjector(lmsColourSpace, 1, 0, 0, 0.49421, 0, 1.24827, 0, 0, 1);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for tritanopia. */

  public ColourProjector getTritanopiaProjector()
  {
    return new ColourProjector(lmsColourSpace, 1, 0, 0, 0, 1, 0, -0.395913, 0.801109, 0);
  }

//------------------------------------------------------------------------------

}
