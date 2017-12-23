
package org.skyllias.alomatia.ui.frame;

import org.skyllias.alomatia.ui.*;

/** Provider of new instances of {@link FrameAdaptor}. */

public interface FrameAdaptorFactory
{
  /** Returns a new window containing displayPanel and with the underlying frame
   *  not visible and set up so that it does not close when its close button
   *  is pressed (it only notifies its listeners). */

  FrameAdaptor getNewFrame(DisplayPanel displayPanel);
}
