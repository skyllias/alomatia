
package org.skyllias.alomatia.filter.daltonism;

/** Provider of projector instances for the known anomalies in the XMY colour space?
 *  <p>
 *  It is unclear whether the anomaly matrices found in different sites are to be
 *  applied directly to the RGB components, but
 *  https://github.com/MaPePeR/jsColorblindSimulator/blob/master/colorblind.js
 *  seems to do it. So this mimics that without much confidence or knowledge
 *  about the proper usage of the matrices.
 *  <p>
 *  Compared with the samples from https://konanmedical.com/colordx/clinical-resources/#gform_12
 *  these results are very bad for prot- and deuter-, and so so for trit-, but
 *  apart from that they look more "realistic" than the LMS counterparts. */

public class XyzProjectorFactory
{
  private static ColourSpace directColourSpace = new ColourSpace()
  {
    @Override
    public double[][] getConversionMatrix()
    {
      return new double[][] {{1, 0, 0},
                             {0, 1, 0},
                             {0, 0, 1}};
    }
    @Override
    public double[][] getInverseConversionMatrix()
    {
      return new double[][] {{1, 0, 0},
                             {0, 1, 0},
                             {0, 0, 1}};
    }
  };

//==============================================================================

  /** Returns the projector for protanopia. */

  public ColourProjector getProtanopiaProjector()
  {
    return new ColourProjector(directColourSpace, 0.56667, 0.43333, 0, 0.55833, 0.44167, 0, 0, 0.24167, 0.75833);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for protanomaly. */

  public ColourProjector getProtanomalyProjector()
  {
    return new ColourProjector(directColourSpace, 0.81667, 0.18333, 0, 0.33333, 0.66667, 0, 0, 0.125, 0.875);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for deuteranopia. */

  public ColourProjector getDeuteranopiaProjector()
  {
    return new ColourProjector(directColourSpace, 0.625, 0.375, 0, 0.7, 0.3, 0, 0, 0.3, 0.7);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for deuteranomaly. */

  public ColourProjector getDeuteranomalyProjector()
  {
    return new ColourProjector(directColourSpace, 0.8, 0.2, 0, 0.25833, 0.74167, 0, 0, 0.14167, 0.85833);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for tritanopia. */

  public ColourProjector getTritanopiaProjector()
  {
    return new ColourProjector(directColourSpace, 0.95, 0.05, 0, 0, 0.43333, 0.56667, 0, 0.475, 0.525);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for tritanomaly. */

  public ColourProjector getTritanomalyProjector()
  {
    return new ColourProjector(directColourSpace, 0.96667, 0.03333, 0, 0, 0.73333, 0.26667, 0, 0.18333, 0.81667);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for achromatopsia. */

  public ColourProjector getAchromatopsiaProjector()
  {
    return new ColourProjector(directColourSpace, 0.299, 0.587, 0.114, 0.299, 0.587, 0.114, 0.299, 0.587, 0.114);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for achromatomaly. */

  public ColourProjector getAchromatomalyProjector()
  {
    return new ColourProjector(directColourSpace, 0.618, 0.32, 0.062, 0.163, 0.775, 0.062, 0.163, 0.32, 0.516);
  }

//------------------------------------------------------------------------------

}
