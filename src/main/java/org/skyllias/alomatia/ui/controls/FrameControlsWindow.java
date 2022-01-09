package org.skyllias.alomatia.ui.controls;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.JFrame;

/** ControlsWindow implementation for a JFrame. */

public class FrameControlsWindow implements ControlsWindow
{
  private final JFrame frame;

//==============================================================================

  public FrameControlsWindow(JFrame frame)
  {
    this.frame = frame;

    frame.setExtendedState(Frame.NORMAL);                                       // this is forced because some desktop managers maximize all windows by default, and this looks better if really packed
  }

//==============================================================================

  @Override
  public Component getComponent() {return frame;}

  @Override
  public void setTitle(String title) {frame.setTitle(title);}

  @Override
  public Container getContentPane() {return frame.getContentPane();}

  @Override
  public void pack() {frame.pack();}

  @Override
  public void setResizable(boolean resizable) {frame.setResizable(resizable);}

  @Override
  public void setVisible(boolean visible) {frame.setVisible(visible);}

//------------------------------------------------------------------------------

}
