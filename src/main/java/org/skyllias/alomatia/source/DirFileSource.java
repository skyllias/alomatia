
package org.skyllias.alomatia.source;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.springframework.stereotype.Component;

/** Source from a directory.
 *  <p>
 *  All files with supported extensions inside a given directory are read and
 *  shown one at a time. They can be cyclically browsed by means of the methods
 *  {@link #nextImageFile()} and {@link #previousImageFile()}.
 *  TODO skip files that disappear or cannot be opened. */

@Component
public class DirFileSource implements ImageSource
{
  private final ImageDisplay imageDisplay;

  private final State state = new State();

//==============================================================================

  public DirFileSource(ImageDisplay imageDisplay)
  {
    this.imageDisplay = imageDisplay;
  }

//==============================================================================

  /** Sets the directory from which image files are to be read.
   *  <p>
   *  If imageDir is a directory and contains at least one file with an extension
   *  regarded as belonging to an image (determined by the system-dependent call
   *  {@link #ImageIO.getReaderFileSuffixes()}), the files are sorted alphabetically
   *  and the first one is displayed. The others are available for browsing by
   *  means of {@link #nextImageFile()} and {@link #previousImageFile()}. */

  public void setFileSource(File imageDir)
  {
    if (imageDir != null)
    {
      FilenameFilter imageFilter = new SuffixFileFilter(ImageIO.getReaderFileSuffixes());
      state.sourceDirContents    = imageDir.listFiles(imageFilter);             // null if not a directory
      if (state.sourceDirContents == null) state.sourceDirContents = new File[0];
      if (state.sourceDirContents.length > 0)
      {
        Arrays.sort(state.sourceDirContents);                                   // File is Comparable (lexicographically by default)
        state.currentFileIndex = 0;
        setCurrentImageFile();
      }
    }
  }

//------------------------------------------------------------------------------

  /** If active, shows the image from the source file. */

  @Override
  public void setActive(boolean active)
  {
    state.active = active;

    if (active) setCurrentImageFile();
  }

//------------------------------------------------------------------------------

  /** Displays the next (alphabetically) image from the source dir, cycling to
   *  the beginning if the current one is the last.
   *  <p>
   *  Only if active. */

  public void nextImageFile()
  {
    if (state.active && state.sourceDirContents.length > 0)
    {
      state.currentFileIndex++;
      if (state.currentFileIndex >= state.sourceDirContents.length) state.currentFileIndex = 0;
      setCurrentImageFile();
    }
  }

//------------------------------------------------------------------------------

  /** Displays the previous (alphabetically) image from the source dir, cycling to
   *  the beginning if the current one is the last.
   *  <p>
   *  Only if active. */

  public void previousImageFile()
  {
    if (state.active && state.sourceDirContents.length > 0)
    {
      state.currentFileIndex--;
      if (state.currentFileIndex < 0) state.currentFileIndex = state.sourceDirContents.length - 1;
      setCurrentImageFile();
    }
  }

//------------------------------------------------------------------------------

  /* Tries and displays the currentFileIndex-th file from sourceDirContents. */

  private void setCurrentImageFile()
  {
    if (state.sourceDirContents.length > state.currentFileIndex)
      setImageFromFile(state.sourceDirContents[state.currentFileIndex]);
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
    File[] sourceDirContents = new File[0];                                     // never null
    int currentFileIndex;
  }

}
