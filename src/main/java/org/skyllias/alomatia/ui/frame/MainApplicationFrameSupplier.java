
package org.skyllias.alomatia.ui.frame;

import java.awt.Image;

import javax.swing.JFrame;

import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.logo.IconSupplier;
import org.springframework.stereotype.Component;

/** Supplier of the main frame of the application.
 *  Only one such frame is expected per application. */

@Component
public class MainApplicationFrameSupplier
{
  private static final String DESKTOP_TITLE = "desktop.window.title";

  private final LabelLocalizer labelLocalizer;
  private final IconSupplier iconSupplier;

  private JFrame mainFrame;

//==============================================================================

  public MainApplicationFrameSupplier(LabelLocalizer labelLocalizer,
                                      IconSupplier iconSupplier)
  {
    this.labelLocalizer = labelLocalizer;
    this.iconSupplier   = iconSupplier;
  }

//==============================================================================

  public JFrame getMainFrame()
  {
    if (mainFrame == null) mainFrame = buildNewFrame();

    return mainFrame;
  }

//------------------------------------------------------------------------------

  /* Returns an invisible frame with a logo and title already set. */

  private JFrame buildNewFrame()
  {
    JFrame frame = new JFrame();
    frame.setTitle(labelLocalizer.getString(DESKTOP_TITLE));
    frame.setIconImage(getDefaultLogo());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    return frame;
  }

//------------------------------------------------------------------------------

  /* Returns the logo used in "normal" application windows. */

  private Image getDefaultLogo()
  {
    return iconSupplier.getIcon();
  }

//------------------------------------------------------------------------------

}
