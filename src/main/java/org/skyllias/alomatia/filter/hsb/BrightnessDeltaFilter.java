
package org.skyllias.alomatia.filter.hsb;

/** Filter that increases brightness by a fixed value.
 *  This differs slightly from {@link BrightnessFilter} because there the
 *  increase depends on the original brightness, while here it is always the same.
 *  <p>
 *  This produces in general worse results alone but can be used in a composition. */

public class BrightnessDeltaFilter extends BasicHSBFilter
{
  private float delta;

//==============================================================================

  /** Creates a filter that modifies the brightness of images by adding deltaBrightness.
   *  It should be in the interval (-1, 1). */

  public BrightnessDeltaFilter(float deltaBrightness)
  {
    delta = deltaBrightness;
  }

//==============================================================================

  /** Adds the delta. */

  @Override
  protected float getNewBrightness(float hue, float saturation, float brightness)
  {
    final float MAX = 1, MIN = 0;

    return Math.max(MIN, Math.min(MAX, brightness + delta));
  }

//------------------------------------------------------------------------------

}
