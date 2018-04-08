
package org.skyllias.alomatia.filter.buffered.distortion.rotational;

/** {@link RotationalDistortionProfile} that is almost unnoticeable in the center
 *  and grows in the image edges. */

public class IncreasingRotationalDistortionProfile implements RotationalDistortionProfile
{
  private final float referenceAngle;

//==============================================================================

  /** referenceAngle is the rotation at referenceRadius. */

  public IncreasingRotationalDistortionProfile(float referenceAngle)
  {
    this.referenceAngle = referenceAngle;
  }

//==============================================================================

  @Override
  public float getSourceAngularShift(float radius, float referenceRadius)
  {
    return radius * referenceAngle / referenceRadius;
  }

//------------------------------------------------------------------------------

}
