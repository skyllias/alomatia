
package org.skyllias.alomatia.ui.start;

/** Opener of the windows that the user expects to see when the application is
 *  launched. There is a different implementation for each frame policy. */

public interface StartupWindowOpener
{
  /** Shows the windows that make up the application on startup.
   *  It must be invoked from the event dispatch thread. */

  void openStartupWindows();
}
