
package org.skyllias.alomatia.filter.buffered;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/** {@link BufferedImageOp} that paints a semitransparent rectangle of certain
 *  colour in front of the source image. */

public class DyeOp extends BasicBufferedImageOp
{
  private Color dye;

//==============================================================================

  /** Creates a filter that will paint the source image with the passed colour,
   *  which should already have an alpha channel. */

  public DyeOp(Color semitransparentColor) {dye = semitransparentColor;}

//------------------------------------------------------------------------------

  /** Creates a filter that will paint the source image with the passed colour,
   *  overriding its alpha channel with the passed value. */

  public DyeOp(Color color, float alpha)
  {
    final int MAX_INT = 255;

    int intAlpha = (int) (alpha * MAX_INT);
    dye          = new Color(color.getRed(), color.getGreen(), color.getBlue(), intAlpha);
  }

//==============================================================================

  /** Copies src and fills a rectangle of the inner dye all over it. */

  @Override
  public void doFilter(BufferedImage src, BufferedImage dest)
  {
    Graphics2D graphics = dest.createGraphics();
    graphics.drawImage(src, 0, 0, null);
    graphics.setColor(dye);
    graphics.fillRect(0, 0, dest.getWidth(), dest.getHeight());

    graphics.dispose();
  }

//------------------------------------------------------------------------------

}
