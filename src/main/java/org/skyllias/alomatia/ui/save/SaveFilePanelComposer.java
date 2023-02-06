
package org.skyllias.alomatia.ui.save;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.SavePreferences;
import org.skyllias.alomatia.ui.component.BarePanelComposer;
import org.skyllias.alomatia.ui.component.BorderedLabel;
import org.skyllias.alomatia.ui.component.PathTextField;
import org.springframework.stereotype.Component;

/** Composer of the panel with the save-to-file controls.
 *  The panel can be obtained by calling {@link #getComponent(FileImageSaver)}. */

@Component
public class SaveFilePanelComposer
{
  private static final String TITLE_LABEL       = "save.control.title";
  private static final String DESTINATION_LABEL = "save.control.destination";
  private static final String CHANGE_LABEL      = "save.control.change";
  private static final String PROMPT_LABEL      = "save.control.prompt";

  protected static final String PROMPT_CHECKBOX_NAME = "checkbox.prompt";

  private final LabelLocalizer labelLocalizer;
  private final BarePanelComposer bareControlPanelComposer;

  private final SavePreferences savePreferences;

//==============================================================================

  public SaveFilePanelComposer(LabelLocalizer localizer,
                               BarePanelComposer panelComposer,
                               SavePreferences savePreferences)
  {
    labelLocalizer           = localizer;
    bareControlPanelComposer = panelComposer;
    this.savePreferences     = savePreferences;
  }

//==============================================================================

  /** Returns a new panel with the required controls to save through the passed
   *  {@link ImageSaver}.
   *  The image saver is modified now with the values found in the preferences. */

  public JComponent getComponent(FileImageSaver imageSaver)
  {
    String initialDestinationPath = savePreferences.getDestinationPath();
    boolean initialPrompt         = savePreferences.isPromptOn();
    imageSaver.setDestinationDir(new File(initialDestinationPath));
    imageSaver.setPrompt(initialPrompt);

    JPanel savePanel = bareControlPanelComposer.getPanel(labelLocalizer.getString(TITLE_LABEL));
    addDestinationComponents(savePanel, initialDestinationPath, imageSaver);
    addPromptComponents(savePanel, initialPrompt, imageSaver);
    return savePanel;
  }

//------------------------------------------------------------------------------

  /* Adds a line with a non-editable textfield and a button to display and change
   * the destinationPath. */

  private void addDestinationComponents(JPanel savePanel, String destinationPath,
                                        FileImageSaver imageSaver)
  {
    JPanel configPanel = new JPanel();
    configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

    JLabel infoLabel = new BorderedLabel(labelLocalizer.getString(DESTINATION_LABEL));

    File initialDir               = new File(destinationPath);
    final PathTextField pathField = new PathTextField();
    pathField.setText(destinationPath);

    final JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setSelectedFile(initialDir);

    JButton fileButton = new JButton(labelLocalizer.getString(CHANGE_LABEL));
    fileButton.setEnabled(true);
    fileButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
          File selectedFile   = chooser.getSelectedFile();
          String selectedPath = selectedFile.getAbsolutePath();
          imageSaver.setDestinationDir(selectedFile);
          pathField.setText(selectedPath);

          savePreferences.setDestinationPath(selectedPath);
        }
      }
    });

    configPanel.add(infoLabel);
    configPanel.add(pathField);
    configPanel.add(fileButton);
    savePanel.add(configPanel);
  }

//------------------------------------------------------------------------------

  /* Adds a line with a checkbox to display and change the prompt behaviour of imageSaver. */

  private void addPromptComponents(JPanel savePanel, boolean initialPrompt,
                                   FileImageSaver imageSaver)
  {
    JPanel configPanel = new JPanel();
    configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

    final JCheckBox promptCheckbox = new JCheckBox(labelLocalizer.getString(PROMPT_LABEL),
                                                   initialPrompt);
    promptCheckbox.addChangeListener(new ChangeListener()
    {
      @Override
      public void stateChanged(ChangeEvent e)
      {
        boolean askUser = promptCheckbox.isSelected();
        imageSaver.setPrompt(askUser);

        savePreferences.setPromptOn(askUser);
      }
    });
    promptCheckbox.setName(PROMPT_CHECKBOX_NAME);

    configPanel.add(promptCheckbox);
    configPanel.add(Box.createHorizontalGlue());
    savePanel.add(configPanel);
  }

//------------------------------------------------------------------------------

}
