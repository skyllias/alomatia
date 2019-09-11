
package org.skyllias.alomatia.ui.frame;

import java.awt.Frame;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;

/** {@link FrameAdaptor} that wraps a {@link JFrame} (ie, an OS window) to
 *  display filtered images. */

public class JFrameAdaptor implements FrameAdaptor
{
  private JFrame jFrame;

//==============================================================================

  public JFrameAdaptor(JFrame underlyingFrame) {jFrame = underlyingFrame;}

//==============================================================================

  @Override
  public void setTitle(String title) {jFrame.setTitle(title);}

  @Override
  public void setIcon(Image icon) {jFrame.setIconImage(icon);}

  @Override
  public void setLocation(int x, int y) {jFrame.setLocation(x, y);}

  @Override
  public void setSize(int width, int height) {jFrame.setSize(width, height);}

  @Override
  public void setMaximized(boolean maximized) {jFrame.setExtendedState(maximized? Frame.MAXIMIZED_BOTH: Frame.NORMAL);}

  @Override
  public void setVisible(boolean visible) {jFrame.setVisible(visible);}

//------------------------------------------------------------------------------

  @Override
  public InputMap getInputMap() {return jFrame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);}

  @Override
  public ActionMap getActionMap() {return jFrame.getRootPane().getActionMap();}

  @Override
  public Frame getOwnerFrame() {return jFrame;}

//------------------------------------------------------------------------------

  @Override
  public void addClosingFrameListener(final ClosingFrameListener listener)
  {
    jFrame.addWindowListener(new WindowListener()
    {
      @Override
      public void windowClosing(WindowEvent e)
      {
        listener.onClosingFrame(JFrameAdaptor.this);
      }

      @Override
      public void windowActivated(WindowEvent arg0) {}

      @Override
      public void windowClosed(WindowEvent arg0) {}

      @Override
      public void windowDeactivated(WindowEvent e) {}

      @Override
      public void windowDeiconified(WindowEvent e) {}

      @Override
      public void windowIconified(WindowEvent e) {}

      @Override
      public void windowOpened(WindowEvent e) {}
    });
  }

//------------------------------------------------------------------------------

  @Override
  public void dispose() {jFrame.dispose();}

//------------------------------------------------------------------------------

}
