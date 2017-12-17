
package org.skyllias.alomatia.filter.buffered.vignette;

/** Profile that depends on the distance to the center, with the maximum on the corners.
 *  If width and height are equal, then the profile will be circular; otherwise,
 *  it will be elliptical.
 *  This could be composed from others, but here it is more compact. */

public class RoundProfile implements VignetteProfile
{
//==============================================================================

  @Override
  public float getVignetteEffect(int x, int y, int width, int height)
  {
    final float SQUARE_DIAGONAL_LENGTH = 2f;

    float horizontalDistance = getUnitDistance(x, width);
    float verticalDistance   = getUnitDistance(y, height);
    float square             = horizontalDistance * horizontalDistance +
                               verticalDistance * verticalDistance;
    return square / SQUARE_DIAGONAL_LENGTH;
  }

//------------------------------------------------------------------------------

  /* Returns 1 if position is 0 or max, 0 if it is max / 2, and intermediate
   * linear values in-between. */

  private float getUnitDistance(int position, int max)
  {
    float half     = max / 2;
    float distance = Math.abs(position - half);
    return distance / half;
  }

//------------------------------------------------------------------------------

}
