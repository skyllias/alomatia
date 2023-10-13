
package org.skyllias.alomatia.ui;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.KeyEvent;
import java.util.concurrent.Callable;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.assertj.swing.core.KeyPressInfo;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.display.DisplayFitPolicy;
import org.skyllias.alomatia.display.ResizableDisplay;
import org.skyllias.alomatia.i18n.KeyLabelLocalizer;
import org.skyllias.alomatia.preferences.ZoomPreferences;
import org.skyllias.alomatia.ui.component.BarePanelComposer;

@RunWith(MockitoJUnitRunner.class)
public class ZoomSelectorComposerTest
{
  @Spy
  private KeyLabelLocalizer labelLocalizer;

  @Mock
  private BarePanelComposer bareControlPanelComposer;

  @Mock
  private ZoomPreferences zoomPreferences;

  @InjectMocks
  private ZoomSelectorComposer zoomSelectorComposer;

  private FrameFixture frameFixture;


  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp()
  {
    when(zoomPreferences.getSliderValue(0)).thenReturn(50);
    when(zoomPreferences.getDisplayFitPolicy()).thenReturn(DisplayFitPolicy.FREE);
  }

  private ResizableDisplay setUpUi()
  {
    ResizableDisplay resizableDisplay = mock(ResizableDisplay.class);

    JComponent controlPanel = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        when(bareControlPanelComposer.getPanel("zoom.selector.title"))
            .thenReturn(new JPanel());

        return zoomSelectorComposer.getComponent(resizableDisplay);
      }
    });
    frameFixture = showInFrame(controlPanel);

    return resizableDisplay;
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldInitializeSliderWithValuesFromPreferencesWhenFree()
  {
    when(zoomPreferences.getSliderValue(0)).thenReturn(50);
    when(zoomPreferences.getDisplayFitPolicy()).thenReturn(DisplayFitPolicy.FREE);

    ResizableDisplay resizableDisplay = setUpUi();

    frameFixture.slider().requireEnabled();
    verify(resizableDisplay).setFitZoom(DisplayFitPolicy.FREE);
    verify(resizableDisplay).setZoomFactor(2);
  }

  @Test
  public void shouldInitializeSliderWithValuesFromPreferencesWhenNotFree()
  {
    when(zoomPreferences.getSliderValue(0)).thenReturn(50);
    when(zoomPreferences.getDisplayFitPolicy()).thenReturn(DisplayFitPolicy.FULL);

    ResizableDisplay resizableDisplay = setUpUi();

    frameFixture.slider().requireDisabled();
    verify(resizableDisplay).setFitZoom(DisplayFitPolicy.FULL);
    verify(resizableDisplay, never()).setZoomFactor(anyDouble());
  }

  @Test
  public void shouldChangePolicyWithRadioButtons()
  {
    ResizableDisplay resizableDisplay = setUpUi();
    frameFixture.radioButton(ZoomSelectorComposer.FIT_HORIZONTAL_LABEL).click();

    frameFixture.slider().requireDisabled();
    verify(resizableDisplay).setFitZoom(DisplayFitPolicy.HORIZONTAL);
  }

  @Test
  public void shouldChangePolicyWithKeyboard()
  {
    ResizableDisplay resizableDisplay = setUpUi();
    frameFixture.pressAndReleaseKey(KeyPressInfo.keyCode(KeyEvent.VK_4).modifiers(KeyEvent.CTRL_MASK));

    frameFixture.slider().requireDisabled();
    verify(resizableDisplay).setFitZoom(DisplayFitPolicy.LARGEST);
  }

  @Test
  public void shouldChangeZoomWithSlider()
  {
    ResizableDisplay resizableDisplay = setUpUi();
    frameFixture.radioButton(ZoomSelectorComposer.CUSTOM_LABEL).click();
    frameFixture.slider().slideTo(-50);

    frameFixture.slider().requireEnabled();
    verify(resizableDisplay).setZoomFactor(0.5);
  }

  @Test
  public void shouldResetSliderWithDoubleClick()
  {
    ResizableDisplay resizableDisplay = setUpUi();
    frameFixture.radioButton(ZoomSelectorComposer.CUSTOM_LABEL).click();
    frameFixture.slider().doubleClick();

    frameFixture.slider().requireEnabled();
    verify(resizableDisplay).setZoomFactor(1);
  }

}
