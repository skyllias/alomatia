
package org.skyllias.alomatia.ui.source;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Optional;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.SourceDirFilePreferences;
import org.skyllias.alomatia.source.DirFileSource;
import org.skyllias.alomatia.ui.component.PathTextField;
import org.skyllias.alomatia.ui.file.FileChooserAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Composer for a selector of {@link DirFileSource}. */

@Component
@Order(6)
public class DirFileSourceSelectionComposer implements SourceSelectionComposer
{
  private static final String SOURCE_KEY = "directory";

  private static final String SELECT_BUTTON_LABEL = "source.selector.directory.button";

  protected static final String PATH_FIELD_NAME  = "path-field";                // protected for testing purposes
  protected static final String BUTTON_NAME      = "select-button";

  private final DirFileSource dirFileSource;
  private final FileChooserAdapter fileChooserAdapter;
  private final SourceDirFilePreferences sourceDirFilePreferences;
  private final LabelLocalizer labelLocalizer;

//==============================================================================

  public DirFileSourceSelectionComposer(DirFileSource dirFileSource,
                                        @Qualifier("dirFileChooser") FileChooserAdapter fileChooserAdapter,
                                        SourceDirFilePreferences sourceDirFilePreferences,
                                        LabelLocalizer labelLocalizer)
  {
    this.dirFileSource            = dirFileSource;
    this.fileChooserAdapter       = fileChooserAdapter;
    this.sourceDirFilePreferences = sourceDirFilePreferences;
    this.labelLocalizer           = labelLocalizer;

    addNavigationKeyListener();
  }

//==============================================================================

  @Override
  public SourceSelection buildSelector()
  {
    return new DirFileSourceSelection();
  }

//------------------------------------------------------------------------------

  @Override
  public String getSourceKey() {return SOURCE_KEY;}

//------------------------------------------------------------------------------

  /* Adds a global listener that makes dirSource show the next or previous file
   * when the proper keys are pressed: Page-Down (or Ctrl + Space) to go forward,
   * Page-Up (or Shift + Space) to go backward. */

  private void addNavigationKeyListener()
  {
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventDispatcher(new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED)
        {
          int pressedKeyCode  = e.getKeyCode();
          boolean isShiftDown = e.isShiftDown();
          boolean isCtrlDown  = e.isControlDown();                              // not EventUtils.isControlDown(e) on purpose because in Mac it would collide with a system-managed shortcut
          boolean isNext      = pressedKeyCode == KeyEvent.VK_PAGE_DOWN ||
                                (pressedKeyCode == KeyEvent.VK_SPACE && isCtrlDown);
          boolean isPrevious  = pressedKeyCode == KeyEvent.VK_PAGE_UP ||
                                (pressedKeyCode == KeyEvent.VK_SPACE && isShiftDown);
          if      (isNext)     dirFileSource.nextImageFile();
          else if (isPrevious) dirFileSource.previousImageFile();
        }
        return false;                                                           // allow the event to be redispatched
      }
    });
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /* Too similar to SingleFileSourceSelectionComposer.SingleFileSourceSelection. */

  private class DirFileSourceSelection implements SourceSelection
  {
    private final JButton selectButton;
    private final JPanel controlsPanel;

    public DirFileSourceSelection()
    {
      final PathTextField pathField = new PathTextField();
      pathField.setName(PATH_FIELD_NAME);

      selectButton  = buildSelectionButton(pathField);
      controlsPanel = buildControlsPanel(pathField, selectButton);

      initSourceDir(pathField);
    }

    @Override
    public ImageSource getSource()
    {
      return new ImageSource()
      {
        @Override
        public void setActive(boolean active)
        {
          dirFileSource.setActive(active);
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
            dirFileSource.setFileSource(selectedFile);
            pathField.setText(selectedPath);

            sourceDirFilePreferences.setDefaultDirPath(selectedPath);
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

  private void initSourceDir(PathTextField pathField)
  {
    Optional.ofNullable(sourceDirFilePreferences.getDefaultDirPath())
            .map(File::new)
            .ifPresent(dir ->
            {
              pathField.setText(dir.getAbsolutePath());
              fileChooserAdapter.setSelectedFile(dir);
              dirFileSource.setFileSource(dir);
            });
  }
}
