
package org.skyllias.alomatia.ui.source;

import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.SourceCommandPreferences;
import org.skyllias.alomatia.ui.component.BarePanelComposer;
import org.skyllias.alomatia.ui.radio.RadioSelector;
import org.skyllias.alomatia.ui.radio.RadioSelector.RadioSelectorListener;
import org.springframework.stereotype.Component;

/** Composer of a panel with the controls to select an {@link ImageSource}. */

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

  /** Creates a new selector to choose from the available sources. */

  public SourceSelectorComposer(List<SourceSelectionComposer> sourceSelectionComposers,
                                LabelLocalizer localizer,
                                SourceCommandPreferences sourceCommandPreferences,
                                BarePanelComposer panelComposer)
  {
    this.sourceSelectionComposers = sourceSelectionComposers;
    labelLocalizer                = localizer;
    radioSelector                 = new SourceRadioSelector<>(JRadioButton.class, labelLocalizer, sourceCommandPreferences);
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

//******************************************************************************

  /* Creator of radio buttons used to select one of the available
   * {@link ImageSource}s.
   * It is generic with respect to the component used to implement the radio
   * buttons (JRadioButton or JRadioButtonMenuItem).
   * This used to be a separate class and might be separated again in the
   * future, but at this moment it does not offer any advantage and makes
   * testing more difficult. */

  private static class SourceRadioSelector<RADIO extends AbstractButton> implements RadioSelectorListener<ImageSource>
  {
    private final RadioSelector<RADIO, ImageSource> radioSelector;
    private final SourceCommandPreferences sourceCommandPreferences;

    private ImageSource previousSource;

//==============================================================================

    /** Creates a new instance that will create radio buttons of radioClazz type
     *  and enable or disable the sources as the radio buttons are selected. */

    public SourceRadioSelector(Class<RADIO> radioClazz, LabelLocalizer localizer,
                               SourceCommandPreferences sourceCommandPreferences)
    {
      radioSelector                 = new RadioSelector<>(radioClazz, localizer, this);
      this.sourceCommandPreferences = sourceCommandPreferences;
    }

//==============================================================================

    /** Returns a new radio button ready to activate source when selected,
     *  deactivating any other Image source that may have been selected through a
     *  radio button generated by this method. The first radio button is
     *  automatically selected, unless actionCommand is stored in the preferences,
     *  in which case source is taken selected.
     *  actionCommand is used as the key for localization and as the action
     *  command and name for the component. It must be different in all calls. */

    public RADIO createRadioObject(String actionCommand, ImageSource source)
    {
      RADIO radio = radioSelector.createRadioObject(actionCommand, source);

      String previousSelectionCommand = sourceCommandPreferences.getSourceCommandName();
      if (actionCommand.equals(previousSelectionCommand))
      {
        radioSelector.setSelectionByActionCommand(previousSelectionCommand);
      }

      if (previousSource == null)
      {
        previousSource = radioSelector.getCurrentSelection();
        if (previousSource != null) previousSource.setActive(true);
      }

      return radio;
    }

//------------------------------------------------------------------------------

    @Override
    public void onSelectionChanged(ImageSource selectedSource)
    {
      if (selectedSource != previousSource)
      {
        if (previousSource != null) previousSource.setActive(false);
        selectedSource.setActive(true);
      }
      previousSource = selectedSource;

      sourceCommandPreferences.setSourceCommandName(radioSelector.getCurrentSelectionAsActionCommand());
    }

//------------------------------------------------------------------------------

  }

}
