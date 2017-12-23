
package org.skyllias.alomatia.ui.frame;

import org.skyllias.alomatia.ui.*;

/** Provider of new instances of {@link FrameAdaptor}. */

public interface FrameAdaptorFactory
{
  /** Returns a new window containing displayPanel and with the underlying frame
   *  already set up:
   *  - Not maximized and with some non-zero size.
   *  - Visible.
   *  - Not closing when its close button is pressed, only notifying its listeners. */

  FrameAdaptor getNewFrame(DisplayPanel displayPanel);
}
