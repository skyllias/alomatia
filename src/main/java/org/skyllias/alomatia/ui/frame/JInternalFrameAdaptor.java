
package org.skyllias.alomatia.ui.frame;

import java.awt.*;
import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;

/** {@link FrameAdaptor} that wraps a {@link JInternalFrame} (ie, a light-weight
 *  Swing component inside a JDesktopPane) to display filtered images. */

public class JInternalFrameAdaptor implements FrameAdaptor
{
  private JInternalFrame jInternalFrame;

//==============================================================================

  public JInternalFrameAdaptor(JInternalFrame underlyingFrame) {jInternalFrame = underlyingFrame;}

//==============================================================================

  @Override
  public void setTitle(String title) {jInternalFrame.setTitle(title);}

  @Override
  public void setIcon(Image icon) {jInternalFrame.setFrameIcon(new ImageIcon(icon));}

  @Override
  public void setLocation(int x, int y) {jInternalFrame.setLocation(x, y);}

  @Override
  public void setSize(int width, int height) {jInternalFrame.setSize(width, height);}

  @Override
  public void setMaximized(boolean maximized) {try {jInternalFrame.setMaximum(maximized);} catch (PropertyVetoException pve) {}}    // the esception should never happen

  @Override
  public void setVisible(boolean visible) {jInternalFrame.setVisible(visible);}

//------------------------------------------------------------------------------

  @Override
  public InputMap getInputMap() {return jInternalFrame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);}

  @Override
  public ActionMap getActionMap() {return jInternalFrame.getRootPane().getActionMap();}

  @Override
  public Frame getOwnerFrame() {return (Frame) SwingUtilities.getRoot(jInternalFrame);} // this catch should never fail...

//------------------------------------------------------------------------------

  @Override
  public void addClosingFrameListener(final ClosingFrameListener listener)
  {
    jInternalFrame.addInternalFrameListener(new InternalFrameListener()
    {
      @Override
      public void internalFrameClosing(InternalFrameEvent e)
      {
        listener.onClosingFrame(JInternalFrameAdaptor.this);
      }

      @Override
      public void internalFrameOpened(InternalFrameEvent e) {}

      @Override
      public void internalFrameIconified(InternalFrameEvent e) {}

      @Override
      public void internalFrameDeiconified(InternalFrameEvent e) {}

      @Override
      public void internalFrameDeactivated(InternalFrameEvent e) {}

      @Override
      public void internalFrameClosed(InternalFrameEvent e) {}

      @Override
      public void internalFrameActivated(InternalFrameEvent e) {}
    });
  }

//------------------------------------------------------------------------------

  @Override
  public void dispose() {jInternalFrame.dispose();}

//------------------------------------------------------------------------------

}
