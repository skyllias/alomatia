
package org.skyllias.alomatia.filter.buffered.simple;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;

/** {@link BufferedImageOperation} that paints a semitransparent rectangle of
 *  certain colour in front of the source image. */

public class DyeOperation implements BufferedImageOperation
{
  private final Color dye;

//==============================================================================

  /** Creates a filter that will paint the source image with the passed colour,
   *  which should already have an alpha channel. */

  public DyeOperation(Color semitransparentColor) {dye = semitransparentColor;}

//------------------------------------------------------------------------------

  /** Creates a filter that will paint the source image with the passed colour,
   *  overriding its alpha channel with the passed value. */

  public DyeOperation(Color color, float alpha)
  {
    final int MAX_INT = 255;

    int intAlpha = (int) (alpha * MAX_INT);
    dye          = new Color(color.getRed(), color.getGreen(), color.getBlue(), intAlpha);
  }

//==============================================================================

  /** Copies src and fills a rectangle of the inner dye all over it. */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    Graphics2D graphics = outputImage.createGraphics();
    graphics.drawImage(inputImage, 0, 0, null);
    graphics.setColor(dye);
    graphics.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());

    graphics.dispose();
  }

//------------------------------------------------------------------------------

}
