
package org.skyllias.alomatia.ui.controls;

import java.awt.Component;
import java.awt.Container;

/** Abstraction of a frame and a dialog, with the methods required to set them up.
 *  Unfortunately, some methods exist in both JFrame and JDialog but with
 *  different hierarchy. */

public interface ControlsWindow
{
  Component getComponent();

  void setTitle(String title);

  Container getContentPane();

  void pack();

  void setResizable(boolean resizable);

  void setVisible(boolean visible);
}
