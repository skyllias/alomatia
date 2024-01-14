
package org.skyllias.alomatia.ui.source;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.event.KeyEvent;
import java.util.concurrent.Callable;

import org.assertj.swing.core.KeyPressInfo;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
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
import org.skyllias.alomatia.preferences.SourceDownloadPreferences;
import org.skyllias.alomatia.source.AsynchronousUrlSource;
import org.skyllias.alomatia.source.AsynchronousUrlSource.DownloadListener;
import org.skyllias.alomatia.source.AsynchronousUrlSource.DownloadListener.ErrorType;

/* Although there should not be any difference, real URLs are not used because
 * AssertJ does not support writing strings with chars that are typed with
 *  modifiers (shift, alt, etc.). */

@RunWith(MockitoJUnitRunner.class)
public class UrlDownloadSourceSelectionComposerTest
{
  @Mock
  private AsynchronousUrlSource urlSource;
  @Mock
  private SourceDownloadPreferences preferences;
  @Spy
  private KeyLabelLocalizer labelLocalizer;

  @InjectMocks
  private UrlDownloadSourceSelectionComposer urlDownloadSourceSelectionComposer;

  @Captor
  private ArgumentCaptor<DownloadListener> downloadListenerCaptor;

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
        return urlDownloadSourceSelectionComposer.buildSelector();
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

    getField().requireText("");
    getField().requireDisabled();
    getButton().requireDisabled();
  }

  @Test
  public void shouldInitializeFieldIfUrlInPreferences()
  {
    when(preferences.getLastUrl()).thenReturn("url from preferences");

    setUpUi();

    getField().requireText("url from preferences");
    getField().requireDisabled();
    getButton().requireDisabled();
  }

  @Test
  public void shouldEnableControlsWhenSourceActivated()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getField().requireEnabled();
    getButton().requireEnabled();
    verify(urlSource).setActive(true);
  }

  @Test
  public void shouldDisableControlsWhenSourceDeactivated()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(false));

    getField().requireDisabled();
    getButton().requireDisabled();
    verify(urlSource).setActive(false);
  }

  @Test
  public void shouldNotStartDownloadWhenFieldEmpty()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getButton().click();

    verify(urlSource, never()).setUrl(any(), any());
    getButton().requireText("source.selector.url.button.ready");
  }

  @Test
  public void shouldStartDownloadWhenUrlAvailable()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getField().enterText("url to download");
    getButton().click();

    verify(urlSource).setUrl(eq("url to download"), any());
    verify(urlSource, never()).cancel();
    verify(preferences).setLastUrl("url to download");
    getButton().requireText("source.selector.url.button.cancel");
    getField().requireToolTip((String) null);
  }

  @Test
  public void shouldStartDownloadOnEnterPressToo()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getField().enterText("url to download");
    getField().pressAndReleaseKey(KeyPressInfo.keyCode(KeyEvent.VK_ENTER));

    verify(urlSource).setUrl(eq("url to download"), any());
    verify(urlSource, never()).cancel();
    verify(preferences).setLastUrl("url to download");
    getButton().requireText("source.selector.url.button.cancel");
    getField().requireToolTip((String) null);
  }

  @Test
  public void shouldCancelDownloadWhenButtonClickedWhileDownloading()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getField().enterText("url to download");
    getButton().click();
    getButton().click();

    verify(urlSource).setUrl(eq("url to download"), any());
    verify(urlSource).cancel();
    getButton().requireText("source.selector.url.button.ready");
    getField().requireToolTip((String) null);
    getField().requireFocused();
  }

  @Test
  public void shouldBeReadyAfterSuccess()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getField().enterText("url to download");
    getButton().click();

    verify(urlSource).setUrl(eq("url to download"), downloadListenerCaptor.capture());
    downloadListenerCaptor.getValue().onSuccess();

    getButton().requireText("source.selector.url.button.ready");
    getField().requireToolTip((String) null);
  }

  @Test
  public void shouldShowErrorTooltipAfterFailure()
  {
    SourceSelection sourceSelection = setUpUi();
    GuiActionRunner.execute(() -> sourceSelection.getSource().setActive(true));

    getField().enterText("url to download");
    getButton().click();

    verify(urlSource).setUrl(eq("url to download"), downloadListenerCaptor.capture());
    downloadListenerCaptor.getValue().onError(ErrorType.IMAGE);

    getButton().requireText("source.selector.url.button.ready");
    getField().requireToolTip("source.selector.url.field.tooltip.error.image");
  }


  private JTextComponentFixture getField()
  {
    return frameFixture.textBox(UrlDownloadSourceSelectionComposer.FIELD_NAME);
  }

  private JButtonFixture getButton()
  {
    return frameFixture.button(UrlDownloadSourceSelectionComposer.BUTTON_NAME);
  }

}
