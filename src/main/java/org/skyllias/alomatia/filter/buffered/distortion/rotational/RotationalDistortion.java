
package org.skyllias.alomatia.filter.buffered.distortion.rotational;

import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D.Float;

import org.skyllias.alomatia.filter.buffered.distortion.Distortion;

/** {@link Distortion} that moves pixels with an angle from the center. */

public class RotationalDistortion implements Distortion
{
  private final RotationalDistortionProfile profile;
  private final boolean usingSmallestDimension;

//==============================================================================

  public RotationalDistortion(RotationalDistortionProfile profile,
                              boolean usingSmallestDimension)
  {
    this.profile                = profile;
    this.usingSmallestDimension = usingSmallestDimension;
  }

//==============================================================================

  /** TODO RadialDistortion has some common calculations.
   *  Remember that cos(A + B) = cosA · cosB - sinA · sinB and
   *  sin(A + B) = sinA · cosB - cosA · sinB. */

  @Override
  public Float getSourcePoint(Float destination, Dimension2D bounds)
  {
    float centerX     = (float) bounds.getWidth() / 2;
    float centerY     = (float) bounds.getHeight() / 2;
    float xFromCenter = destination.x - centerX;
    float yFromCenter = destination.y - centerY;

    float radius          = (float) destination.distance(centerX, centerY);
    float referenceRadius = (usingSmallestDimension ^ (centerX > centerY))? centerX: centerY;

    float rotationAngle  = profile.getSourceAngularShift(radius, referenceRadius);
    float rotationSine   = (float) Math.sin(rotationAngle);
    float rotationCosine = (float) Math.cos(rotationAngle);

    float newXFromCenter = xFromCenter * rotationCosine - yFromCenter * rotationSine;
    float newYFromCenter = xFromCenter * rotationSine   + yFromCenter * rotationCosine;
    return new Float(centerX + newXFromCenter, centerY + newYFromCenter);
  }

//------------------------------------------------------------------------------

}
