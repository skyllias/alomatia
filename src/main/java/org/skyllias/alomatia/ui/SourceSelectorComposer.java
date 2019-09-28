
package org.skyllias.alomatia.ui;

import java.awt.AWTException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.source.AsynchronousUrlSource;
import org.skyllias.alomatia.source.BasicFileSource;
import org.skyllias.alomatia.source.ClipboardSource;
import org.skyllias.alomatia.source.DirFileSource;
import org.skyllias.alomatia.source.DropSource;
import org.skyllias.alomatia.source.ScreenSource;
import org.skyllias.alomatia.source.ScreenSource.ScreenRectangle;
import org.skyllias.alomatia.source.SingleFileSource;
import org.skyllias.alomatia.source.SourceCatalogue;
import org.skyllias.alomatia.source.VoidSource;
import org.skyllias.alomatia.ui.CaptureFrameComposer.CaptureBoundsListener;

/** Composer of a panel with the controls to select a {@link ImageSource}.
 *  TODO Make it more OO by obtaining a component for each source type. */

public class SourceSelectorComposer
{
  private static final String SOURCE_LABEL             = "source.selector.title";
  protected static final String NO_SOURCE_LABEL        = "source.none.name";    // protected to be accessible in tests
  protected static final String DND_SOURCE_LABEL       = "source.dnd.name";
  protected static final String CLIPBOARD_SOURCE_LABEL = "source.clipboard.name";
  protected static final String FILE_SOURCE_LABEL      = "source.file.name";
  protected static final String DIR_SOURCE_LABEL       = "source.directory.name";
  protected static final String SCREEN_SOURCE_LABEL    = "source.screen.name";
  protected static final String URL_SOURCE_LABEL       = "source.url.name";
  private static final String CLIPBOARD_AUTO_LABEL     = "source.selector.clipboard.checkbox";
  private static final String CAPTURE_LABEL            = "source.selector.screen.button";
  private static final String FILE_LABEL               = "source.selector.file.button";
  private static final String DIR_LABEL                = "source.selector.directory.button";
  private static final String IMAGE_FILES_FILTER       = "source.selector.file.filter";

  protected static final String CLIPBOARD_AUTOMODE_NAME = "checkbox.clipboard.automode";

  protected static final String PREFKEY_CLIPBOARDAUTO = "sourceClipboardAuto";
  protected static final String PREFKEY_DEFAULTDIR    = "defaultSourceDir";
  protected static final String PREFKEY_DEFAULTFILE   = "defaultSourceFile";

  private final LabelLocalizer labelLocalizer;
  private final CaptureFrameComposer captureFrameComposer;
  private final SourceCatalogue sourceCatalogue;

  private SourceRadioSelector<JRadioButton> radioSelector;

  private Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  /** Creates a new selector to choose from the known types in the catalogue.
   *  The unknown types are ignored, and the missing known types are gently skipped. */

  public SourceSelectorComposer(LabelLocalizer localizer, SourceCatalogue catalogue,
                                CaptureFrameComposer captureFrame,
                                SourceRadioSelector<JRadioButton> sourceRadioSelector)
  {
    labelLocalizer       = localizer;
    sourceCatalogue      = catalogue;
    captureFrameComposer = captureFrame;
    radioSelector        = sourceRadioSelector;
  }

//==============================================================================

  /** Returns a new component with the controls set up. */

  public JComponent getComponent()
  {
    JPanel panel = new BasicControlPanelComposer().getPanel(labelLocalizer.getString(SOURCE_LABEL));

    initVoidSelector(panel);
    initDropSelector(panel);
    initClipboardSelector(panel);
    initScreenSelector(panel);
    initUrlSelector(panel);
    initSingleFileSelector(panel);
    initDirFileSelector(panel);

    return panel;
  }

//------------------------------------------------------------------------------

  /** Only meant for testing. */

  protected void setPreferences(Preferences prefs) {preferences = prefs;}

//------------------------------------------------------------------------------

  /* Sets up the drop selector radio if the catalogue contains a DropSource. */

