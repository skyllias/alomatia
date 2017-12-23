
package org.skyllias.alomatia.ui;

import java.awt.*;

import javax.swing.*;

import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.logo.*;

/** Superclass for all application windows. */

@SuppressWarnings("serial")
public abstract class BasicAlomatiaWindow extends JFrame
{
  public static final int ICON_WIDTH  = 32;
  public static final int ICON_HEIGHT = 32;

  private LabelLocalizer labelLocalizer;

//==============================================================================

  /** Instantiates a new window with the icon already set and a title translated
   *  to the current locale. */

  protected BasicAlomatiaWindow(LabelLocalizer localizer, String titleKey)
  {
    super(localizer.getString(titleKey));

    labelLocalizer = localizer;

    setIconImage(getDefaultLogo());
  }

//==============================================================================

  /** Returns the localizer from which UI labels can be obtained. */

  protected LabelLocalizer getLabelLocalizer() {return labelLocalizer;}

//------------------------------------------------------------------------------

  /** Returns the logo used in "normal" application windows.
   *  Subclasses may use it to apply filters to it if wanted and afterwards
   *  invoke {@link #setIconImage(Image)}. */

  protected Image getDefaultLogo()
  {
    return new LogoProducer().createImage(ICON_WIDTH, ICON_HEIGHT);             // dynamically generated every time instead of reading it from file or even caching it: it is not such a big overhead
  }

//------------------------------------------------------------------------------

}
