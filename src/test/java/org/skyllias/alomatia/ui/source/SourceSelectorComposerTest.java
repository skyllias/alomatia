package org.skyllias.alomatia.ui.source;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.i18n.KeyLabelLocalizer;
import org.skyllias.alomatia.preferences.SourcePreferences;
import org.skyllias.alomatia.ui.component.BarePanelComposer;

@RunWith(MockitoJUnitRunner.class)
public class SourceSelectorComposerTest
{
  @Spy
  private KeyLabelLocalizer labelLocalizer;
  @Mock
  private BarePanelComposer bareControlPanelComposer;
  @Mock
  private SourcePreferences sourcePreferences;

  private SourceSelectorComposer sourceSelectorComposer;

  private FrameFixture frameFixture;


  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  public Map<String, ImageSource> setUp(String sourceCommandName, int amount)
  {
    when(sourcePreferences.getSourceCommandName()).thenReturn(sourceCommandName);

    Map<String, ImageSource> sourcesByKey  = new HashMap<>();
    List<SourceSelectionComposer> selectionComposers = new LinkedList<>();

    for (int i = 0; i < amount; i++)
    {
      String key         = "source" + i;
      ImageSource source = mock(ImageSource.class);

      sourcesByKey.put(key, source);
      selectionComposers.add(mockSelectionComposer(key, source));
    }

    JComponent controlPanel = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        when(bareControlPanelComposer.getPanel("source.selector.title"))
            .thenReturn(new JPanel());

        sourceSelectorComposer = new SourceSelectorComposer(selectionComposers, labelLocalizer,
                                                            sourcePreferences, bareControlPanelComposer);
        return sourceSelectorComposer.getComponent();
      }
    });
    frameFixture = showInFrame(controlPanel);

    return sourcesByKey;
  }

  private SourceSelectionComposer mockSelectionComposer(String key, ImageSource source)
  {
    SourceSelectionComposer composer = mock(SourceSelectionComposer.class);
    when(composer.getSourceKey()).thenReturn(key);
    when(composer.buildSelector()).thenReturn(new SourceSelection()
    {
      @Override
      public ImageSource getSource() {return source;}

      @Override
      public JComponent getControls() {return new JPanel();}
    });

    return composer;
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldSelectFirstIfNoPreferences()
  {
    Map<String, ImageSource> sourcesByKey = setUp(null, 3);

    verify(sourcesByKey.get("source0")).setActive(true);
    verify(sourcesByKey.get("source1"), never()).setActive(true);
    verify(sourcesByKey.get("source2"), never()).setActive(true);

    frameFixture.radioButton("source.source0.name").requireSelected();
  }

  @Test
  public void shouldSelectSomeSourceIfPreferences()
  {
    Map<String, ImageSource> sourcesByKey = setUp("source.source2.name", 3);

    verify(sourcesByKey.get("source0")).setActive(true);
    verify(sourcesByKey.get("source1"), never()).setActive(true);
    verify(sourcesByKey.get("source2")).setActive(true);
    verify(sourcesByKey.get("source0")).setActive(false);

    frameFixture.radioButton("source.source2.name").requireSelected();
  }

  @Test
  public void shouldActivateSourceWhenRadioButtonSelected()
  {
    Map<String, ImageSource> sourcesByKey = setUp(null, 3);

    verify(sourcesByKey.get("source0")).setActive(true);
    verify(sourcesByKey.get("source1"), never()).setActive(true);
    verify(sourcesByKey.get("source2"), never()).setActive(true);

    frameFixture.radioButton("source.source1.name").click();

    verify(sourcesByKey.get("source1")).setActive(true);
    verify(sourcesByKey.get("source0")).setActive(false);
    verify(sourcesByKey.get("source2"), never()).setActive(true);

    frameFixture.radioButton("source.source1.name").requireSelected();
  }

}