  private void initVoidSelector(JPanel panel)
  {
    VoidSource voidSource = sourceCatalogue.get(VoidSource.class);
    if (voidSource != null)
    {
      JPanel configPanel = new JPanel();
      configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));
      configPanel.add(radioSelector.createRadioObject(NO_SOURCE_LABEL, voidSource));
      configPanel.add(Box.createHorizontalGlue());
      panel.add(configPanel);
    }
  }

//------------------------------------------------------------------------------

  /* Sets up the drop selector radio if the catalogue contains a DropSource. */

  private void initDropSelector(JPanel panel)
  {
    DropSource dropSource = sourceCatalogue.get(DropSource.class);
    if (dropSource != null)
    {
      JPanel configPanel = new JPanel();
      configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));
      configPanel.add(radioSelector.createRadioObject(DND_SOURCE_LABEL, dropSource));
      configPanel.add(Box.createHorizontalGlue());
      panel.add(configPanel);
    }
  }

//------------------------------------------------------------------------------

  /* Sets up the clipboard selector radio and checkbox if the catalogue contains a ClipboardSource. */

  private void initClipboardSelector(JPanel panel)
  {
    final ClipboardSource clipboardSource = sourceCatalogue.get(ClipboardSource.class);
    if (clipboardSource != null)
    {
      boolean isAutoMode = preferences.getBoolean(PREFKEY_CLIPBOARDAUTO, true);

      JPanel configPanel = new JPanel();
      configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

      final JCheckBox autoCheckbox = new JCheckBox(labelLocalizer.getString(CLIPBOARD_AUTO_LABEL), isAutoMode);
      autoCheckbox.setName(CLIPBOARD_AUTOMODE_NAME);
      autoCheckbox.setEnabled(false);
      autoCheckbox.addChangeListener(new ChangeListener()
      {
        @Override
        public void stateChanged(ChangeEvent event)
        {
          boolean newAutoMode = autoCheckbox.isSelected();
          clipboardSource.setAutoMode(newAutoMode);
          preferences.putBoolean(PREFKEY_CLIPBOARDAUTO, newAutoMode);
        }
      });

      addPasteKeyListener(clipboardSource);

      ButtonSource wrapperSource = new ButtonSource(clipboardSource,
                                                    new CheckBoxEnabable(autoCheckbox), false);
      configPanel.add(radioSelector.createRadioObject(CLIPBOARD_SOURCE_LABEL, wrapperSource));
      configPanel.add(autoCheckbox);
      configPanel.add(Box.createHorizontalGlue());
      panel.add(configPanel);
    }
  }

//------------------------------------------------------------------------------

  /* Sets up the screen selector radio and button if the catalogue contains a ScreenSource. */

  private void initScreenSelector(JPanel panel)
  {
    final ScreenSource screenSource = sourceCatalogue.get(ScreenSource.class);
    if (screenSource != null)
    {
      JPanel configPanel = new JPanel();
      configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

      JButton screenButton = new JButton(labelLocalizer.getString(CAPTURE_LABEL));
      screenButton.setEnabled(false);
      screenButton.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          CaptureFrameListener captureListener = new CaptureFrameListener(screenSource);

          screenSource.setActive(false);                                        // always disable capture when the capture frame is open
          captureFrameComposer.openNewFrame(captureListener);
        }
      });

      ButtonSource wrapperSource = new ButtonSource(screenSource,
                                                    new ButtonEnabable(screenButton));
      configPanel.add(radioSelector.createRadioObject(SCREEN_SOURCE_LABEL, wrapperSource));
      configPanel.add(screenButton);
      configPanel.add(Box.createHorizontalGlue());
      panel.add(configPanel);
    }
  }

