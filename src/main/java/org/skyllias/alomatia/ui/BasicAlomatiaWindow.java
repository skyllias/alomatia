
package org.skyllias.alomatia.ui;

import javax.swing.*;

import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.logo.*;

/** Superclass for all application windows. */

@SuppressWarnings("serial")
public abstract class BasicAlomatiaWindow extends JFrame
{
  private static final int ICON_WIDTH  = 32;
  private static final int ICON_HEIGHT = 32;

  private LabelLocalizer labelLocalizer;

//==============================================================================

  /** Instantiates a new window with the icon already set and a title translated
   *  to the current locale. */

  protected BasicAlomatiaWindow(LabelLocalizer localizer, String titleKey)
  {
    super(localizer.getString(titleKey));

    labelLocalizer = localizer;

    setIconImage(new LogoProducer().createImage(ICON_WIDTH, ICON_HEIGHT));      // dynamically generated instead of reading it from file: it is not such a big overhead
  }

//==============================================================================

  /** Returns the localizer from which UI labels can be obtained. */

  protected LabelLocalizer getLabelLocalizer() {return labelLocalizer;}

//------------------------------------------------------------------------------

}
