
package org.skyllias.alomatia.filter.buffered.vignette;

/** Profile that depends on some fixed power of either the horizontal or vertical
 *  distance of the point from the middle. */

public class PowerProfile implements VignetteProfile
{
  private final boolean horizontal;

//==============================================================================

  /** Creates a profile that depends solely on the distance to the horizontal
   *  axis if isHorizontal, or to the vertical axis otherwise. */

  public PowerProfile(boolean isHorizontal) {horizontal = isHorizontal;}

//==============================================================================

  @Override
  public float getVignetteEffect(int x, int y, int width, int height)
  {
    if (horizontal) return getSingleDimensionEffect(x, width);
    else            return getSingleDimensionEffect(y, height);
  }

//------------------------------------------------------------------------------

  /* Returns 1 if position is 0 or max, 0 if it is max / 2, and intermediate
   * power values in-between. */

  private float getSingleDimensionEffect(int position, int max)
  {
    float half     = max / 2;
    float distance = Math.abs(position - half);
    return (float) Math.pow(distance / half, 8);
  }

//------------------------------------------------------------------------------

}