//------------------------------------------------------------------------------

  /* Sets up the asynchronous URL selector radio and button, reading and writing
   * preferences to remember the last selection between executions. */

  private void initUrlSelector(JPanel panel)
  {
    final AsynchronousUrlSource urlSource = sourceCatalogue.get(AsynchronousUrlSource.class);
    if (urlSource != null)
    {
      UrlDownloadSubcomponentComposer downloadComponent = new UrlDownloadSubcomponentComposer(labelLocalizer,
                                                                        urlSource);
      JPanel configPanel = new JPanel();
      configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

      ButtonSource wrapperSource = new ButtonSource(urlSource, downloadComponent, false);
      configPanel.add(radioSelector.createRadioObject(URL_SOURCE_LABEL, wrapperSource));
      configPanel.add(downloadComponent.getTextField());
      configPanel.add(downloadComponent.getButton());
      panel.add(configPanel);
    }
  }

//------------------------------------------------------------------------------

  /* Sets up the single file selector radio and button, reading and writing
   * preferences to remember the last selection between executions. */

  private void initSingleFileSelector(JPanel panel)
  {
    final SingleFileSource fileSource = sourceCatalogue.get(SingleFileSource.class);
    if (fileSource != null)
    {
      JFileChooser chooser   = new JFileChooser();
      FileFilter imageFilter = new FileNameExtensionFilter(labelLocalizer.getString(IMAGE_FILES_FILTER),
                                                           ImageIO.getReaderFileSuffixes());
      chooser.setAcceptAllFileFilterUsed(false);
      chooser.addChoosableFileFilter(imageFilter);

      initFileSelector(fileSource, chooser, panel, PREFKEY_DEFAULTFILE,
                       FILE_LABEL, FILE_SOURCE_LABEL);
    }
  }

//------------------------------------------------------------------------------

  /* Sets up the directory file selector radio and button, reading and writing
   * preferences to remember the last selection between executions. */

  private void initDirFileSelector(JPanel panel)
  {
    final DirFileSource dirSource = sourceCatalogue.get(DirFileSource.class);
    if (dirSource != null)
    {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      initFileSelector(dirSource, chooser, panel, PREFKEY_DEFAULTDIR,
                       DIR_LABEL, DIR_SOURCE_LABEL);

      addNavigationKeyListener(dirSource);
    }
  }

//------------------------------------------------------------------------------

  /* Adds to panel  a radio button with sourceLabel localized as its label and
   * used as its action command and with a PathTextField and a button next to it.
   * If the preferences contain a value for preferencesKey, it is used as the
   * initial value for the field and for the chooser.
   * The button gets as label the localized value for buttonLabelKey and when
   * clicked shows the chooser, which should already be set up to choose files
   * of types compatible with fileSource. When a file is selected, it is
   * passed to the source.
   * Initially the button and source are inactive, and when the radio button
   * is selected then both are activated. */

  private void initFileSelector(final BasicFileSource fileSource, final JFileChooser chooser,
                                JPanel panel, final String preferencesKey,
                                String buttonLabelKey, String sourceLabel)
  {
    JPanel configPanel = new JPanel();
    configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

    File initialFile       = null;
    String initialFilePath = preferences.get(preferencesKey, null);
    if (initialFilePath != null) initialFile = new File(initialFilePath);
    fileSource.setFileSource(initialFile);

    final PathTextField pathField = new PathTextField();
    pathField.setText(initialFilePath);                                         // nulls are gently handled

    chooser.setSelectedFile(initialFile);

    JButton fileButton = new JButton(labelLocalizer.getString(buttonLabelKey));
    fileButton.setEnabled(false);
    fileButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
          File selectedFile   = chooser.getSelectedFile();
          String selectedPath = selectedFile.getAbsolutePath();
          fileSource.setFileSource(selectedFile);
          pathField.setText(selectedPath);

          preferences.put(preferencesKey, selectedPath);
        }
      }
    });

    ButtonSource wrapperSource = new ButtonSource(fileSource,
                                                  new ButtonEnabable(fileButton),
                                                  false);
    configPanel.add(radioSelector.createRadioObject(sourceLabel, wrapperSource));
    configPanel.add(pathField);
    configPanel.add(fileButton);
    panel.add(configPanel);
  }

