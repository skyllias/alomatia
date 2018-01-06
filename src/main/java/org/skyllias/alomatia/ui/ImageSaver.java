
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.io.*;

/** Persister of images. The most natural storage is {@link File}, but
 *  implementations may choose any other type.
 *  ImageSavers take care of everything, including error handling and user
 *  interaction.
 *  This interface has no direct relation to UI and therefore it could be in a
 *  "save" package. However, all the classes that depend on it are in the ui
 *  package, so...
 *  It could be more abstracted, serving for the copy into clipboard
 *  functionality too, but initially it does not seem to pay off. */

public interface ImageSaver
{
  /** Saves the passed image, optionally using nameHint in the operation. When
   *  working with files, for example, the hint may be used to generate the file
   *  name. If silently, user interaction should be reduced to the maximum; else,
   *  the user may (it's up to the implementation whether it is really done and
   *  how) be requested to interact somehow.
   *  Other properties (like directory, image format, how errors are handled,
   *  etc.) are decided by implementations. */

  void save(Image image, String nameHint, boolean silently);
}
