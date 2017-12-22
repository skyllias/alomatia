
package org.skyllias.alomatia.ui.frame;

/** Listener to be notified when a {@link DisplayFrame} is being closed.
 *  All the other typical window events are irrelevant to this class. */

public interface ClosingFrameListener
{
  /** Invoked when the underlying frame of the monitored DisplayFrame is
   *  attempted to be closed (eg by clicking on some X-like button). A subsequent
   *  explicit call to its dispose() method may be necessary.
   *  No need for the affected frame is envisaged. */

  void onClosingFrame();
}
