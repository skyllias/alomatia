package org.skyllias.alomatia.ui.source;

import java.awt.AWTException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.source.ScreenSource;
import org.skyllias.alomatia.source.ScreenSource.ScreenRectangle;
import org.skyllias.alomatia.ui.CaptureFrameComposer;
import org.skyllias.alomatia.ui.CaptureFrameComposer.CaptureBoundsListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Composer for a selector of {@link ScreenSource}. */

@Component
@Order(3)
public class ScreenSourceSelectionComposer implements SourceSelectionComposer
{
  private static final String SOURCE_KEY = "screen";

  private static final String CAPTURE_LABEL = "source.selector.screen.button";

  protected static final String CAPTURE_FRAME_BUTTON_NAME = "button.capture";

  private final ScreenSource screenSource;
  private final CaptureFrameComposer captureFrameComposer;
  private final LabelLocalizer labelLocalizer;

//==============================================================================

  public ScreenSourceSelectionComposer(ScreenSource screenSource,
                                       CaptureFrameComposer captureFrameComposer,
                                       LabelLocalizer labelLocalizer)
  {
    this.screenSource         = screenSource;
    this.captureFrameComposer = captureFrameComposer;
    this.labelLocalizer       = labelLocalizer;
  }

//==============================================================================

  @Override
  public SourceSelection buildSelector()
  {
    return new ScreenSourceSelection();
  }

//------------------------------------------------------------------------------

  @Override
  public String getSourceKey() {return SOURCE_KEY;}

//------------------------------------------------------------------------------

//******************************************************************************

  private class ScreenSourceSelection implements SourceSelection
  {
    private final JButton captureSelectorButton;
    private final JPanel controlsPanel;

    public ScreenSourceSelection()
    {
      captureSelectorButton = buildCaptureSelectorButton();

      controlsPanel = new JPanel();
      controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));

      controlsPanel.add(captureSelectorButton);
      controlsPanel.add(Box.createHorizontalGlue());
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
          captureSelectorButton.setEnabled(active);
          if (!active) screenSource.setActive(false);
        }
      };
    }

    @Override
    public JComponent getControls() {return controlsPanel;}

    private JButton buildCaptureSelectorButton()
    {
      JButton button = new JButton(labelLocalizer.getString(CAPTURE_LABEL));
      button.setName(CAPTURE_FRAME_BUTTON_NAME);
      button.setEnabled(false);
      button.addActionListener(new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          CaptureFrameListener captureListener = new CaptureFrameListener();

          screenSource.setActive(false);                                        // always disable capture when the capture frame is open
          captureFrameComposer.openNewFrame(captureListener);
        }
      });

      return button;
    }
  }

//******************************************************************************

  /* Listener to invoke when the CaptureFrame button is clicked. */

  private class CaptureFrameListener implements CaptureBoundsListener
  {
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

}
