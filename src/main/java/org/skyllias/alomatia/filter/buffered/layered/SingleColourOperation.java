
package org.skyllias.alomatia.filter.buffered.layered;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;

/** {@link BufferedImageOperation} that simply paints everything with a colour.
 *  It only makes sense to be used in the lowest layer. */

public class SingleColourOperation implements BufferedImageOperation
{
  private final Color colour;

//==============================================================================

  public SingleColourOperation(Color colour)
  {
    this.colour = colour;
  }

//==============================================================================

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    Graphics2D graphics = outputImage.createGraphics();
    graphics.setColor(colour);
    graphics.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
    graphics.dispose();
  }

//------------------------------------------------------------------------------

}
