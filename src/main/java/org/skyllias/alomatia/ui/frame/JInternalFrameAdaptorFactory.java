
package org.skyllias.alomatia.ui.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

import org.skyllias.alomatia.dependency.Profiles;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** {@link FrameAdaptorFactory} that returns {@link JInternalFrame}s inside a
 *  {@link JDesktopPane} added to the main application window. */

@Component
@Profile(Profiles.INTERNAL_WINDOWS)
public class JInternalFrameAdaptorFactory implements FrameAdaptorFactory
{
  private static final Dimension DEFAULT_SIZE = new Dimension(600, 400);

  private final MainApplicationFrameSupplier mainApplicationFrameSupplier;

  private JDesktopPane desktopPane;

//==============================================================================

  public JInternalFrameAdaptorFactory(LabelLocalizer labelLocalizer,
                                      MainApplicationFrameSupplier mainApplicationFrameSupplier)
  {
    this.mainApplicationFrameSupplier = mainApplicationFrameSupplier;
  }

//==============================================================================

  @Override
  public FrameAdaptor getNewFrame(JComponent displayPanel)
  {
    if (desktopPane == null) desktopPane = createDesktopPane();

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

  /* Creates and returns a JDesktopPane inside the main application frame,
   * which is also tuned. */

  private JDesktopPane createDesktopPane()
  {
    JFrame mainApplicationFrame = mainApplicationFrameSupplier.getMainFrame();

    JDesktopPane desktopPane = new JDesktopPane();
    mainApplicationFrame.getContentPane().add(desktopPane, BorderLayout.CENTER);

    mainApplicationFrame.setSize(DEFAULT_SIZE.width, DEFAULT_SIZE.height);
    mainApplicationFrame.setVisible(true);                                      // do this at the end, when the size has been fixed

    return desktopPane;
  }

//------------------------------------------------------------------------------

  /* Returns a random point inside the rectangle defined by (0, 0) and
   * (desktopPane.getWidth() - DEFAULT_SIZE.width,
   * desktopPane.getHeight() - DEFAULT_SIZE.height). */

  private Point getRandomLocation()
  {
    int maxX = desktopPane.getWidth() - DEFAULT_SIZE.width;
    int maxY = desktopPane.getHeight() - DEFAULT_SIZE.height;
    if (maxX < 1) maxX = 1;
    if (maxY < 1) maxY = 1;

    Random randomGenerator = new Random();
    return new Point(randomGenerator.nextInt(maxX),
                     randomGenerator.nextInt(maxY));
  }

//------------------------------------------------------------------------------

}
