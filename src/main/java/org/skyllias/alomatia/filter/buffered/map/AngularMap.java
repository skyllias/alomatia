
package org.skyllias.alomatia.filter.buffered.map;

/** HueMap that varies as the angle from (0, 0) to (x, y). */

public class AngularMap implements HueMap
{
//==============================================================================

  @Override
  public float getHue(int x, int y, int width, int height)
  {
    return ((float) height) / width + (float) Math.atan((1.0 + x) / (1.0 + y));
  }

//------------------------------------------------------------------------------

}
