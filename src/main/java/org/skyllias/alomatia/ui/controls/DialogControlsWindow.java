
package org.skyllias.alomatia.ui.controls;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JDialog;

/** ControlsWindow implementation for a JDialog. */

public class DialogControlsWindow implements ControlsWindow
{
  private final JDialog dialog;

//==============================================================================

  public DialogControlsWindow(JDialog dialog)
  {
    this.dialog = dialog;
  }

//==============================================================================

  @Override
  public Component getComponent() {return dialog;}

  @Override
  public void setTitle(String title) {dialog.setTitle(title);}

  @Override
  public Container getContentPane() {return dialog.getContentPane();}

  @Override
  public void pack() {dialog.pack();}

  @Override
  public void setResizable(boolean resizable) {dialog.setResizable(resizable);}

  @Override
  public void setVisible(boolean visible) {dialog.setVisible(visible);}

//------------------------------------------------------------------------------

}
