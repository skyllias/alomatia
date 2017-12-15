
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.prefs.*;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;

import org.skyllias.alomatia.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.source.*;
import org.skyllias.alomatia.source.ScreenSource.*;
import org.skyllias.alomatia.ui.CaptureFrame.*;

/** Selector of the source of original images.
 *  <p>
 *  The type and last selected file and dir are stored as user preferences. */

@SuppressWarnings("serial")
public class SourceSelector extends BasicSelector<ImageSource>
                            implements ActionListener
{
  private static final String SOURCE_LABEL             = "source.selector.title";
  protected static final String NO_SOURCE_LABEL        = "source.none.name";    // protected to be accessible in tests
  protected static final String DND_SOURCE_LABEL       = "source.dnd.name";
  protected static final String CLIPBOARD_SOURCE_LABEL = "source.clipboard.name";
  protected static final String FILE_SOURCE_LABEL      = "source.file.name";
  protected static final String DIR_SOURCE_LABEL       = "source.directory.name";
  protected static final String SCREEN_SOURCE_LABEL    = "source.screen.name";
  protected static final String URL_SOURCE_LABEL       = "source.url.name";
  private static final String CAPTURE_LABEL            = "source.selector.screen.button";
  private static final String FILE_LABEL               = "source.selector.file.button";
  private static final String DIR_LABEL                = "source.selector.directory.button";
  private static final String IMAGE_FILES_FILTER       = "source.selector.file.filter";

  private static final String PREFKEY_SOURCECOMMAND = "sourceCommandName";
  private static final String PREFKEY_DEFAULTDIR    = "defaultSourceDir";
  private static final String PREFKEY_DEFAULTFILE   = "defaultSourceFile";

  private static final int TEXT_FIELD_COLUMNS = 40;                             // used to prevent text fields from trying to fit the whole text with its preferred size

  private ImageSource previousSource;
  private JPanel optionsContainer = new JPanel();                               // no idea why (well, probably due to the use of alignments, see https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html#features), but if the options are added to the SourceSelector directly then it only stretches the right 50% of the space. TODO fix this

  private Preferences preferences;

//==============================================================================

  /** Creates a new selector to choose from the known types in the catalogue.
   *  The unknown types are ignored, and the missing known types are gently skipped. */

  public SourceSelector(LabelLocalizer localizer, SourceCatalogue catalogue)
  {
    this(getDefaultPreferences(), localizer, catalogue);
  }

//------------------------------------------------------------------------------

  /** Only meant for preferences injection in tests. */

  protected SourceSelector(Preferences prefs, LabelLocalizer localizer, SourceCatalogue catalogue)
  {
    super(localizer, SOURCE_LABEL);

    preferences = prefs;

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));                           // all this stuff is required for the SourceSelector to be centered
    optionsContainer.setLayout(new BoxLayout(optionsContainer, BoxLayout.Y_AXIS));
    add(optionsContainer);

    VoidSource voidSource = catalogue.get(VoidSource.class);                    // TODO make it more OO by getting a component for each type of source
    if (voidSource != null) addRadioObject(NO_SOURCE_LABEL, voidSource, optionsContainer);

    DropSource dropSource = catalogue.get(DropSource.class);
    if (dropSource != null) addRadioObject(DND_SOURCE_LABEL, dropSource, optionsContainer);


    ClipboardSource clipboardSource = catalogue.get(ClipboardSource.class);
    if (clipboardSource != null) addRadioObject(CLIPBOARD_SOURCE_LABEL, clipboardSource, optionsContainer);

    initScreenSelector(catalogue);
    initUrlSelector(catalogue);
    initSingleFileSelector(catalogue);
    initDirFileSelector(catalogue);

    String previousSelectionCommand = getPreferences().get(PREFKEY_SOURCECOMMAND, null);
    setSelectionByActionCommand(previousSelectionCommand);
    previousSource = getCurrentSelection();
    previousSource.setActive(true);                                             // set as active the source corresponding to the currently selected radio
  }

//==============================================================================

  /* Sets up the screen selector radio and button if the catalogue contains a ScreenSource. */

  private void initScreenSelector(SourceCatalogue catalogue)
  {
    final ScreenSource screenSource = catalogue.get(ScreenSource.class);
    if (screenSource != null)
    {
      JPanel configPanel = new JPanel();
      configPanel.setAlignmentX(LEFT_ALIGNMENT);
      configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));


      final CaptureFrameListener captureListener = new CaptureFrameListener(screenSource);

      JButton screenButton = new JButton(getLabelLocalizer().getString(CAPTURE_LABEL));
      screenButton.setEnabled(false);
      screenButton.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          screenSource.setActive(false);                                        // always disable capture when the capture frame is open
          new CaptureFrame(getLabelLocalizer(), captureListener);
        }
      });

      ButtonSource wrapperSource = new ButtonSource(screenSource,
                                                    new ButtonEnabable(screenButton));
      addRadioObject(SCREEN_SOURCE_LABEL, wrapperSource, configPanel);
      configPanel.add(screenButton);
      optionsContainer.add(configPanel);
    }
  }

