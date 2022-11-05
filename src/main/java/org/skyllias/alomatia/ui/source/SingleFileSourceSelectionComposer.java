
package org.skyllias.alomatia.ui.source;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.source.SingleFileSource;
import org.skyllias.alomatia.ui.component.PathTextField;
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
  private final JFileChooser fileChooser;
  private final LabelLocalizer labelLocalizer;

//==============================================================================

  public SingleFileSourceSelectionComposer(SingleFileSource singleFileSource,
                                           @Qualifier("singleFileChooser") JFileChooser fileChooser,
                                           LabelLocalizer labelLocalizer)
  {
    this.singleFileSource = singleFileSource;
    this.fileChooser      = fileChooser;
    this.labelLocalizer   = labelLocalizer;
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

  private class SingleFileSourceSelection implements SourceSelection
  {
    private final JButton selectButton;
    private final JPanel controlsPanel;

    public SingleFileSourceSelection()
    {
      final PathTextField pathField = new PathTextField();
      pathField.setName(PATH_FIELD_NAME);
      singleFileSource.getSourceFile()
                      .map(File::getAbsolutePath)
                      .ifPresent(pathField::setText);

      selectButton  = buildSelectionButton(pathField);
      controlsPanel = buildControlsPanel(pathField, selectButton);
    }

    @Override
    public ImageSource getSource()
    {
      return new ImageSource()
      {
        @Override
        public void setDisplay(ImageDisplay display) {}

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
      singleFileSource.getSourceFile()
                      .ifPresent(fileChooser::setSelectedFile);

      JButton fileButton = new JButton(labelLocalizer.getString(SELECT_BUTTON_LABEL));
      fileButton.setEnabled(false);
      fileButton.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
          {
            File selectedFile   = fileChooser.getSelectedFile();
            String selectedPath = selectedFile.getAbsolutePath();
            singleFileSource.setFileSource(selectedFile);
            pathField.setText(selectedPath);
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
}
