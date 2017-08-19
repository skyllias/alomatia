
package org.skyllias.alomatia;

import java.awt.*;

/** "Consumer" of images that may have to be filtered and displayed.
 *  <p>
 *  Every time that a change happens, either because the image contents are
 *  modified or because a new source is used, {@link} #setOriginalImage(Image)}
 *  should be invoked. */

public interface ImageDisplay
{
  /** Draws the passed image, either as is or modified by some filter. */

  void setOriginalImage(Image image);
}
