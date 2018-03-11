
package org.skyllias.alomatia.filter.buffered.distortion;

import java.awt.geom.*;
import java.awt.geom.Point2D.Float;

/** Distortion that accumulates the displacements of two or more other distortions. */

public class DistortionChain implements Distortion
{
  private final Distortion[] distortions;

//==============================================================================

  /** Creates a chain with distortions, applying them in order. Distortions are
   *  commutable only if they are isotropic.
   *  Although zero and one distortions are supported, it does not make much
   *  sense to call this with less than two parameters. */

  public DistortionChain(Distortion... distortions)
  {
    this.distortions = distortions;
  }

//==============================================================================

  @Override
  public Float getSourcePoint(Float destination, Dimension2D bounds)
  {
    Float accumulatedSource = destination;
    for (Distortion distortion : distortions)
    {
      accumulatedSource = distortion.getSourcePoint(destination, bounds);
      destination       = accumulatedSource;
    }
    return accumulatedSource;
  }

//------------------------------------------------------------------------------

}
