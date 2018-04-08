
package org.skyllias.alomatia.filter.buffered.distortion.rotational;

/** {@link RotationalDistortionProfile} that is almost unnoticeable in the edges
 *  and grows in the image center. */

public class WhirlpoolRotationalDistortionProfile implements RotationalDistortionProfile
{
  private static final float DISTANCE_FACTOR = 4;

  private final float referenceAngle;

//==============================================================================

  /** referenceAngle is the rotation at approx. referenceRadius / 4. */

  public WhirlpoolRotationalDistortionProfile(float referenceAngle)
  {
    this.referenceAngle = referenceAngle;
  }

//==============================================================================

  @Override
  public float getSourceAngularShift(float radius, float referenceRadius)
  {
    return referenceAngle * (float) Math.exp((referenceRadius - DISTANCE_FACTOR * radius) / referenceRadius);
  }

//------------------------------------------------------------------------------

}
