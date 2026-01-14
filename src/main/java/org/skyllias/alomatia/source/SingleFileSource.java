
package org.skyllias.alomatia.source;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.springframework.stereotype.Component;

/** Source from a fixed file in disk. */

@Component
public class SingleFileSource implements ImageSource
{
  private final ImageDisplay imageDisplay;

  private final State state = new State();

//==============================================================================

  public SingleFileSource(ImageDisplay imageDisplay)
  {
    this.imageDisplay = imageDisplay;
  }

//==============================================================================

  /** If active, shows the image from the source file. */

  @Override
  public void setActive(boolean active)
  {
    state.active = active;
    if (active) setImageFromFile(state.sourceFile);
  }

//------------------------------------------------------------------------------

  /** Sets the file whose contents are to be used as source image, and tries
   *  and displays it. */

  public void setFileSource(File imageFile)
  {
    state.sourceFile = imageFile;

    setImageFromFile(state.sourceFile);
  }

//------------------------------------------------------------------------------

  /* If source is active and the passed file is not null and contains an image,
   * it is sent to the display. Else, nothing happens. */

  private void setImageFromFile(File imageFile)
  {
    if (state.active && imageFile != null)
    {
      try
      {
        BufferedImage image = ImageIO.read(imageFile);                          // this returns null if an image cannot be read from the file
        if (image != null) imageDisplay.setOriginalImage(image);
      }
      catch (Exception e) {e.printStackTrace();}                                // the file must be wrong, not critical. TODO log it
    }
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private static class State
  {
    boolean active;
    File sourceFile;
  }

}
