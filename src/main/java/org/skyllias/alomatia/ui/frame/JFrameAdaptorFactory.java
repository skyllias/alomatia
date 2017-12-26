
package org.skyllias.alomatia.ui.frame;

import java.awt.*;

import javax.swing.*;

import org.skyllias.alomatia.ui.*;

/** {@link FrameAdaptorFactory} that returns {@link JFrameAdaptor}s. */

public class JFrameAdaptorFactory implements FrameAdaptorFactory
{
  private static final Dimension DEFAULT_SIZE = new Dimension(600, 400);

//==============================================================================

  @Override
  public FrameAdaptor getNewFrame(DisplayPanel displayPanel)
  {
    JFrame jFrame = new JFrame();
    jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    jFrame.getContentPane().add(displayPanel, BorderLayout.CENTER);
    jFrame.setSize(DEFAULT_SIZE);                                               // some windows managers will use a 0 by 0 size if this is not forced

    return new JFrameAdaptor(jFrame);
  }

//------------------------------------------------------------------------------

  /** Returns a rectangle whose corners are on the corners of the screen where
   *  a new frame would be, substracting the system's taskbar, if any. */

  @Override
  public Rectangle getRearrengementBounds()
  {
    GraphicsConfiguration graphicsConfig = new JFrame().getGraphicsConfiguration();
    if (graphicsConfig == null) return null;

    Rectangle bounds    = graphicsConfig.getBounds();
    Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfig);

    Rectangle effectiveScreenArea = new Rectangle();
    effectiveScreenArea.x      = bounds.x + screenInsets.left;
    effectiveScreenArea.y      = bounds.y + screenInsets.top;
    effectiveScreenArea.height = bounds.height - screenInsets.top  - screenInsets.bottom;
    effectiveScreenArea.width  = bounds.width  - screenInsets.left - screenInsets.right;
    return effectiveScreenArea;
  }

//------------------------------------------------------------------------------

}
