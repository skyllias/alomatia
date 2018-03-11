
package org.skyllias.alomatia.filter.buffered.distortion.wave;

import java.awt.geom.*;
import java.awt.geom.Point2D.Float;

import org.skyllias.alomatia.filter.buffered.distortion.*;

/** Distortion that displaces pixels according to some sinusoidal function of
 *  constant amplitude and frequency.
 *  Waves have the following parameters:
 *  <ul>
 *    <li> Angle of propagation: angle between the horizontal and the direction
 *         taken as parameter of the sinus. 0 means horizontal; π/2, vertical;
 *         something in between, a bottom-left to top-right direction; something
 *         in between but negative, a top-left to bottom-right direction.
 *    <li> Angle of displacement: angle between the angle of propagation and the
 *         direction along which pixels are moved. 0 means longitudinal; π/2,
 *         perpendicular; something in between, oblique.
 *    <li> Period: Amount of pixels after which the distortion repeats.
 *    <li> Amplitude: Maximum length a pixel may be displaced. In longitudinal
 *         waves, it should not be greater than period/2π.
 *    Offsets are not considered, and all the above magnitudes are independent
 *    of the images' dimensions.
 *  </ul> */

public class IsotropicWaveDistortion implements Distortion
{
  private final float xFrequency, yFrequency;                                   // component of the frequency by the horizontal and vertical coordinate
  private final float xAmplitude, yAmplitude;                                   // component of the amplitude in the horizontal and vertical direction

//==============================================================================

  public IsotropicWaveDistortion(float propagationAngle, float displacementAngle,
                                 float period, float amplitude)
  {
    float frequency = (float) (2 * Math.PI / period);
    xFrequency      = frequency * (float) Math.cos(-propagationAngle);
    yFrequency      = frequency * (float) Math.sin(-propagationAngle);

    xAmplitude = amplitude * (float) Math.cos(-displacementAngle - propagationAngle);
    yAmplitude = amplitude * (float) Math.sin(-displacementAngle - propagationAngle);
  }

//==============================================================================

  @Override
  public Float getSourcePoint(Float destination, Dimension2D bounds)
  {
    float phase    = xFrequency * destination.x + yFrequency * destination.y;
    float unitWave = (float) Math.sin(phase);

    float horizontalDisplacement = unitWave * xAmplitude;
    float verticalDisplacement   = unitWave * yAmplitude;
    return new Float(destination.x + horizontalDisplacement,
                     destination.y + verticalDisplacement);
  }

//------------------------------------------------------------------------------

}
