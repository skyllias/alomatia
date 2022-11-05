
package org.skyllias.alomatia.source;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.preferences.SourcePreferences;
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
  private final SourcePreferences sourcePreferences;

  private boolean active;
  private File currentDir;
  private File[] sourceDirContents = new File[0];                               // never null
  private int currentFileIndex;

//==============================================================================

  public DirFileSource(ImageDisplay imageDisplay,
                       SourcePreferences sourcePreferences)
  {
    this.imageDisplay      = imageDisplay;
    this.sourcePreferences = sourcePreferences;

    initCurrentDir();
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
      sourceDirContents          = imageDir.listFiles(imageFilter);             // null if not a directory
      if (sourceDirContents == null) sourceDirContents = new File[0];
      if (sourceDirContents.length > 0)
      {
        Arrays.sort(sourceDirContents);                                         // File is Comparable (lexicographically by default)
        currentFileIndex = 0;
        setCurrentImageFile();
      }

      currentDir = imageDir;
      sourcePreferences.setDefaultDirPath(imageDir.getAbsolutePath());
    }
  }

//------------------------------------------------------------------------------

  public Optional<File> getCurrentDir() {return Optional.ofNullable(currentDir);}

//------------------------------------------------------------------------------

  /** If active, shows the image from the source file. */

  @Override
  public void setActive(boolean active)
  {
    this.active = active;

    if (active) setCurrentImageFile();
  }

//------------------------------------------------------------------------------

  @Override
  public void setDisplay(ImageDisplay display) {}

//------------------------------------------------------------------------------

  /** Displays the next (alphabetically) image from the source dir, cycling to
   *  the beginning if the current one is the last.
   *  <p>
   *  Only if active. */

  public void nextImageFile()
  {
    if (active && sourceDirContents.length > 0)
    {
      currentFileIndex++;
      if (currentFileIndex >= sourceDirContents.length) currentFileIndex = 0;
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
    if (active && sourceDirContents.length > 0)
    {
      currentFileIndex--;
      if (currentFileIndex < 0) currentFileIndex = sourceDirContents.length - 1;
      setCurrentImageFile();
    }
  }

//------------------------------------------------------------------------------

  /* Tries and displays the currentFileIndex-th file from sourceDirContents. */

  private void setCurrentImageFile()
  {
    if (sourceDirContents.length > currentFileIndex)
      setImageFromFile(sourceDirContents[currentFileIndex]);
  }

//------------------------------------------------------------------------------

  /* If source is active and the passed file is not null and contains an image,
   * it is sent to the display. Else, nothing happens. */

  private void setImageFromFile(File imageFile)
  {
    if (active && imageFile != null)
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

  /* Initializes currentDir from the preferences. */

  private void initCurrentDir()
  {
    String defaultDirPath = sourcePreferences.getDefaultDirPath();
    if (defaultDirPath != null)
    {
      currentDir = new File(defaultDirPath);
      setFileSource(currentDir);
    }
  }

//------------------------------------------------------------------------------

}
