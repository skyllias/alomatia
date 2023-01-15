package org.skyllias.alomatia.ui.source;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.util.concurrent.Callable;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.i18n.KeyLabelLocalizer;
import org.skyllias.alomatia.source.ScreenSource;
import org.skyllias.alomatia.source.ScreenSource.ScreenRectangle;
import org.skyllias.alomatia.ui.CaptureFrameComposer;
import org.skyllias.alomatia.ui.CaptureFrameComposer.CaptureBoundsListener;

@RunWith(MockitoJUnitRunner.class)
public class ScreenSourceSelectionComposerTest
{
  @Mock
  private ScreenSource screenSource;
  @Mock
  private CaptureFrameComposer captureFrameComposer;
  @Spy
  private KeyLabelLocalizer labelLocalizer;

  @InjectMocks
  private ScreenSourceSelectionComposer screenSourceSelectionComposer;

  @Captor
  private ArgumentCaptor<CaptureBoundsListener> captureBoundsListenerCaptor;

  private FrameFixture frameFixture;

  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  private SourceSelection setUpUi()
  {
    SourceSelection sourceSelection = GuiActionRunner.execute(new Callable<SourceSelection>()
    {
      @Override
      public SourceSelection call() throws Exception
      {
        return screenSourceSelectionComposer.buildSelector();
      }
    });
    frameFixture = showInFrame(sourceSelection.getControls());

    return sourceSelection;
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldStartDisabled()
  {
    setUpUi();

    getCaptureButton().requireDisabled();
  }

  @Test
  public void shouldEnableButtonWhenSourceActivated()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getCaptureButton().requireEnabled();
    verify(screenSource, never()).setActive(true);
  }

  @Test
  public void shouldDisableButtonWhenSourceDeactivated()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(false));

    getCaptureButton().requireDisabled();
    verify(screenSource).setActive(false);
  }

  @Test
  public void shouldOpenCaptureFrameWhenButtonClicked()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getCaptureButton().click();
    verify(screenSource).setActive(false);
    verify(captureFrameComposer).openNewFrame(any());
  }

  @Test
  public void shouldActivateSourceWhenSelectionCompleted() throws Exception
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getCaptureButton().click();

    verify(captureFrameComposer).openNewFrame(captureBoundsListenerCaptor.capture());
    ScreenRectangle screenRectangle = new ScreenRectangle(mock(GraphicsDevice.class), new Rectangle());
    captureBoundsListenerCaptor.getValue().boundsSelected(screenRectangle);
    verify(screenSource).setScreenBounds(screenRectangle);
    verify(screenSource).setActive(true);
  }


  private JButtonFixture getCaptureButton()
  {
    return frameFixture.button(ScreenSourceSelectionComposer.CAPTURE_FRAME_BUTTON_NAME);
  }

}
