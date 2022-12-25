
package org.skyllias.alomatia.filter.buffered.distortion.radial;

/** {@link RadialDistortionProfile} that makes the central region smaller. */

public class ReductorRadialDistortionProfile implements RadialDistortionProfile
{
  private final float reductionFactor;
  private final float spatialFactor;

//==============================================================================

  /** Creates a reductor with a reductionFactor that affects how much the image
   *  is reduced (small values distort is less) and a spatialFactor that affects
   *  how big the reduced region is (small values mean larger regions). */

  public ReductorRadialDistortionProfile(float reductionFactor, float spatialFactor)
  {
    this.reductionFactor = reductionFactor;
    this.spatialFactor   = spatialFactor;
  }

//==============================================================================

  @Override
  public float getSourceRadiusShift(float radius, float referenceRadius)
  {
    float relativeRadius = radius / referenceRadius;

    return  (reductionFactor + relativeRadius) * (float) Math.exp((-relativeRadius * spatialFactor));
  }

//------------------------------------------------------------------------------

}
