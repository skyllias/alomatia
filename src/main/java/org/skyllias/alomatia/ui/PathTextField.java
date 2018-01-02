
package org.skyllias.alomatia.ui;

import java.awt.*;

import javax.swing.*;

/** Non-editable {@link JTextField} with some preset sizes that prevent it from
 *  growing vertically or horizontally. */

@SuppressWarnings("serial")
public class PathTextField extends JTextField
{
  public static final int TEXT_FIELD_COLUMNS = 40;                              // used to prevent text fields from trying to fit the whole text with its preferred size

//==============================================================================

  public PathTextField()
  {
    setEditable(false);
    setColumns(TEXT_FIELD_COLUMNS);
    setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                 getPreferredSize().height));                   // prevent the containing layout from streching the field vertically
  }

//==============================================================================

}