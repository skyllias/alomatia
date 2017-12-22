
package org.skyllias.alomatia.ui.frame;

import java.awt.*;

import javax.swing.*;

import org.skyllias.alomatia.ui.*;

/** Abstraction of a frame (window) containing a {@link DisplayPanel}, so that
 *  the management logic can be independent on how frames are implemented.
 *  Implementations are expected to be as thin as possible, working as wrappers
 *  around some Swing components. They can choose whether the frame is really a
 *  {@link JFrame} or a {@link JInternalFrame}. */

public interface DisplayFrame
{
  /** Sets the title of the frame without further modification. */

  void setTitle(String title);

  /** Sets the icon of the frame without further modification. */

  void setIcon(Image icon);

  /** Moves the top left corner of the frame to the passed coordinates of the screen. */

  void setLocation(int x, int y);

  /** Resizes the frame to the passed dimensions. */

  void setSize(int width, int height);

  /** Changes the extended state of the frame. */

  void setMaximized(boolean maximized);

  /** Returns the InputMap associated to the underlying component for some condition. */

  InputMap getInputMap();

  /** Returns the ActionMap associated to the underlying component. */

  ActionMap getActionMap();

  /** Returns the {@link Frame} that owns the underlying component, which may be
   *  the frame itself or something else. */

  Frame getOwnerFrame();

  /** Registers listener so that it is notified when the underlying frame is
   *  being closed. */

  void addClosingFrameListener(ClosingFrameListener listener);

  /** Closes the frame, releasing all of its associated resources. */

  void dispose();
}
