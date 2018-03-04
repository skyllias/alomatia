
package org.skyllias.alomatia.ui.frame;

import java.awt.*;
import java.util.*;

import javax.swing.*;

/** {@link FrameAdaptorFactory} that returns {@link JInternalFrame}s inside a
 *  given {@link JDesktopPane}. */

public class JInternalFrameAdaptorFactory implements FrameAdaptorFactory
{
  private static final Dimension DEFAULT_SIZE = new Dimension(600, 400);

  private JDesktopPane desktopPane;

//==============================================================================

  public JInternalFrameAdaptorFactory(JDesktopPane desktop) {desktopPane = desktop;}

//==============================================================================

  @Override
  public FrameAdaptor getNewFrame(JComponent displayPanel)
  {
    JInternalFrame jInternalFrame = new JInternalFrame();
    jInternalFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    jInternalFrame.getContentPane().add(displayPanel, BorderLayout.CENTER);
    jInternalFrame.setClosable(true);
    jInternalFrame.setMaximizable(true);
    jInternalFrame.setResizable(true);

    jInternalFrame.setSize(DEFAULT_SIZE);
    jInternalFrame.setLocation(getRandomLocation());                            // this prevents all the internal frames from stacking

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

  /* Returns a random point inside the rectangle defined by (0, 0) and
   * (desktopPane.getWidth() - DEFAULT_SIZE.width, desktopPane.getHeight() - DEFAULT_SIZE.height). */

  private Point getRandomLocation()
  {
    int maxX = desktopPane.getWidth() - DEFAULT_SIZE.width;
    int maxY = desktopPane.getHeight() - DEFAULT_SIZE.height;
    if (maxX < 0) maxX = 0;
    if (maxY < 0) maxY = 0;

    Random randomGenerator = new Random();
    return new Point(randomGenerator.nextInt(maxX), randomGenerator.nextInt(maxY));
  }

//------------------------------------------------------------------------------

}
