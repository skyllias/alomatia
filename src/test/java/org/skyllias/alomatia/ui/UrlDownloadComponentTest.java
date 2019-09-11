
package org.skyllias.alomatia.ui;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.Callable;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skyllias.alomatia.i18n.KeyLabelLocalizer;
import org.skyllias.alomatia.source.AsynchronousUrlSource;
import org.skyllias.alomatia.source.AsynchronousUrlSource.DownloadListener;

/** AssertJ does not support writing strings with chars that are typed with
 *  modifiers (shift, alt, etc.), so there is not much effort placed in restoring
 *  previous values or using realistic URLs. */

public class UrlDownloadComponentTest
{
  private static final String FIELD_NAME  = "url.field";
  private static final String BUTTON_NAME = "download.button";

  private FrameFixture frameFixture;
  @Mock
  private AsynchronousUrlSource source;
  @Mock
  private Preferences preferences;
  private UrlDownloadComponent downloadComponent;

  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    when(preferences.get(eq(UrlDownloadComponent.PREFKEY_DEFAULTURL), any(String.class))).thenReturn(null);

    JPanel container = GuiActionRunner.execute(new Callable<JPanel>()
    {
      @Override
      public JPanel call() throws Exception
      {
        downloadComponent = new UrlDownloadComponent(new KeyLabelLocalizer(), source);
        downloadComponent.setPreferences(preferences);

        JButton button      = downloadComponent.getButton();
        JTextField urlField = downloadComponent.getTextField();
        JPanel container    = new JPanel();
        container.add(button);
        container.add(urlField);
        button.setName(BUTTON_NAME);
        urlField.setName(FIELD_NAME);
        return container;
      }
    });
    frameFixture = showInFrame(container);
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldDisableEverythingWhenStarting()
  {
    frameFixture.textBox(FIELD_NAME).requireDisabled();
    frameFixture.button(BUTTON_NAME).requireDisabled();
    frameFixture.button(BUTTON_NAME).requireText(UrlDownloadComponent.BUTTON_READY_LABEL);
  }

  @Test
  public void shouldEnableEverythingWhenEnabled()
  {
    GuiActionRunner.execute(new GuiTask()
    {
      @Override
      protected void executeInEDT() throws Throwable {downloadComponent.setEnabled(true);}
    });

    frameFixture.textBox(FIELD_NAME).requireEnabled();
    frameFixture.button(BUTTON_NAME).requireEnabled();
  }

  @Test
  public void shouldNotDownloadWhenUrlFieldEmpty()
  {
    GuiActionRunner.execute(new GuiTask()
    {
      @Override
      protected void executeInEDT() throws Throwable
      {
        downloadComponent.setEnabled(true);
        downloadComponent.onSuccess();                                          // simulate the end of the download fired by setEnabled(true)
      }
    });
    frameFixture.textBox(FIELD_NAME).deleteText();
    frameFixture.button(BUTTON_NAME).click();
    verify(source, never()).setUrl(any(String.class), any(DownloadListener.class));
  }

  @Test
  public void shouldDownloadWhenUrlFieldIsNotEmpty()
  {
    GuiActionRunner.execute(new GuiTask()
    {
      @Override
      protected void executeInEDT() throws Throwable
      {
        downloadComponent.setEnabled(true);
        downloadComponent.onSuccess();                                          // simulate the end of the download fired by setEnabled(true)
      }
    });
    frameFixture.textBox(FIELD_NAME).deleteText();
    frameFixture.textBox(FIELD_NAME).enterText("whatever");
    frameFixture.button(BUTTON_NAME).click();

    verify(source, times(1)).setUrl("whatever", downloadComponent);
    frameFixture.button(BUTTON_NAME).requireText(UrlDownloadComponent.BUTTON_CANCEL_LABEL);
  }

  @Test
  public void shouldCancelSourceWhenButtonCancelled()
  {
    GuiActionRunner.execute(new GuiTask()
    {
      @Override
      protected void executeInEDT() throws Throwable
      {
        downloadComponent.setEnabled(true);
        downloadComponent.onSuccess();                                          // simulate the end of the download fired by setEnabled(true)
      }
    });
    frameFixture.textBox(FIELD_NAME).enterText("whatever");
    frameFixture.button(BUTTON_NAME).click();                                   // begin download
    frameFixture.button(BUTTON_NAME).click();                                   // cancel download

    verify(source, times(1)).cancel();
  }
}
