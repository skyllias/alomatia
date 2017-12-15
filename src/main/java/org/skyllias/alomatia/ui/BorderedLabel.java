
package org.skyllias.alomatia.ui;

import javax.swing.*;
import javax.swing.border.*;

/** Text-only JLabel with a border so that it does not stick to nearby components.
 *  The margin should be the same used by other components like JRadioButton,
 *  but that may depend on the L&F. Therefore, initially it is hardcoded. */

@SuppressWarnings("serial")
public class BorderedLabel extends JLabel
{
  private static final int BORDER_SIZE = 5;

  private Border fixedBorder = BorderFactory.createEmptyBorder(0, BORDER_SIZE, 0, BORDER_SIZE);

//==============================================================================

  public BorderedLabel()
  {
    super();

    setBorder(fixedBorder);
  }

//------------------------------------------------------------------------------

  public BorderedLabel(String text)
  {
    super(text);

    setBorder(fixedBorder);
  }

//==============================================================================

}
