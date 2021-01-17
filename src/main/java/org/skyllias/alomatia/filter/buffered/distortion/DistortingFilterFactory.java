
package org.skyllias.alomatia.filter.buffered.distortion;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.HintlessBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.SingleFrameBufferedImageFilter;
import org.skyllias.alomatia.filter.buffered.distortion.radial.MagnifierRadialDistortionProfile;
import org.skyllias.alomatia.filter.buffered.distortion.radial.RadialDistortion;
import org.skyllias.alomatia.filter.buffered.distortion.radial.RadialDistortionProfile;
import org.skyllias.alomatia.filter.buffered.distortion.radial.ReductorRadialDistortionProfile;
import org.skyllias.alomatia.filter.buffered.distortion.rotational.ConstantRotationalDistortionProfile;
import org.skyllias.alomatia.filter.buffered.distortion.rotational.IncreasingRotationalDistortionProfile;
import org.skyllias.alomatia.filter.buffered.distortion.rotational.RotationalDistortion;
import org.skyllias.alomatia.filter.buffered.distortion.rotational.RotationalDistortionProfile;
import org.skyllias.alomatia.filter.buffered.distortion.rotational.WhirlpoolRotationalDistortionProfile;
import org.skyllias.alomatia.filter.buffered.distortion.wave.IsotropicWaveDistortion;

/** Instantiator of filters that distort images. */

public class DistortingFilterFactory
{
  private static final Interpolator INTERPOLATOR = new BilinearInterpolator();

//==============================================================================

  public static ImageFilter forMagnifier(float distortionFactor, float radialFactor)
  {
    RadialDistortionProfile distortionProfile = new MagnifierRadialDistortionProfile(distortionFactor,
                                                                                     radialFactor);
    return forDistortion(new RadialDistortion(distortionProfile, true));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forReductor(float reductionFactor, float spatialFactor,
                                        boolean useSmallestDimension)
  {
    RadialDistortionProfile distortionProfile = new ReductorRadialDistortionProfile(reductionFactor,
                                                                                    spatialFactor);
    return forDistortion(new RadialDistortion(distortionProfile, useSmallestDimension));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forIsotropicWave(float propagationAngle, float displacementAngle,
                                             float period, float amplitude)
  {
    IsotropicWaveDistortion waveDistortion = new IsotropicWaveDistortion(propagationAngle,
                                                                         displacementAngle,
                                                                         period, amplitude);
    return forDistortion(waveDistortion);
  }

//------------------------------------------------------------------------------

  public static ImageFilter forRoughWaves()
  {
    Distortion waveDistortion = new DistortionChain(new IsotropicWaveDistortion(0, 1.57f, 400, 30),
                                                    new IsotropicWaveDistortion(1, .57f, 200, 20),
                                                    new IsotropicWaveDistortion(1.57f, 1.57f, 800, 50),
                                                    new IsotropicWaveDistortion(-1, 1.57f, 90, 15),
                                                    new IsotropicWaveDistortion(-0.2f, -0.3f, 1000, 30));
    return forDistortion(waveDistortion);
  }

//------------------------------------------------------------------------------

  public static ImageFilter forConstantRotation(float angle)
  {
    RotationalDistortionProfile rotationalDistortionProfile = new ConstantRotationalDistortionProfile(angle);
    return forDistortion(new RotationalDistortion(rotationalDistortionProfile, true));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forIncreasingRotation(float referenceAngle, boolean usingSmallestDimension)
  {
    RotationalDistortionProfile rotationalDistortionProfile = new IncreasingRotationalDistortionProfile(referenceAngle);
    return forDistortion(new RotationalDistortion(rotationalDistortionProfile, usingSmallestDimension));
  }

//------------------------------------------------------------------------------

  public static ImageFilter forWhirlpool(float referenceAngle, boolean usingSmallestDimension)
  {
    RotationalDistortionProfile rotationalDistortionProfile = new WhirlpoolRotationalDistortionProfile(referenceAngle);
    return forDistortion(new RotationalDistortion(rotationalDistortionProfile, usingSmallestDimension));
  }

//------------------------------------------------------------------------------

  private static ImageFilter forDistortion(Distortion distortion)
  {
    DistortingBufferedImageOperation imageOperation = new DistortingBufferedImageOperation(distortion, INTERPOLATOR);

    return new SingleFrameBufferedImageFilter(new HintlessBufferedImageOp(imageOperation));
  }

//------------------------------------------------------------------------------


}
