
package org.skyllias.alomatia.filter.daltonism;

/** Provider of projector instances for the known anomalies. */

public class LmsProjectorFactory
{
//==============================================================================

  /** Returns the projector for protanopia. */

  public LmsProjector getProtanopiaProjector()
  {
    return new LmsProjector(0, 2.02344, -2.52581, 0, 1, 0, 0, 0, 1);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for deuteranopia. */

  public LmsProjector getDeuteranopiaProjector()
  {
    return new LmsProjector(1, 0, 0, 0.49421, 0, 1.24827, 0, 0, 1);
  }

//------------------------------------------------------------------------------

  /** Returns the projector for tritanopia. */

  public LmsProjector getTritanopiaProjector()
  {
    return new LmsProjector(1, 0, 0, 0, 1, 0, -0.395913, 0.801109, 0);
  }

//------------------------------------------------------------------------------

}
