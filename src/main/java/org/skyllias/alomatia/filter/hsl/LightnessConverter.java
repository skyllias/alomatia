
package org.skyllias.alomatia.filter.hsl;

import org.skyllias.alomatia.filter.factor.SimpleFactor;
import org.skyllias.alomatia.filter.factor.UnitFactor;

/** Converter that increases or decreases the lightness of the colours in an
 *  image by a non-linear factor.
 *  <p>
 *  Unlike its HSB counterpart, increasing the lightness fades the colours
 *  towards white instead of keeping them saturated.
 *  <p>
 *  This could be a particular case of {@link HueDependingLightnessConverter}. */

public class LightnessConverter implements HslConverter
{
  private final UnitFactor unitFactor;

//==============================================================================

  /** Creates a filter that modifies the lightness of images according to the
   *  value of lightnessFactor:
   *  - If it is 0, then the lightness does not change.
   *  - If is is lower than 0, it is reduced.
   *  - If is is higher than 0, it is increased.
   *  - With large negative numbers (3 and above), everything becomes nearly black.
   *  - With large positive numbers (3 and above), everything becomes nearly white.
   *  - The first noticeable differences occur with absolute values of the order of 0.1. */

  public LightnessConverter(double lightnessFactor)
  {
    unitFactor = new SimpleFactor(lightnessFactor);
  }

//==============================================================================

  /** Applies a {@link SimpleFactor} to the original lightness avoiding results
   *  outside [0, 1]. */

  @Override
  public float getNewLightness(float hue, float saturation, float lightness)
  {
    return unitFactor.apply(lightness);
  }

//------------------------------------------------------------------------------

}
