
package org.skyllias.alomatia.filter.buffered.layered;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;

/** {@link BufferedImageOperation} that simply copies the input image to the output.
 *  It only makes sense to be used in the lowest layer. */

public class NoOperation implements BufferedImageOperation
{
//==============================================================================

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    Graphics2D graphics = outputImage.createGraphics();
    graphics.drawImage(inputImage, 0, 0, null);
    graphics.dispose();
  }

//------------------------------------------------------------------------------

}
