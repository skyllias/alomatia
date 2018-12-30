
package org.skyllias.alomatia.filter.buffered.map;

/** HueMap that varies as the distance from (0, 0) to (x, y). */

public class RadialMap implements HueMap
{
//==============================================================================

  @Override
  public float getHue(int x, int y, int width, int height)
  {
    return (float) (Math.pow(((float) x) / (2 * height), 2) +
                    Math.pow(((float) y) / (2 * width), 2));
  }

//------------------------------------------------------------------------------

}