//------------------------------------------------------------------------------

  /* Adds a global listener that makes clipboardSource show the contents of the
   * clipboard when the proper keys are pressed: Ctrl + V or Shift + Insert. */

  private void addPasteKeyListener(final ClipboardSource clipboardSource)
  {
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventDispatcher(new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED)
        {
          int pressedKeyCode    = e.getKeyCode();
          boolean isControlDown = EventUtils.isControlDown(e);
          boolean isShiftDown   = e.isShiftDown();
          boolean isPaste = (isControlDown && pressedKeyCode == KeyEvent.VK_V) ||
                            (isShiftDown && pressedKeyCode == KeyEvent.VK_INSERT);
          if (isPaste) clipboardSource.readFromClipboard();
        }
        return false;                                                           // allow the event to be redispatched
      }
    });
  }

//------------------------------------------------------------------------------

  /* Adds a global listener that makes dirSource show the next or previous file
   * when the proper keys are pressed: Page-Down (or Ctrl + Space) to go forward,
   * Page-Up (or Shift + Space) to go backward. */

  private void addNavigationKeyListener(final DirFileSource dirSource)
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
          boolean isCtrlDown  = e.isControlDown();                              // not EventUtils.isControlDown(e) on purpose
          boolean isNext      = pressedKeyCode == KeyEvent.VK_PAGE_DOWN ||
                                (pressedKeyCode == KeyEvent.VK_SPACE && isCtrlDown);
          boolean isPrevious  = pressedKeyCode == KeyEvent.VK_PAGE_UP ||
                                (pressedKeyCode == KeyEvent.VK_SPACE && isShiftDown);
          if      (isNext)     dirSource.nextImageFile();
          else if (isPrevious) dirSource.previousImageFile();
        }
        return false;                                                           // allow the event to be redispatched
      }
    });
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /* Listener to invoke when the CaptureFrame button is clicked. */

  private class CaptureFrameListener implements CaptureBoundsListener
  {
    private ScreenSource screenSource;

    public CaptureFrameListener(ScreenSource source) {screenSource = source;}

    /** Passes the bounds to the source and activates it. */

    @Override
    public void boundsSelected(ScreenRectangle bounds)
    {
      try
      {
        screenSource.setScreenBounds(bounds);
        screenSource.setActive(true);
      }
      catch (AWTException awte) {awte.printStackTrace();}                       // TODO log
    }

  }

//******************************************************************************

  /** Wrapper around a component that can be enabled.
   *  It should be better explained and designed. */

  public interface Enabable
  {
    void setEnabled(boolean active);
  }

//******************************************************************************

  /* Wrapper around a JButton. */

  private class ButtonEnabable implements Enabable
  {
    private JButton button;

    public ButtonEnabable(JButton jButton) {button = jButton;}

    @Override
    public void setEnabled(boolean active) {button.setEnabled(active);}
  }

//******************************************************************************

  /* Wrapper around a JCheckBox. */

  private class CheckBoxEnabable implements Enabable
  {
    private JCheckBox checkbox;

    public CheckBoxEnabable(JCheckBox jCheckbox) {checkbox = jCheckbox;}

    @Override
    public void setEnabled(boolean active) {checkbox.setEnabled(active);}
  }

//******************************************************************************

  /* Fake source wrapping a real one that instead of activating it directly
   * enables a JButton that may eventually activate the delegate source. This
   * "delay", though, can be turned off.
   * Deactivating it implies the deactivation of both the component and the real
   * source. */

  private class ButtonSource implements ImageSource
  {
    private ImageSource realSource;
    private Enabable button;
    private boolean delay = true;

    public ButtonSource(ImageSource wrappedSource, Enabable enablyButton)
    {
      this(wrappedSource, enablyButton, true);
    }

    public ButtonSource(ImageSource wrappedSource, Enabable enablyButton, boolean delaySourceActivation)
    {
      realSource = wrappedSource;
      button     = enablyButton;
      delay      = delaySourceActivation;
    }

    @Override
    public void setDisplay(ImageDisplay display) {}

    @Override
    public void setActive(boolean active)
    {
      button.setEnabled(active);
      if (delay)
      {
        if (!active) realSource.setActive(false);
      }
      else realSource.setActive(active);
    }

  }

//------------------------------------------------------------------------------

}
