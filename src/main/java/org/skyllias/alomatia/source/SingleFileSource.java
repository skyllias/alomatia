
package org.skyllias.alomatia.source;

import java.io.File;

/** Source from a fixed file in disk. */

public class SingleFileSource extends BasicFileSource
{
  private File sourceFile;

//==============================================================================

  /** Sets the file whose contents are to be used as source image, and tries
   *  and displays it. */

  @Override
  public void setFileSource(File imageFile)
  {
    sourceFile = imageFile;
    setImageFromFile(sourceFile);
  }

//------------------------------------------------------------------------------

  /** If active, shows the image from the source file. */

  @Override
  public void setActive(boolean active)
  {
    super.setActive(active);
    if (active && sourceFile != null) setImageFromFile(sourceFile);
  }

//------------------------------------------------------------------------------

}
