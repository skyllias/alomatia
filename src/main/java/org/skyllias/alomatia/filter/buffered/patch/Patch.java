
package org.skyllias.alomatia.filter.buffered.patch;

import java.awt.Point;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

/** Contiguous collection of pixels from an image.
 *  A patch can contain zero, one, or more pixels. In the latter case, all of
 *  them must have at least another pixel located immediately above, below, left
 *  or right. Diagonal pixels are not considered adjacent.
 *  A pixel must only appear once in the patch. */

public class Patch
{
  private final Collection<Pixel> pixels = new LinkedList<>();                  // this does not ensure uniqueness, but will be much faster than a Set. Although it is a bad design practice, it can be assumed that the code generating the patches is unlikely to add a pixel twice

//==============================================================================

  public void add(Pixel pixel) {pixels.add(pixel);}

//------------------------------------------------------------------------------

  public Collection<Pixel> getPixels() {return Collections.unmodifiableCollection(pixels);}

//------------------------------------------------------------------------------

  /** Calculates the (approximate) center of all pixels.
   *  The returned Point may not belong to any Pixel in this Patch. */

  public Point getCentralPoint()
  {
    float totalX = 0;
    float totalY = 0;
    for (Pixel currentPixel : pixels)
    {
      totalX += currentPixel.getCoordinates().x;
      totalY += currentPixel.getCoordinates().y;
    }

    int averageX = Math.round(totalX / pixels.size());
    int averageY = Math.round(totalY / pixels.size());
    return new Point(averageX, averageY);
  }

//------------------------------------------------------------------------------

}
