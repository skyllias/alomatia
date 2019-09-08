
package org.skyllias.alomatia.filter.buffered.map;

/** HueMap that returns 0 at (0, 0) and increases with each dimension, yielding
 *  diagonal isochromatic lines. */

public class DiagonalMap implements HueMap
{
//==============================================================================

  @Override
  public float getHue(int x, int y, int width, int height)
  {
    return ((float) x) / width + ((float) y) / height;
  }

//------------------------------------------------------------------------------

}
