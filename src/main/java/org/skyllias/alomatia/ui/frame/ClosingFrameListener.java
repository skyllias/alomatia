
package org.skyllias.alomatia.ui.frame;

/** Listener to be notified when a {@link FrameAdaptor} is being closed.
 *  All the other typical window events are irrelevant to this class. */

public interface ClosingFrameListener
{
  /** Invoked when the frame's underlying frame is attempted to be closed (eg
   *  by clicking on some X-like button). A subsequent explicit call to frame's
   *  dispose() method may be necessary for the window to really close. */

  void onClosingFrame(FrameAdaptor frame);
}
