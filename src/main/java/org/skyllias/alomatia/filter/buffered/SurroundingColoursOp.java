
package org.skyllias.alomatia.filter.buffered;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.util.concurrent.*;

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
    int imageWidth      = src.getWidth();
    int imageHeight     = src.getHeight();
    Color[] colourCache = getAllPixels(src);

    RecursiveColoursAction processAction = new RecursiveColoursAction(dest, colourCache, 0,
                                                                      imageWidth, 0, imageHeight);
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

  /* Fork/join task that recursively splits the image whose colours are to be
   * processed into halves, until they are 100x100 or smaller. Then, the
   * fragments are scanned, retrieving all the surrounding colours of each
   * pixel (which may or may not lie inside the current fragment) and using
   * calculator to set the colour in the destination image. */

  @SuppressWarnings("serial")
  private class RecursiveColoursAction extends RecursiveAction
  {
    private BufferedImage dest;
    private Color[] srcColours;
    private int xFrom, xTo, yFrom, yTo;

    /* Creates a task that will process the pixels inside the rectangle
     * between (xFrom, yFrom) (inclusive) and (xTo, yTo) (exclusive). */

    public RecursiveColoursAction(BufferedImage destImage, Color[] srcColours,
                                  int xFrom, int xTo, int yFrom, int yTo)
    {
      this.dest       = destImage;
      this.srcColours = srcColours;
      this.xFrom      = xFrom;
      this.xTo        = xTo;
      this.yFrom      = yFrom;
      this.yTo        = yTo;
    }

    /* First, the image is split horizontally until stripes of 100 or less pixels
     * are obtained; afterwards, it is split vertically with the same criteria.
     * If the task's fragment is small enough, the real computation is carried out. */

    @Override
    protected void compute()
    {
      final int MAX_SIZE = 100;

      if (xTo - xFrom > MAX_SIZE)
      {
        final int halfWay = (xTo - xFrom) / 2;
        RecursiveColoursAction leftAction  = new RecursiveColoursAction(dest, srcColours, xFrom, xFrom + halfWay, yFrom, yTo);
        RecursiveColoursAction rightAction = new RecursiveColoursAction(dest, srcColours, xFrom + halfWay, xTo, yFrom, yTo);
        invokeAll(leftAction, rightAction);
      }
      else if (yTo - yFrom > MAX_SIZE)
      {
        final int halfWay = (yTo - yFrom) / 2;
        RecursiveColoursAction topAction    = new RecursiveColoursAction(dest, srcColours, xFrom, xTo, yFrom, yFrom + halfWay);
        RecursiveColoursAction bottomAction = new RecursiveColoursAction(dest, srcColours, xFrom, xTo, yFrom + halfWay, yTo);
        invokeAll(topAction, bottomAction);
      }
      else processPixels();
    }

    /* Processes the pixels inside the fragment, updating the destination image
     * according to the source colours. */

    private void processPixels()
    {
      for (int i = xFrom; i < xTo; i++)
      {
        for (int j = yFrom; j < yTo; j++)
        {
          Collection<Color> surroundingColours = getSurroundingColours(srcColours, i, j,
                                                                       dest.getWidth(), dest.getHeight());
          Color pickedColour                   = calculator.getColour(surroundingColours);
          dest.setRGB(i, j, pickedColour.getRGB());
        }
      }
    }
  }
}
