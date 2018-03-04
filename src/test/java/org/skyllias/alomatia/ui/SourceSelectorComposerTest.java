
package org.skyllias.alomatia.ui;

import static org.assertj.swing.fixture.Containers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.*;
import java.util.prefs.*;

import javax.swing.*;

import org.assertj.swing.edt.*;
import org.assertj.swing.fixture.*;
import org.junit.*;
import org.mockito.*;
import org.skyllias.alomatia.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.source.*;

public class SourceSelectorComposerTest
{
  private FrameFixture frameFixture;

  @Mock
  private VoidSource voidSource;
  @Mock
  private DropSource dropSource;
  @Mock
  private ScreenSource screenSource;
  @Mock
  private ClipboardSource clipboardSource;
  @Mock
  private SingleFileSource fileSource;
  @Mock
  private DirFileSource dirSource;
  @Mock
  private AsynchronousUrlSource urlSource;
  @Mock
  private CaptureFrameComposer captureFrameComposer;
  @Mock
  private Preferences preferences;

  private SourceCatalogue catalogue;

  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    catalogue = new SourceCatalogue();
  }

  /* Cannot use @Before because the source selector needs the instances in the
   * catalogue when instantiated. Therefore, this must be called manually in every test. */

  public void setUpAfterCatalogueInitialization()
  {
    final SourceSelectorComposer sourceComposer = new SourceSelectorComposer(preferences, new KeyLabelLocalizer(), catalogue, captureFrameComposer);
    JComponent sourcePanel                      = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        return sourceComposer.getComponent();
      }
    });
    frameFixture = showInFrame(sourcePanel);
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldActivateVoidSourceWhenVoidSelected()
  {
    selectDirectlyActiveSource(VoidSource.class, voidSource, SourceSelectorComposer.NO_SOURCE_LABEL);
  }

  @Test
  public void shouldActivateClipboardSourceWhenClipboardSelected()
  {
    selectDirectlyActiveSource(ClipboardSource.class, clipboardSource, SourceSelectorComposer.CLIPBOARD_SOURCE_LABEL);
  }

  @Test
  public void shouldActivateDropSourceWhenDndSelected()
  {
    selectDirectlyActiveSource(DropSource.class, dropSource, SourceSelectorComposer.DND_SOURCE_LABEL);
  }

  @Test
  public void shouldNotActivateScreenSourceWhenCaptureSelected()
  {
    selectIndirectlyActiveSource(ScreenSource.class, screenSource, SourceSelectorComposer.SCREEN_SOURCE_LABEL);
  }

  @Test
  public void shouldActivateFileSourceWhenFileSelected()
  {
    selectDirectlyActiveSource(SingleFileSource.class, fileSource, SourceSelectorComposer.FILE_SOURCE_LABEL);
  }

  @Test
  public void shouldActivateDirSourceWhenDirSelected()
  {
    selectDirectlyActiveSource(DirFileSource.class, dirSource, SourceSelectorComposer.DIR_SOURCE_LABEL);
  }

  @Test
  public void shouldActivateUrlSourceWhenUrlSelected()
  {
    selectDirectlyActiveSource(AsynchronousUrlSource.class, urlSource, SourceSelectorComposer.URL_SOURCE_LABEL);
  }

  @Test
  public void shouldSetClipboardSourceAutoModeWhenCheckboxSelected()
  {
    doReturn(false).when(preferences).getBoolean(eq(SourceSelectorComposer.PREFKEY_CLIPBOARDAUTO),
                                                 any(Boolean.class));
    catalogue.add(ClipboardSource.class, clipboardSource);
    setUpAfterCatalogueInitialization();

    JRadioButtonFixture radioButton = frameFixture.radioButton(SourceSelectorComposer.CLIPBOARD_SOURCE_LABEL);
    radioButton.uncheck();                                                      // always uncheck in case this was the initial selection
    radioButton.check();
    Mockito.reset(clipboardSource);                                             // bad practice, but the method is called at init time
    JCheckBoxFixture checkBox = frameFixture.checkBox(SourceSelectorComposer.CLIPBOARD_AUTOMODE_NAME);
    checkBox.check(true);

    verify(clipboardSource, atLeastOnce()).setAutoMode(true);
  }

  @Test
  public void shouldSetClipboardSourceNotAutoModeWhenCheckboxUnselected()
  {
    doReturn(true).when(preferences).getBoolean(eq(SourceSelectorComposer.PREFKEY_CLIPBOARDAUTO),
                                                any(Boolean.class));
    catalogue.add(ClipboardSource.class, clipboardSource);
    setUpAfterCatalogueInitialization();

    JRadioButtonFixture radioButton = frameFixture.radioButton(SourceSelectorComposer.CLIPBOARD_SOURCE_LABEL);
    radioButton.uncheck();                                                      // always uncheck in case this was the initial selection
    radioButton.check();
    Mockito.reset(clipboardSource);                                             // bad practice, but the method is called at init time
    JCheckBoxFixture checkBox = frameFixture.checkBox(SourceSelectorComposer.CLIPBOARD_AUTOMODE_NAME);
    checkBox.check(false);

    verify(clipboardSource, atLeastOnce()).setAutoMode(false);
  }

//------------------------------------------------------------------------------

  /* For the sources that become active when the button is selected. */

  private <S extends ImageSource> void selectDirectlyActiveSource(Class<S> sourceClass,
                                                                  S instance, String radioName)
  {
    catalogue.add(sourceClass, instance);
    setUpAfterCatalogueInitialization();

    JRadioButtonFixture radioButton = frameFixture.radioButton(radioName);
    radioButton.uncheck();                                                      // always uncheck in case this was the initial selection
    radioButton.check();
    verify(instance).setActive(true);
  }

  /* For the sources that do not become active when the button is selected. */

  private <S extends ImageSource> void selectIndirectlyActiveSource(Class<S> sourceClass,
                                                                    S instance, String radioName)
  {
    catalogue.add(sourceClass, instance);
    setUpAfterCatalogueInitialization();

    JRadioButtonFixture radioButton = frameFixture.radioButton(radioName);
    radioButton.uncheck();                                                      // always uncheck in case this was the initial selection
    radioButton.check();
    verify(instance, never()).setActive(true);
  }
}
