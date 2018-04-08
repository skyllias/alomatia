
package org.skyllias.alomatia.filter.buffered.distortion.rotational;

/** {@link RotationalDistortionProfile} that always returns a fixed angle. */

public class ConstantRotationalDistortionProfile implements RotationalDistortionProfile
{
  private final float angle;

//==============================================================================

  public ConstantRotationalDistortionProfile(float angle)
  {
    this.angle = angle;
  }

//==============================================================================

  @Override
  public float getSourceAngularShift(float radius, float referenceRadius)
  {
    return angle;
  }

//------------------------------------------------------------------------------

}
