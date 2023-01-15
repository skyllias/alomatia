
package org.skyllias.alomatia.source;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.preferences.SourcePreferences;
import org.springframework.stereotype.Component;

/** Source from a fixed file in disk. */

@Component
public class SingleFileSource implements ImageSource
{
  private final ImageDisplay imageDisplay;
  private final SourcePreferences sourcePreferences;

  private final State state = new State();

//==============================================================================

  public SingleFileSource(ImageDisplay imageDisplay,
                          SourcePreferences sourcePreferences)
  {
    this.imageDisplay      = imageDisplay;
    this.sourcePreferences = sourcePreferences;

    initSourceFile();
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

  public Optional<File> getSourceFile() {return Optional.ofNullable(state.sourceFile);}

//------------------------------------------------------------------------------

  /** Sets the file whose contents are to be used as source image, and tries
   *  and displays it. */

  public void setFileSource(File imageFile)
  {
    state.sourceFile = imageFile;

    setImageFromFile(state.sourceFile);

    sourcePreferences.setDefaultFilePath(state.sourceFile.getAbsolutePath());
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

  /* Initializes sourceFile from the preferences. */

  private void initSourceFile()
  {
    String defaultFilePath = sourcePreferences.getDefaultFilePath();
    if (defaultFilePath != null) state.sourceFile = new File(defaultFilePath);
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private static class State
  {
    boolean active;
    File sourceFile;
  }

}
