
package org.skyllias.alomatia.ui;

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.ui.source.SourceSelection;
import org.skyllias.alomatia.ui.source.SourceSelectionComposer;
import org.springframework.stereotype.Component;

/** Composer of a panel with the controls to select an {@link ImageSource}.
 *  TODO Test. */

@Component
public class SourceSelectorComposer
{
  private static final String SOURCE_ACTION_COMMAND_FORMAT = "source.%s.name";

  private static final String SOURCE_LABEL = "source.selector.title";

  private final List<SourceSelectionComposer> sourceSelectionComposers;
  private final LabelLocalizer labelLocalizer;
  private final BarePanelComposer bareControlPanelComposer;
  private final SourceRadioSelector<JRadioButton> radioSelector;

//==============================================================================

  /** Creates a new selector to choose from the known types in the catalogue.
   *  The unknown types are ignored, and the missing known types are gently skipped. */

  public SourceSelectorComposer(List<SourceSelectionComposer> sourceSelectionComposers,
                                LabelLocalizer localizer,
                                SourceRadioSelector<JRadioButton> sourceRadioSelector,
                                BarePanelComposer panelComposer)
  {
    this.sourceSelectionComposers = sourceSelectionComposers;
    labelLocalizer                = localizer;
    radioSelector                 = sourceRadioSelector;
    bareControlPanelComposer      = panelComposer;
  }

//==============================================================================

  /** Returns a new component with the controls set up. */

  public JComponent getComponent()
  {
    JPanel panel = bareControlPanelComposer.getPanel(labelLocalizer.getString(SOURCE_LABEL));

    sourceSelectionComposers.forEach(composer -> addSourceSelector(composer, panel));

    return panel;
  }

//------------------------------------------------------------------------------

  /* Sets up the selector radio for the passed composer and adds it to the panel
   * along with the controls component. */

  private void addSourceSelector(SourceSelectionComposer sourceSelectionComposer,
                                 JPanel panel)
  {
    SourceSelection sourceSelection = sourceSelectionComposer.buildSelector();

    JPanel configPanel = new JPanel();
    configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

    String sourceActionCommand = String.format(SOURCE_ACTION_COMMAND_FORMAT,
                                               sourceSelectionComposer.getSourceKey());
    JRadioButton radioButton   = radioSelector.createRadioObject(sourceActionCommand,
                                                                 sourceSelection.getSource());
    configPanel.add(radioButton);
    configPanel.add(sourceSelection.getControls());
    panel.add(configPanel);
  }

//------------------------------------------------------------------------------

}
