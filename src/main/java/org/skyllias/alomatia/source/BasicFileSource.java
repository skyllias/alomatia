
package org.skyllias.alomatia.source;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/** Superclass for the images that produce images from a {@link File} (file or directory). */

public abstract class BasicFileSource extends BasicSource
{
//==============================================================================

  /** If source is active and the passed file is not null and contains an image,
   * it is sent to the display. Else, nothing happens. */

  protected void setImageFromFile(File imageFile)
  {
    if (isActive())
    {
      try
      {
        BufferedImage image = ImageIO.read(imageFile);                          // this returns null if an image cannot be read from the file
        if (image != null) sendImageToDisplay(image);
      }
      catch (Exception e) {e.printStackTrace();}                                // the file must be wrong, not critical. TODO log it
    }
  }

//------------------------------------------------------------------------------

  /** Sets the file to read images from.
   *  Subclasses decide what to do with it to finally invoke
   *  {@link #setImageFromFile(File)}. */

  public abstract void setFileSource(File imageFile);

//------------------------------------------------------------------------------

}
