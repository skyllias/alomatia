
package org.skyllias.alomatia.filter.buffered.distortion.radial;

/** {@link RadialDistortionProfile} that makes the central region bigger. */

public class MagnifierRadialDistortionProfile implements RadialDistortionProfile
{
  private float distortionFactor;
  private float radialFactor;

//==============================================================================

  /** Creates a new magnifier with a distortionFactor that affects how much the
   *  central region is magnified (something around 0.75 seems fine, and 0 means
   *  no distortion at all) and a radialFactor that affects how sharp  the
   *  distorted region is (something around 4 seems fine, while low values
   *  affect the whole picture and high values make the distortion frontier very
   *  clear). */

  public MagnifierRadialDistortionProfile(float distortionFactor, float radialFactor)
  {
    this.distortionFactor = distortionFactor;
    this.radialFactor     = radialFactor;
  }

//==============================================================================

  @Override
  public float getSourceRadiusShift(float radius, float referenceRadius)
  {
    float relativeRadius = radius / referenceRadius;

    return - distortionFactor / (1 + (float) Math.exp(radialFactor * (relativeRadius - 1)));
  }

//------------------------------------------------------------------------------

}
