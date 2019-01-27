
package org.skyllias.alomatia.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.skyllias.alomatia.display.Repeater;
import org.skyllias.alomatia.filter.FilterFactory;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.logo.LogoProducer;
import org.skyllias.alomatia.source.DropSource;
import org.skyllias.alomatia.source.SourceCatalogue;
import org.skyllias.alomatia.ui.frame.FrameAdaptorFactory;
import org.skyllias.alomatia.ui.frame.FramePolicy;
import org.skyllias.alomatia.ui.frame.JFrameAdaptorFactory;
import org.skyllias.alomatia.ui.frame.JInternalFrameAdaptorFactory;

/** Logic for the main windows of the application.
 *  <p>
 *  Only one instance of ControlFrame is expected in a given application. */

public class ControlFrame
{
  public static final int ICON_WIDTH  = 32;
  public static final int ICON_HEIGHT = 32;

  public static final Dimension DEFAULT_DESKTOP_SIZE = new Dimension(800, 600);

  private static final String CONTROL_TITLE = "control.window.title";
  private static final String DESKTOP_TITLE = "desktop.window.title";

//==============================================================================

  /** The catalogue is left untouched, although it is expected to have all the
   *  sources with the repeater set as display. If it contains a DropSource,
   *  this frame is added as drop target. */

  public ControlFrame(LabelLocalizer labelLocalizer, SourceCatalogue catalogue,
                      Repeater displayRepeater, FilterFactory filterFactory,
                      FramePolicy framePolicy)
  {
    FrameAdaptorFactory adaptorFactory;
    ControlsWindow controlsWindow;
    JFrame mainFrame = getNewFrame();
    if (framePolicy.isUsingInternalFrames())
    {
      JDesktopPane desktopPane = createDesktopFrame(mainFrame, labelLocalizer, catalogue);
      adaptorFactory           = new JInternalFrameAdaptorFactory(desktopPane);

      JDialog dialog           = new JDialog(mainFrame);
      controlsWindow           = new DialogControlsWindow(dialog);
      addPreferencesVisibleKeyListener(dialog);
    }
    else
    {
      adaptorFactory = new JFrameAdaptorFactory();
      controlsWindow = new FrameControlsWindow(mainFrame);
    }

    FileImageSaver imageSaver        = new FileImageSaver(labelLocalizer);
    DisplayFrameManager frameManager = new DisplayFrameManager(labelLocalizer,
                                                               filterFactory,
                                                               adaptorFactory,
                                                               imageSaver);

    setUpControlsFrame(controlsWindow, labelLocalizer, catalogue, displayRepeater,
                       filterFactory, frameManager, framePolicy, imageSaver);
  }

//==============================================================================

  /* Shows ownerContainer as non resizable and packed, with all the controls. */

  private void setUpControlsFrame(ControlsWindow ownerContainer,
                                  LabelLocalizer labelLocalizer, SourceCatalogue catalogue,
                                  Repeater displayRepeater, FilterFactory filterFactory,
                                  DisplayFrameManager frameManager, FramePolicy framePolicy,
                                  FileImageSaver imageSaver)
  {
    ownerContainer.setTitle(labelLocalizer.getString(CONTROL_TITLE));

    DropTargetListener dropListener = catalogue.get(DropSource.class);          // if not present it will be null
    if (dropListener != null) new DropTarget(ownerContainer.getComponent(), dropListener);

    ControlsPaneComposer controlsPane = new ControlsPaneComposer(labelLocalizer, catalogue,
                                                                 displayRepeater, dropListener,
                                                                 frameManager, framePolicy,
                                                                 imageSaver);
    ownerContainer.getContentPane().add(controlsPane.getComponent(),
                                        BorderLayout.CENTER);

    ownerContainer.pack();
    ownerContainer.setResizable(false);
    ownerContainer.setVisible(true);                                                     // do this at the end, when the size has been fixed
  }

//------------------------------------------------------------------------------

  /* Adds a global listener that makes dialog visible when Ctrl + P is pressed. */

  private void addPreferencesVisibleKeyListener(final JDialog dialog)
  {
    KeyboardFocusManager keyboardManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    keyboardManager.addKeyEventDispatcher(new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED)
        {
          if (e.getKeyCode() == KeyEvent.VK_P && EventUtils.isControlDown(e)) dialog.setVisible(true);
        }
        return false;                                                           // allow the event to be redispatched
      }
    });
  }

//------------------------------------------------------------------------------

  /* Shows a resizable frame with a JDesktopPane inside, which is returned. */

  private JDesktopPane createDesktopFrame(JFrame ownerFrame, LabelLocalizer labelLocalizer, SourceCatalogue catalogue)
  {
    ownerFrame.setTitle(labelLocalizer.getString(DESKTOP_TITLE));

    JDesktopPane desktopPane = new JDesktopPane();
    ownerFrame.getContentPane().add(desktopPane, BorderLayout.CENTER);

    ownerFrame.setSize(DEFAULT_DESKTOP_SIZE.width, DEFAULT_DESKTOP_SIZE.height);
    ownerFrame.setVisible(true);                                                // do this at the end, when the size has been fixed

    return desktopPane;
  }

//------------------------------------------------------------------------------

  /* Returns an invisible frame with a logo already set. */

  private JFrame getNewFrame()
  {
    JFrame frame = new JFrame();
    frame.setIconImage(getDefaultLogo());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    return frame;
  }

//------------------------------------------------------------------------------

  /* Returns the logo used in "normal" application windows. */

  private Image getDefaultLogo()
  {
    return new LogoProducer().createImage(ICON_WIDTH, ICON_HEIGHT);             // dynamically generated every time instead of reading it from file or even caching it: it is not such a big overhead
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /* Abstraction of a frame and a dialog, with the methods required to set them up.
   * Unfortunately, some methods exist in both JFrame and JDialog but with
   * different hierarchy. */

  private static interface ControlsWindow
  {
    Component getComponent();

    void setTitle(String title);

    Container getContentPane();

    void pack();

    void setResizable(boolean resizable);

    void setVisible(boolean visible);
  }

//******************************************************************************

  /* ControlsWindow implementation for a JFrame. */

  private static class FrameControlsWindow implements ControlsWindow
  {
    private final JFrame frame;

    public FrameControlsWindow(JFrame frame)
    {
      this.frame = frame;

      frame.setExtendedState(Frame.NORMAL);                                     // this is forced because some desktop managers maximize all windows by default, and this looks better if really packed
    }

    @Override
    public Component getComponent() {return frame;}

    @Override
    public void setTitle(String title) {frame.setTitle(title);}

    @Override
    public Container getContentPane() {return frame.getContentPane();}

    @Override
    public void pack() {frame.pack();}

    @Override
    public void setResizable(boolean resizable) {frame.setResizable(resizable);}

    @Override
    public void setVisible(boolean visible) {frame.setVisible(visible);}
  }

//******************************************************************************

  /* ControlsWindow implementation for a JDialog. */

  private static class DialogControlsWindow implements ControlsWindow
  {
    private final JDialog dialog;

    public DialogControlsWindow(JDialog dialog)
    {
      this.dialog = dialog;
    }

    @Override
    public Component getComponent() {return dialog;}

    @Override
    public void setTitle(String title) {dialog.setTitle(title);}

    @Override
    public Container getContentPane() {return dialog.getContentPane();}

    @Override
    public void pack() {dialog.pack();}

    @Override
    public void setResizable(boolean resizable) {dialog.setResizable(resizable);}

    @Override
    public void setVisible(boolean visible) {dialog.setVisible(visible);}

  }
}
