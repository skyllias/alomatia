
package org.skyllias.alomatia.filter.buffered.simple;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.LinkedList;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.surround.AverageChannelCalculator;

/** {@link BufferedImageOperation} that divides the original image in same-sized
 *  squares and paints all the pixels in each box with its average colour. */

public class PixelizerOperation implements BufferedImageOperation
{
  private final int boxSize;

//==============================================================================

  /** Creates a filter that will use square boxes of pixelSize pixels. It should
   *  be greater than 2. */

  public PixelizerOperation(int pixelSize) {boxSize = pixelSize;}

//==============================================================================

  /** Loops over all the resulting boxes and calculates their average colour,
   *  setting it to all its corresponding pixels in the destination. */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    Graphics2D destGraphics = outputImage.createGraphics();

    int xOffset = getTileOffset(inputImage.getWidth());
    int yOffset = getTileOffset(inputImage.getHeight());

    int amountOfHorizontalTiles = 1 + (inputImage.getWidth() - 1) / boxSize;
    int amountOfVerticalTiles   = 1 + (inputImage.getHeight() - 1) / boxSize;

    for (int i = 0; i < amountOfHorizontalTiles; i++)
    {
      for (int j = 0; j < amountOfVerticalTiles; j++)
      {
        filterTile(inputImage, destGraphics, i, j, xOffset, yOffset);
      }
    }
    destGraphics.dispose();
  }

//------------------------------------------------------------------------------

  /* Returns the offset that should be applied to tiles of boxSize pixels so
   * that they cover the whole size when put consecutively not covering the
   * edges with thin fragments.
   * The returned value is not negative but should be "substracted" to get the
   * starting position of tiles. */

  private int getTileOffset(int size)
  {
    int remainingPixels = size % boxSize;
    boolean isMaxCover  = remainingPixels == 0 || remainingPixels == boxSize-1;
    if (isMaxCover) return 0;
    else            return boxSize - (remainingPixels + boxSize) / 2;
  }

//------------------------------------------------------------------------------

  /* Averages the colours of the tile composed by the pixels between the coords
   * ((i * boxSize) - xOffset, (j * boxSize) - yOffset) inclusive and
   * ((i * boxSize) - xOffset + boxSize - 1, (j * boxSize) - yOffset + boxSize - 1)
   * inclusive, and paints the resulting colour over the same region in destGraphics.
   * Image edges are taken into account. */

  private void filterTile(BufferedImage src, Graphics2D destGraphics, int i, int j,
                          int xOffset, int yOffset)
  {
    int xStart = Math.max(0, (i * boxSize) - xOffset);
    int xEnd   = Math.min(src.getWidth(), (i * boxSize) - xOffset + boxSize);
    int yStart = Math.max(0, (j * boxSize) - yOffset);
    int yEnd   = Math.min(src.getHeight(), (j * boxSize) - yOffset + boxSize);

    Collection<Color> tileColours = new LinkedList<>();
    for (int x = xStart; x < xEnd; x++)
    {
      for (int y = yStart; y < yEnd; y++)
      {
        Color currentPixel = new Color(src.getRGB(x, y));
        tileColours.add(currentPixel);
      }
    }
    AverageChannelCalculator averageCalculator = new AverageChannelCalculator();
    Color averageColour                        = averageCalculator.getColour(tileColours);
    destGraphics.setColor(averageColour);
    destGraphics.fillRect(xStart, yStart, xEnd - xStart, yEnd - yStart);
  }

//------------------------------------------------------------------------------

}
