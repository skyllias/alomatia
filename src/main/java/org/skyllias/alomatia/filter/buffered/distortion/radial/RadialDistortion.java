
package org.skyllias.alomatia.filter.buffered.distortion.radial;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D.Float;

import org.skyllias.alomatia.filter.buffered.distortion.Distortion;

/** {@link Distortion} that enlarges the central region of images with circular
 *  symmetry. */

public class RadialDistortion implements Distortion
{
  private RadialDistortionProfile profile;
  private boolean usingSmallestDimension;

//==============================================================================

  public RadialDistortion(RadialDistortionProfile radialDistortionProfile, boolean useSmallestDimension)
  {
    profile                = radialDistortionProfile;
    usingSmallestDimension = useSmallestDimension;
  }

//==============================================================================

  @Override
  public Float getSourcePoint(Float destination, Dimension2D bounds)
  {
    float centerX = (float) bounds.getWidth() / 2;
    float centerY = (float) bounds.getHeight() / 2;

    float radius          = (float) destination.distance(centerX, centerY);
    float referenceRadius = (usingSmallestDimension ^ (centerX > centerY))? centerX: centerY;
    float shiftFactor     = 1 + profile.getSourceRadiusShift(radius, referenceRadius);

    return new Float(centerX + (destination.x - centerX) * shiftFactor,
                     centerY + (destination.y - centerY) * shiftFactor);
  }

//------------------------------------------------------------------------------

}
