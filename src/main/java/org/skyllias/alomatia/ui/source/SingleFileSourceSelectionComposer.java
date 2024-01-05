
package org.skyllias.alomatia.ui.source;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.SourcePreferences;
import org.skyllias.alomatia.source.SingleFileSource;
import org.skyllias.alomatia.ui.component.PathTextField;
import org.skyllias.alomatia.ui.file.FileChooserAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Composer for a selector of {@link SingleFileSource}. */

@Component
@Order(5)
public class SingleFileSourceSelectionComposer implements SourceSelectionComposer
{
  private static final String SOURCE_KEY = "file";

  private static final String SELECT_BUTTON_LABEL = "source.selector.file.button";

  protected static final String PATH_FIELD_NAME  = "path-field";                // protected for testing purposes
  protected static final String BUTTON_NAME      = "select-button";

  private final SingleFileSource singleFileSource;
  private final FileChooserAdapter fileChooserAdapter;
  private final SourcePreferences sourcePreferences;
  private final LabelLocalizer labelLocalizer;

//==============================================================================

  public SingleFileSourceSelectionComposer(SingleFileSource singleFileSource,
                                           @Qualifier("singleFileChooser") FileChooserAdapter fileChooserAdapter,
                                           SourcePreferences sourcePreferences,
                                           LabelLocalizer labelLocalizer)
  {
    this.singleFileSource   = singleFileSource;
    this.fileChooserAdapter = fileChooserAdapter;
    this.sourcePreferences  = sourcePreferences;
    this.labelLocalizer     = labelLocalizer;
  }

//==============================================================================

  @Override
  public SourceSelection buildSelector()
  {
    return new SingleFileSourceSelection();
  }

//------------------------------------------------------------------------------

  @Override
  public String getSourceKey() {return SOURCE_KEY;}

//------------------------------------------------------------------------------

//******************************************************************************

  /* Too similar to DirFileSourceSelectionComposer.DirFileSourceSelection. */

  private class SingleFileSourceSelection implements SourceSelection
  {
    private final JButton selectButton;
    private final JPanel controlsPanel;

    public SingleFileSourceSelection()
    {
      final PathTextField pathField = new PathTextField();
      pathField.setName(PATH_FIELD_NAME);

      selectButton  = buildSelectionButton(pathField);
      controlsPanel = buildControlsPanel(pathField, selectButton);

      initSourceFile(pathField);
    }

    @Override
    public ImageSource getSource()
    {
      return new ImageSource()
      {
        @Override
        public void setActive(boolean active)
        {
          singleFileSource.setActive(active);
          selectButton.setEnabled(active);
        }
      };
    }

    @Override
    public JComponent getControls() {return controlsPanel;}

    private JButton buildSelectionButton(final PathTextField pathField)
    {
      JButton fileButton = new JButton(labelLocalizer.getString(SELECT_BUTTON_LABEL));
      fileButton.setEnabled(false);
      fileButton.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          if (fileChooserAdapter.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
          {
            File selectedFile   = fileChooserAdapter.getSelectedFile();
            String selectedPath = selectedFile.getAbsolutePath();
            singleFileSource.setFileSource(selectedFile);
            pathField.setText(selectedPath);

            sourcePreferences.setDefaultFilePath(selectedPath);
          }
        }
      });
      fileButton.setName(BUTTON_NAME);

      return fileButton;
    }

    private JPanel buildControlsPanel(PathTextField pathField,
                                      JButton selectionButton)
    {
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

      panel.add(pathField);
      panel.add(selectionButton);

      return panel;
    }
  }

  private void initSourceFile(PathTextField pathField)
  {
    Optional.ofNullable(sourcePreferences.getDefaultFilePath())
            .map(File::new)
            .ifPresent(file ->
            {
              pathField.setText(file.getAbsolutePath());
              fileChooserAdapter.setSelectedFile(file);
              singleFileSource.setFileSource(file);
            });
  }

}
