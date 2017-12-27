
package org.skyllias.alomatia.filter.buffered;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

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
    for (int i = 0; i < src.getWidth(); i++)
    {
      for (int j = 0; j < src.getHeight(); j++)
      {
        Collection<Color> surroundingColours = getSurroundingColours(src, i, j);
        Color pickedColour                   = calculator.getColour(surroundingColours);
        dest.setRGB(i, j, pickedColour.getRGB());
      }
    }
  }

//------------------------------------------------------------------------------

  /* Returns the colours in the pixels of src inside the square ([x - boxSize, x + boxSize],
   *  [y - boxSize, y + boxSize]), caring about the image bounds. */

  private Collection<Color> getSurroundingColours(BufferedImage src, int x, int y)
  {
    int minX = Math.max(0, x - boxSize);
    int maxX = Math.min(src.getWidth() - 1, x + boxSize);
    int minY = Math.max(0, y - boxSize);
    int maxY = Math.min(src.getHeight() - 1, y + boxSize);

    int width    = maxX - minX + 1;
    int hegiht   = maxY - minY + 1;
    int[] pixels = src.getRGB(minX, minY, width, hegiht, null, 0, width);       // the last param, scansize, seems to have to be equal to the width from BufferedImage's source code
    Collection<Color> result = new LinkedList<>();
    for (int currentRgb : pixels) result.add(new Color(currentRgb, true));
    return result;
  }

//------------------------------------------------------------------------------

}
