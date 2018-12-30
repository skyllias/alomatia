
package org.skyllias.alomatia.filter.buffered.patch;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

/** Generator of patches from images.
 *  Patch boundaries are decided according to colour similarity between adjacent
 *  pixels. One similar adjacent pixel is enough to be included into a patch.
 *  This may lead to larger patches and be less efficient, but makes the
 *  extraction independent from the way pixels are looped. */

public class SimilarPatchesFinder
{
  private final ColourSimilarity similarity;

//==============================================================================

  public SimilarPatchesFinder(ColourSimilarity similarity)
  {
    this.similarity = similarity;
  }

//==============================================================================

  /** Returns patches from image so that:
   *  - All have at least one pixel.
   *  - The whole image is covered.
   *  - There are no overlaps. */

  public Collection<Patch> findPatches(BufferedImage image)
  {
    Collection<Patch> patches    = new LinkedList<>();
    Set<Pixel> unassignedPixels  = getAllPixels(image);
    while (!unassignedPixels.isEmpty())
    {
      Pixel firstPixelForPatch = unassignedPixels.iterator().next();

      Set<Pixel> pendingPixels = new HashSet<>();
      pendingPixels.add(firstPixelForPatch);
      Patch patch = new Patch();
      transferSimilarPixels(image, patch, pendingPixels, unassignedPixels);

      patches.add(patch);
    }

    return patches;
  }

//------------------------------------------------------------------------------

  /* Returns one pixel for each in the image.
   * It could be parallelized. */

  private Set<Pixel> getAllPixels(BufferedImage image)
  {
    Set<Pixel> pixels = new HashSet<>();
    for (int i = 0; i < image.getWidth(); i++)
    {
      for (int j = 0; j < image.getHeight(); j++)
      {
        pixels.add(getPixelAt(new Point(i, j), image));
      }
    }
    return pixels;
  }

//------------------------------------------------------------------------------

  /* Iteratively takes pixels from pendingPixels, adds them to patch, removes
   * them from unassignedPixels, and adds to pendingPixels its neighbour pixels
   * from image if they are similar.
   * It ends when there are no more pending pixels.
   * It does not seem easily parallelizable. */

  private void transferSimilarPixels(BufferedImage image, Patch patch,
                                     Set<Pixel> pendingPixels,
                                     Set<Pixel> unassignedPixels)
  {
    while (!pendingPixels.isEmpty())
    {
      Pixel currentPendingPixel = pendingPixels.iterator().next();
      patch.add(currentPendingPixel);
      pendingPixels.remove(currentPendingPixel);
      unassignedPixels.remove(currentPendingPixel);

      Collection<Pixel> neighbours = getNeighbours(currentPendingPixel, image);
      for (Pixel currentNeighbour : neighbours)
      {
        if (areSimilar(currentPendingPixel, currentNeighbour) &&
            unassignedPixels.contains(currentNeighbour))
        {
          pendingPixels.add(currentNeighbour);
        }
      }
    }
  }

//------------------------------------------------------------------------------

  /* Returns the pixels up, down, left and right from the pixel.
   * If pixel is on the border, only three will be returned; if on a corner,
   * only teo. */

  private Collection<Pixel> getNeighbours(Pixel pixel, BufferedImage image)
  {
    Point point                  = pixel.getCoordinates();
    Collection<Pixel> neighbours = new LinkedList<>();

    if (point.x > 0) neighbours.add(getPixelAt(new Point(point.x - 1, point.y), image));
    if (point.y > 0) neighbours.add(getPixelAt(new Point(point.x, point.y - 1), image));

    if (point.x < image.getWidth() - 1)  neighbours.add(getPixelAt(new Point(point.x + 1, point.y), image));
    if (point.y < image.getHeight() - 1) neighbours.add(getPixelAt(new Point(point.x, point.y + 1), image));

    return neighbours;
  }

//------------------------------------------------------------------------------

  /* Returns a Pixel with the colour at the passed point. */

  private Pixel getPixelAt(Point point, BufferedImage image)
  {
    return new Pixel(point, new Color(image.getRGB(point.x, point.y)));
  }

//------------------------------------------------------------------------------

  /* Returns true if the colours of the two pixels are similar. */

  private boolean areSimilar(Pixel aPixel, Pixel anotherPixel)
  {
    return similarity.areSimilar(aPixel.getColour(), anotherPixel.getColour());
  }

//------------------------------------------------------------------------------

}
