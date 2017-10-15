
package org.skyllias.alomatia.ui;

import javax.swing.*;
import javax.swing.border.*;

import org.skyllias.alomatia.i18n.*;;

/** Superclass for the panels with a title used to configure something. */

@SuppressWarnings("serial")
public abstract class BasicControlPanel extends JPanel
{
  private LabelLocalizer labelLocalizer;

//==============================================================================

  /** Creates a new panel with the passed title localized. */

  protected BasicControlPanel(LabelLocalizer localizer, String legendKey)
  {
    labelLocalizer = localizer;

    setBorder(new TitledBorder(labelLocalizer.getString(legendKey)));

    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
  }

//==============================================================================

  /** Returns the localizer from which UI labels can be obtained. */

  protected LabelLocalizer getLabelLocalizer() {return labelLocalizer;}

//------------------------------------------------------------------------------

}
