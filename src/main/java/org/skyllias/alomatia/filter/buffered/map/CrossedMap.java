
package org.skyllias.alomatia.filter.buffered.map;

/** HueMap that varies linearly with each dimension but crosses width and height
 *  proportions, and shifts the hue at (0, 0) as a function of the image
 *  aspect ratio. */

public class CrossedMap implements HueMap
{
//==============================================================================

  @Override
  public float getHue(int x, int y, int width, int height)
  {
    return ((float) width) / height +
           ((float) x) / (2 * height) -
           ((float) y) / (2 * width);
  }

//------------------------------------------------------------------------------

}
