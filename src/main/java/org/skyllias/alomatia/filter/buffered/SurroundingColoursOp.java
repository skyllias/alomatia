
package org.skyllias.alomatia.filter.buffered;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.concurrent.*;

import org.skyllias.alomatia.filter.buffered.parallelization.*;

/** {@link BufferedImageOp} that replaces the colour in each pixel with another
 *  one derived from all the pixels in the box surrounding it.
 *  The alpha channel info is erased.
 *  The implementation is only partially optimized. Therefore, filters using this
 *  may be slow. */

public class SurroundingColoursOp extends BasicBufferedImageOp
{
  private int boxSize;
  private SurroundingColoursCalculator calculator;

//==============================================================================

  /** Creates an operator that will use the colour returned by calculator from
   *  the pixels in a square box defined by ([i - boxSize, i + boxSize],
   *  [i - boxSize, i + boxSize]) around the pixel at (i, j).
   *  Of course, boxSize should be positive. */

  public SurroundingColoursOp(int boxSize, SurroundingColoursCalculator calculator)
  {
    this.boxSize    = boxSize;
    this.calculator = calculator;
  }

//==============================================================================

  /** Loops over each pixel in src, gets its surrounding pixels, and writes the
   *  resulting pixel calculated by the {@link SurroundingColoursCalculator} in
   *  dest or a new image if null.
   *  The work is parallelized to take advantage of multiple processors with a
   *  fork/join strategy. */

  @Override
  public void doFilter(BufferedImage src, BufferedImage dest)
  {
    Color[] colourCache = getAllPixels(src);

    SurroundingColoursProcessor imageProcessor = new SurroundingColoursProcessor(colourCache, dest);
    RecursiveAction processAction              = new RecursiveImageAction(imageProcessor);
    processAction.invoke();
  }

//------------------------------------------------------------------------------

  /* Returns all the colours in src arranged in rows, one after another.
   * This has a high cost in terms of memory, but runs fairly fast and avoids
   * a lot of conversions when retrieving the surrounding colours.
   * The corresponding cache when writing to the destination, on the other hand,
   * does not offer any advantage: there are a few ms of delay when using
   * setRGB(int, int, int, int, int[], int, int) instead of setRGB(int, int). */

  private Color[] getAllPixels(BufferedImage src)
  {
    int imageWidth      = src.getWidth();
    int imageHeight     = src.getHeight();
    Color[] colourCache = new Color[imageWidth * imageHeight];
    int[] srcPixels     = src.getRGB(0, 0, imageWidth, imageHeight, null, 0, imageWidth); // the last param, scansize, seems to have to be equal to the width from BufferedImage's source code
    for (int i = 0; i < srcPixels.length; i++) colourCache[i] = new Color(srcPixels[i], true);
    return colourCache;
  }

//------------------------------------------------------------------------------

  /* Returns the colours in the pixels of src inside the square ([x - boxSize, x + boxSize],
   *  [y - boxSize, y + boxSize]), caring about the image bounds. */

  private Collection<Color> getSurroundingColours(Color[] colours, int x, int y,
                                                  int width, int height)
  {
    int minX = Math.max(0, x - boxSize);
    int maxX = Math.min(width - 1, x + boxSize);                                // inclusive
    int minY = Math.max(0, y - boxSize);
    int maxY = Math.min(height - 1, y + boxSize);                               // inclusive

    Collection<Color> result = new LinkedList<>();
    for (int i = minX; i <= maxX; i++)
    {
      for (int j = minY; j <= maxY; j++)
      {
        result.add(colours[i + width * j]);
      }
    }
    return result;
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private class SurroundingColoursProcessor implements ImageProcessor
  {
    private Color[] srcColours;
    private BufferedImage destImage;

    public SurroundingColoursProcessor(Color[] srcColours, BufferedImage destImage)
    {
      this.srcColours = srcColours;
      this.destImage  = destImage;
    }

    @Override
    public int getImageWidth() {return destImage.getWidth();}

    @Override
    public int getImageHeight() {return destImage.getHeight();}

    @Override
    public void processPixel(int x, int y)
    {
      Collection<Color> surroundingColours = getSurroundingColours(srcColours, x, y,
                                                                   destImage.getWidth(), destImage.getHeight());
      Color pickedColour                   = calculator.getColour(surroundingColours);
      destImage.setRGB(x, y, pickedColour.getRGB());
    }
  }
}
