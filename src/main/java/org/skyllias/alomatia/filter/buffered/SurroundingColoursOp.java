
package org.skyllias.alomatia.filter.buffered;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import org.apache.commons.lang.time.*;

/** {@link BufferedImageOp} that replaces the colour in each pixel with another
 *  one derived from all the pixels in the box surrounding it.
 *  The alpha channel info is erased.
 *  The implementation is not particularly optimized. So, filters using this
 *  may be rather slow. */

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
   *  dest or a new image if null. */

  @Override
  public void doFilter(BufferedImage src, BufferedImage dest)
  {
System.out.println("Beginning SurroundingColoursOp.doFilter");
StopWatch cachingWatch = new StopWatch();
StopWatch fullFilterWatch = new StopWatch();
collectorWatch = new StopWatch();
collectorWatch.start();
collectorWatch.suspend();
calculatorWatch = new StopWatch();
calculatorWatch.start();
calculatorWatch.suspend();
medianChannelWatch = new StopWatch();
medianChannelWatch.start();
medianChannelWatch.suspend();
medianSortWatch = new StopWatch();
medianSortWatch.start();
medianSortWatch.suspend();
fullFilterWatch.start();

    int imageWidth      = src.getWidth();
    int imageHeight     = src.getHeight();
cachingWatch.start();
    Color[] colourCache = getPixels(src);
cachingWatch.stop();

    for (int i = 0; i < imageWidth; i++)
    {
      for (int j = 0; j < imageHeight; j++)
      {
        Collection<Color> surroundingColours = getSurroundingColours(colourCache, i, j,
                                                                     imageWidth, imageHeight);
calculatorWatch.resume();
        Color pickedColour                   = calculator.getColour(surroundingColours);
calculatorWatch.suspend();
        dest.setRGB(i, j, pickedColour.getRGB());
      }
    }
  }

//------------------------------------------------------------------------------

  /* Returns all the colours in src arranged in rows, one after another.
   * This has a high cost in terms of memory, but runs fairly fast and avoids
   * a lot of conversions when retrieving the surrounding colours. */

  private Color[] getPixels(BufferedImage src)
  {
    int imageWidth      = src.getWidth();
    int imageHeight     = src.getHeight();
    Color[] colourCache = new Color[imageWidth * imageHeight];
    int[] srcPixels     = src.getRGB(0, 0, imageWidth, imageHeight, null, 0, imageWidth); // the last param, scansize, seems to have to be equal to the width from BufferedImage's source code
    for (int i = 0; i < srcPixels.length; i++) colourCache[i] = new Color(srcPixels[i], true);
    return colourCache;
  }

//------------------------------------------------------------------------------

private StopWatch collectorWatch = new StopWatch();
private StopWatch calculatorWatch = new StopWatch();
public static StopWatch medianChannelWatch = new StopWatch();
public static StopWatch medianSortWatch = new StopWatch();

  /* Returns the colours in the pixels of src inside the square ([x - boxSize, x + boxSize],
   *  [y - boxSize, y + boxSize]), caring about the image bounds. */

  private Collection<Color> getSurroundingColours(Color[] colours, int x, int y,
                                                  int width, int height)
  {
collectorWatch.resume();
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
collectorWatch.suspend();
    return result;
  }

//------------------------------------------------------------------------------

}
