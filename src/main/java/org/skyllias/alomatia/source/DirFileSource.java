
package org.skyllias.alomatia.source;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.io.filefilter.SuffixFileFilter;

/** Source from a directory.
 *  <p>
 *  All files with supported extensions inside a given directory are read and
 *  shown one at a time. They can be cyclically browsed by means of the methods
 *  {@link #nextImageFile()} and {@link #previousImageFile()}. */

public class DirFileSource extends BasicFileSource
{
  private File[] sourceDirContents = new File[0];                               // never null
  private int currentFileIndex;

//==============================================================================

  /** Sets the directory from which image files are to be read.
   *  <p>
   *  If imageDir is a directory and contains at least one file with an extension
   *  regarded as belonging to an image (determined by the system-dependent call
   *  {@link #ImageIO.getReaderFileSuffixes()}), the files are sorted alphabetically
   *  and the first one is displayed. The others are available for browsing by
   *  means of {@link #nextImageFile()} and {@link #previousImageFile()}. */

  @Override
  public void setFileSource(File imageDir)
  {
    if (imageDir != null)
    {
      FilenameFilter imageFilter = new SuffixFileFilter(ImageIO.getReaderFileSuffixes());
      sourceDirContents          = imageDir.listFiles(imageFilter);               // null if not a directory
      if (sourceDirContents == null) sourceDirContents = new File[0];
      if (sourceDirContents.length > 0)
      {
        Arrays.sort(sourceDirContents);                                           // File is Comparable (lexicographically by default)
        currentFileIndex = 0;
        setCurrentImageFile();
      }
    }
  }

//------------------------------------------------------------------------------

  /** If active, shows the image from the source file. */

  @Override
  public void setActive(boolean active)
  {
    super.setActive(active);
    if (active) setCurrentImageFile();
  }

//------------------------------------------------------------------------------

  /** Displays the next (alphabetically) image from the source dir, cycling to
   *  the beginning if the current one is the last.
   *  <p>
   *  Only if active. */

  public void nextImageFile()
  {
    if (isActive() && sourceDirContents.length > 0)
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
    if (isActive() && sourceDirContents.length > 0)
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

}