//------------------------------------------------------------------------------

  /* Sets up the asynchronous URL selector radio and button, reading and writing
   * preferences to remember the last selection between executions. */

  private void initUrlSelector(SourceCatalogue catalogue)
  {
    final AsynchronousUrlSource urlSource = catalogue.get(AsynchronousUrlSource.class);
    if (urlSource != null)
    {
      UrlDownloadComponent downloadComponent = new UrlDownloadComponent(getLabelLocalizer(),
                                                                        urlSource);
      JPanel configPanel = new JPanel();
      configPanel.setAlignmentX(LEFT_ALIGNMENT);
      configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

      ButtonSource wrapperSource = new ButtonSource(urlSource, downloadComponent, false);
      addRadioObject(URL_SOURCE_LABEL, wrapperSource, configPanel);
      configPanel.add(downloadComponent.getTextField());
      configPanel.add(downloadComponent.getButton());
      optionsContainer.add(configPanel);

      downloadComponent.getTextField().setColumns(TEXT_FIELD_COLUMNS);
    }
  }

//------------------------------------------------------------------------------

  /* Sets up the single file selector radio and button, reading and writing
   * preferences to remember the last selection between executions. */

  private void initSingleFileSelector(SourceCatalogue catalogue)
  {
    final SingleFileSource fileSource = catalogue.get(SingleFileSource.class);
    if (fileSource != null)
    {
      JFileChooser chooser   = new JFileChooser();
      FileFilter imageFilter = new FileNameExtensionFilter(getLabelLocalizer().getString(IMAGE_FILES_FILTER),
                                                           ImageIO.getReaderFileSuffixes());
      chooser.setAcceptAllFileFilterUsed(false);
      chooser.addChoosableFileFilter(imageFilter);

      initFileSelector(fileSource, chooser, PREFKEY_DEFAULTFILE,
                       FILE_LABEL, FILE_SOURCE_LABEL);
    }
  }

//------------------------------------------------------------------------------

  /* Sets up the directory file selector radio and button, reading and writing
   * preferences to remember the last selection between executions. */

  private void initDirFileSelector(SourceCatalogue catalogue)
  {
    final DirFileSource dirSource = catalogue.get(DirFileSource.class);
    if (dirSource != null)
    {
      JFileChooser chooser = new JFileChooser();
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      initFileSelector(dirSource, chooser, PREFKEY_DEFAULTDIR,
                       DIR_LABEL, DIR_SOURCE_LABEL);

      addNavigationKeyListener(dirSource);
    }
  }

//------------------------------------------------------------------------------

  /* Adds a radio button with sourceLabel localized as its label and used as its
   * action command and with a PathTextField and a button next to it.
   * If the preferences contain a value for preferencesKey, it is used as the
   * initial value for the field and for the chooser.
   * The button gets as label the localized value for buttonLabelKey and when
   * clicked shows the chooser, which should already be set up to choose files
   * of types compatible with fileSource. When a file is selected, it is
   * passed to the source.
   * Initially the button and source are inactive, and when the radio button
   * is selected then both are activated. */

  private void initFileSelector(final BasicFileSource fileSource, final JFileChooser chooser,
                                final String preferencesKey, String buttonLabelKey, String sourceLabel)
  {
    JPanel configPanel = new JPanel();
    configPanel.setAlignmentX(LEFT_ALIGNMENT);
    configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.X_AXIS));

    File initialFile       = null;
    String initialFilePath = getPreferences().get(preferencesKey, null);
    if (initialFilePath != null) initialFile = new File(initialFilePath);
    fileSource.setFileSource(initialFile);

    final PathTextField pathField = new PathTextField();
    pathField.setText(initialFilePath);                                         // nulls are gently handled

    chooser.setSelectedFile(initialFile);

    JButton fileButton = new JButton(getLabelLocalizer().getString(buttonLabelKey));
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

          getPreferences().put(preferencesKey, selectedPath);
        }
      }
    });

    ButtonSource wrapperSource = new ButtonSource(fileSource,
                                                  new ButtonEnabable(fileButton),
                                                  false);
    addRadioObject(sourceLabel, wrapperSource, configPanel);
    configPanel.add(pathField);
    configPanel.add(fileButton);
    optionsContainer.add(configPanel);
  }

//------------------------------------------------------------------------------

  /* Adds a global listener that makes dirSource show the next or previous file
   * when the proper keys are pressed: X to go forward, Z to go backward. */

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
          if (e.getKeyCode() == KeyEvent.VK_X) dirSource.nextImageFile();
          if (e.getKeyCode() == KeyEvent.VK_Z) dirSource.previousImageFile();
        }
        return false;                                                           // allow the event to be redispatched
      }
    });
  }

//------------------------------------------------------------------------------

  /** Sets the selected nonnull source as active so that it can begin producing
   *  images to the display. The previous source get setActive(false) called.
   *  <p>
   *  The selection is stored in the user preferences. */


  @Override
  protected void onSelectionChanged(ImageSource source)
  {
    if (source != previousSource)
    {
      if (previousSource != null) previousSource.setActive(false);
      source.setActive(true);
    }
    previousSource = source;

    getPreferences().put(PREFKEY_SOURCECOMMAND, getCurrentSelectionAsActionCommand());
  }

//------------------------------------------------------------------------------

  /* Shortcut method to get preferences by subclasses that store the last URL. */

  private Preferences getPreferences() {return preferences;}

//------------------------------------------------------------------------------

  /* Returns the preferences to use when they are not externally injected. */

  private static Preferences getDefaultPreferences() {return Preferences.userNodeForPackage(SourceSelector.class);}

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

  /* Text field to show paths in. */

  private class PathTextField extends JTextField
  {
    public PathTextField()
    {
      setEditable(false);
      setColumns(TEXT_FIELD_COLUMNS);
      setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                   getPreferredSize().height));                 // prevent the containing layout from streching the field vertically
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
