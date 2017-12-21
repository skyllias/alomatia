
package org.skyllias.alomatia.filter.buffered.vignette;

/** Profile that linearly depends on either the horizontal or vertical distance
 *  of the point from the middle. */

public class LinearProfile implements VignetteProfile
{
  private boolean horizontal;

//==============================================================================

  /** Creates a profile that depends solely on the distance to the horizontal axis
   *  if isHorizontal, or to the vertical axis otherwise. */

  public LinearProfile(boolean isHorizontal) {horizontal = isHorizontal;}

//==============================================================================

  @Override
  public float getVignetteEffect(int x, int y, int width, int height)
  {
    if (horizontal) return getSingleDimensionEffect(x, width);
    else            return getSingleDimensionEffect(y, height);
  }

//------------------------------------------------------------------------------

  /* Returns 1 if position is 0 or max, 0 if it is max / 2, and intermediate
   * linear values in-between. */

  private float getSingleDimensionEffect(int position, int max)
  {
    float half     = max / 2;
    float distance = Math.abs(position - half);
    return distance / half;
  }

//------------------------------------------------------------------------------

}
