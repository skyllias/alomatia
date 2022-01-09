
package org.skyllias.alomatia.ui;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.springframework.stereotype.Component;

/** Composer of bare empty panels that can be used to contain controls. */

@Component
public class BarePanelComposer
{
//==============================================================================

  /** Returns a new instance with the passed title, which must be already i18ed. */

  public JPanel getPanel(String title)
  {
    JPanel panel = new JPanel();
    panel.setName(title);

    panel.setBorder(BorderFactory.createTitledBorder(title));

    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    return panel;
  }

//------------------------------------------------------------------------------

}
