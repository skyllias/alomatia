
package org.skyllias.alomatia.ui;

import java.awt.event.*;
import java.io.*;
import java.util.prefs.*;

import javax.swing.*;
import javax.swing.event.*;

import org.skyllias.alomatia.i18n.*;

/** Composer of the panel with the save to file controls.
 *  The panel can be obtained by calling {@link #getComponent()}. */

public class SaveFilePanelComposer
{
  private static final String USER_HOME_PROP = "user.home";

  private static final String TITLE_LABEL       = "save.control.title";
  private static final String DESTINATION_LABEL = "save.control.destination";
  private static final String CHANGE_LABEL      = "save.control.change";
  private static final String PROMPT_LABEL      = "save.control.prompt";

  protected static final String PROMPT_CHECKBOX_NAME = "checkbox.prompt";

  protected static final String PREFKEY_DESTINATION = "saveDestinationDirectory";
  protected static final String PREFKEY_PROMPT      = "promptFileToSave";

  private LabelLocalizer labelLocalizer;
  private FileImageSaver imageSaver;

  private Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public SaveFilePanelComposer(LabelLocalizer localizer, FileImageSaver saver)
  {
    labelLocalizer = localizer;
    imageSaver     = saver;
  }

//==============================================================================

  /** Returns a new panel with the required controls.
   *  The image saver is modified now with the values found in the preferences. */

  public JComponent getComponent()
  {
    String initialDestinationPath = preferences.get(PREFKEY_DESTINATION,
                                                    System.getProperty(USER_HOME_PROP));
    boolean initialPrompt         = preferences.getBoolean(PREFKEY_PROMPT, true);
    imageSaver.setDestinationDir(new File(initialDestinationPath));
    imageSaver.setPrompt(initialPrompt);

    SavePanel savePanel = new SavePanel();
    addDestinationComponents(savePanel, initialDestinationPath);
    addPromptComponents(savePanel, initialPrompt);
    return savePanel;
  }

//------------------------------------------------------------------------------

  /** Meant only for testing purposes. */

  protected void setPreferences(Preferences prefs) {preferences = prefs;}

//------------------------------------------------------------------------------

  /* Adds a line with a non-editable textfield and a button to display and change
   * the destinationPath. */

  private void addDestinationComponents(SavePanel savePanel, String destinationPath)
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

          preferences.put(PREFKEY_DESTINATION, selectedPath);
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

  private void addPromptComponents(SavePanel savePanel, boolean initialPrompt)
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

        preferences.putBoolean(PREFKEY_PROMPT, askUser);
      }
    });
    promptCheckbox.setName(PROMPT_CHECKBOX_NAME);

    configPanel.add(promptCheckbox);
    configPanel.add(Box.createHorizontalGlue());
    savePanel.add(configPanel);
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /* The panel itself, simply setting the title and the label localizer. */

  @SuppressWarnings("serial")
  private class SavePanel extends BasicControlPanel
  {
    protected SavePanel() {super(labelLocalizer, TITLE_LABEL);}
  }

}
