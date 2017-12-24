
package org.skyllias.alomatia.ui.frame;

import java.awt.*;

import javax.swing.*;

import org.skyllias.alomatia.ui.*;

/** {@link FrameAdaptorFactory} that returns {@link JInternalFrame}s inside a
 *  given {@link JDesktopPane}. */

public class JInternalFrameAdaptorFactory implements FrameAdaptorFactory
{
  private JDesktopPane desktopPane;

//==============================================================================

  public JInternalFrameAdaptorFactory(JDesktopPane desktop) {desktopPane = desktop;}

//==============================================================================

  @Override
  public FrameAdaptor getNewFrame(DisplayPanel displayPanel)
  {
    JInternalFrame jInternalFrame = new JInternalFrame();
    jInternalFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    jInternalFrame.getContentPane().add(displayPanel, BorderLayout.CENTER);
    jInternalFrame.setClosable(true);
    jInternalFrame.setMaximizable(true);
    jInternalFrame.setResizable(true);
    desktopPane.add(jInternalFrame);

    return new JInternalFrameAdaptor(jInternalFrame);
  }

//------------------------------------------------------------------------------

  /** Returns the inner bounds of the desktop pane. */

  @Override
  public Rectangle getRearrengementBounds()
  {
    return new Rectangle(0, 0, desktopPane.getWidth(), desktopPane.getHeight());
  }

//------------------------------------------------------------------------------

}
