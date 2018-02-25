
package org.skyllias.alomatia.ui.frame;

import java.awt.*;

import javax.swing.*;

/** Provider of new instances of {@link FrameAdaptor}. */

public interface FrameAdaptorFactory
{
  /** Returns a new window containing displayPanel and with the underlying frame
   *  not visible and set up so that it does not close when its close button
   *  is pressed (it only notifies its listeners). */

  FrameAdaptor getNewFrame(JComponent displayPanel);

  /** Returns the bounds available for frames obtained from this factory to
   *  rearrange (ie, relocate and resize). */

  Rectangle getRearrengementBounds();
}
