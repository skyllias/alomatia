
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.dnd.*;

import javax.swing.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.logo.*;
import org.skyllias.alomatia.source.*;
import org.skyllias.alomatia.ui.frame.*;

/** Logic for the main windows of the application.
 *  <p>
 *  Only one of these is expected in a given application. */

public class ControlFrame
{
  public static final int ICON_WIDTH  = 32;
  public static final int ICON_HEIGHT = 32;

  public static final Dimension DEFAULT_DESKTOP_SIZE = new Dimension(800, 600);

  private static final String CONTROL_TITLE = "control.window.title";
  private static final String DESKTOP_TITLE = "desktop.window.title";

//==============================================================================

  /** The catalogue is left untouched, although it is expected to have all the
   *  sources with the repeater set as display. If it contains a DropSource,
   *  this frame is added as drop target. */

  public ControlFrame(LabelLocalizer labelLocalizer, SourceCatalogue catalogue,
                      Repeater displayRepeater, FilterFactory filterFactory,
                      FramePolicy framePolicy)
  {
    FrameAdaptorFactory adaptorFactory;
    if (framePolicy.isUsingInternalFrames())
    {
      JDesktopPane desktopPane = createDesktopFrame(labelLocalizer);
      adaptorFactory           = new JInternalFrameAdaptorFactory(desktopPane);
    }
    else adaptorFactory = new JFrameAdaptorFactory();

    FileImageSaver imageSaver        = new FileImageSaver(labelLocalizer);
    DisplayFrameManager frameManager = new DisplayFrameManager(labelLocalizer,
                                                               filterFactory,
                                                               adaptorFactory,
                                                               imageSaver);

    createControlsFrame(labelLocalizer, catalogue, displayRepeater,
                        filterFactory, frameManager, framePolicy, imageSaver);
  }

//==============================================================================

  /* Shows a non resizable, packed frame with the source and window controls. */

  private void createControlsFrame(LabelLocalizer labelLocalizer, SourceCatalogue catalogue,
                                   Repeater displayRepeater, FilterFactory filterFactory,
                                   DisplayFrameManager frameManager, FramePolicy framePolicy,
                                   FileImageSaver imageSaver)
  {
    JFrame frame = getNewFrame();
    frame.setTitle(labelLocalizer.getString(CONTROL_TITLE));

    DropTargetListener dropListener = catalogue.get(DropSource.class);          // if not present it will be null
    if (dropListener != null) new DropTarget(frame, dropListener);

    ControlsPane controlsPane = new ControlsPane(labelLocalizer, catalogue,
                                                 displayRepeater, dropListener,
                                                 frameManager, framePolicy);
    frame.getContentPane().add(controlsPane, BorderLayout.CENTER);

    frame.setExtendedState(Frame.NORMAL);                                       // this is forced because some desktop managers maximize all windows by default, and this looks better if really packed
    frame.pack();
    frame.setResizable(false);
    frame.setVisible(true);                                                     // do this at the end, when the size has been fixed
  }

//------------------------------------------------------------------------------

  /* Shows a resizable frame with a JDesktopPane inside, which is returned. */

  private JDesktopPane createDesktopFrame(LabelLocalizer labelLocalizer)
  {
    JFrame frame = getNewFrame();
    frame.setTitle(labelLocalizer.getString(DESKTOP_TITLE));

    JDesktopPane desktopPane = new JDesktopPane();
    frame.getContentPane().add(desktopPane, BorderLayout.CENTER);

    frame.setSize(DEFAULT_DESKTOP_SIZE.width, DEFAULT_DESKTOP_SIZE.height);
    frame.setVisible(true);                                                     // do this at the end, when the size has been fixed

    return desktopPane;
  }

//------------------------------------------------------------------------------

  /* Returns an invisible frame with a logo already set. */

  private JFrame getNewFrame()
  {
    JFrame frame = new JFrame();
    frame.setIconImage(getDefaultLogo());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    return frame;
  }

//------------------------------------------------------------------------------

  /* Returns the logo used in "normal" application windows. */

  private Image getDefaultLogo()
  {
    return new LogoProducer().createImage(ICON_WIDTH, ICON_HEIGHT);             // dynamically generated every time instead of reading it from file or even caching it: it is not such a big overhead
  }

//------------------------------------------------------------------------------

}
