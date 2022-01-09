
package org.skyllias.alomatia.save;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

/** Saver of images to files. */

@Component
public class FileSaver
{
  private static final String IMAGE_FORMAT = "PNG";

//==============================================================================

  /** Stores the image in destinationFile or throws an exception.
   *  The format is fixed to PNG.
   *  Unfortunately, the IOException hierarchy does not allow for surefire
   *  distinction of causes. */

  public void save(Image image, File destinationFile) throws IOException
  {
    RenderedImage renderedImage = getRenderedImage(image);
    ImageIO.write(renderedImage, IMAGE_FORMAT, destinationFile);
  }

//------------------------------------------------------------------------------

  /* Returns a RenderedImage with the contents of image. */

  private RenderedImage getRenderedImage(Image image)
  {
    if (image instanceof RenderedImage) return (RenderedImage) image;           // filtered images will most likely be already BufferedImages

    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                                                    image.getHeight(null),
                                                    BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics         = bufferedImage.createGraphics();
    graphics.drawImage(image, 0, 0, null);
    graphics.dispose();
    return bufferedImage;
  }

//------------------------------------------------------------------------------

}
