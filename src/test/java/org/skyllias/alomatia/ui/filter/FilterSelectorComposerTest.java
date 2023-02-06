
package org.skyllias.alomatia.ui.filter;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.concurrent.Callable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JRadioButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.display.FilterableDisplay;
import org.skyllias.alomatia.filter.ColourFilter;
import org.skyllias.alomatia.filter.NamedFilter;
import org.skyllias.alomatia.filter.factory.FilterFactory;
import org.skyllias.alomatia.filter.rgb.BrighterConverter;
import org.skyllias.alomatia.filter.rgb.DarkerConverter;
import org.skyllias.alomatia.i18n.KeyLabelLocalizer;
import org.skyllias.alomatia.ui.component.BarePanelComposer;

@RunWith(MockitoJUnitRunner.class)
public class FilterSelectorComposerTest
{
  private static final String NO_FILTER_NAME      = "filter1";                  // these need not be the same keys used in the property files
  private static final String LIGHTER_FILTER_NAME = "filter2";
  private static final String DARKER_FILTER_NAME  = "filter3";

  private FrameFixture frameFixture;

  @Mock
  private FilterableDisplay filterableDisplay;
  @Mock
  private FilterFactory filterFactory;
  @Mock
  private HistorySuggestionDecorator historySuggestionDecorator;
  @Mock
  private BarePanelComposer bareControlPanelComposer;

  private NamedFilter nullFilter     = new NamedFilter(null,                                      NO_FILTER_NAME);
  private NamedFilter brighterFilter = new NamedFilter(new ColourFilter(new BrighterConverter()), LIGHTER_FILTER_NAME);
  private NamedFilter darkerFilter   = new NamedFilter(new ColourFilter(new DarkerConverter()),   DARKER_FILTER_NAME);

  private FilterSearchHistory filterSearchHistory = mock(FilterSearchHistory.class);

  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp()
  {
    when(filterFactory.getAllAvailableFilters())
        .thenReturn(Arrays.asList(nullFilter, brighterFilter, darkerFilter));

    final FilterSelectorComposer filterSelector = new FilterSelectorComposer(new KeyLabelLocalizer(),
                                                                             filterFactory,
                                                                             filterSearchHistory,
                                                                             historySuggestionDecorator,
                                                                             bareControlPanelComposer);

    JComponent filterPanel = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        when(bareControlPanelComposer.getPanel("filter.selector.title"))
            .thenReturn(new JPanel());

        return filterSelector.createFilterSelector(filterableDisplay).getComponent();
      }
    });
    frameFixture = showInFrame(filterPanel);
  }

  @After
  public void tearDown()
  {
    frameFixture.cleanUp();
  }

//------------------------------------------------------------------------------

  @Test
  public void shouldDecorateTextFieldForSuggestions()
  {
    verify(historySuggestionDecorator).decorate(any(JTextField.class));
  }

  @Test
  public void shouldSetNullFilterWhenVoidOptionSelected()
  {
    JRadioButtonFixture radioButton = getRadioButton(NO_FILTER_NAME);
    radioButton.uncheck();                                                      // always uncheck in case this was the initial selection
    radioButton.check();

    verify(filterableDisplay, times(1)).setImageFilter(nullFilter);
  }

  /* TODO test all filters */

  @Test
  public void shouldSetBrighterFilterWhenLighterOptionSelected()
  {
    JRadioButtonFixture radioButton = getRadioButton(LIGHTER_FILTER_NAME);
    radioButton.uncheck();                                                      // always uncheck in case this was the initial selection
    radioButton.check();

    verify(filterableDisplay, times(1)).setImageFilter(brighterFilter);
  }

  @Test
  public void shouldNotRegisterSearchHistoryWhenRadioSelectedAndEmptyField()
  {
    JRadioButtonFixture radioButton = getRadioButton(LIGHTER_FILTER_NAME);
    radioButton.uncheck();                                                      // always uncheck in case this was the initial selection
    radioButton.check();

    verify(filterSearchHistory, never()).registerSearchString(any(String.class));
  }

  @Test
  public void shouldRegisterSearchHistoryWhenRadioSelectedAndPopulatedField()
  {
    JTextComponentFixture searchField = frameFixture.textBox("filter.selector.search");
    searchField.enterText("filter");

    JRadioButtonFixture radioButton = getRadioButton(LIGHTER_FILTER_NAME);
    radioButton.uncheck();                                                      // always uncheck in case this was the initial selection
    radioButton.check();

    verify(filterSearchHistory).registerSearchString("filter");
  }

  @Test
  public void shouldHideAllWhenNoSearchMatches()
  {
    shouldShowOrHideSearchedFilters("inexisting text", false, false, false);
  }

  @Test
  public void shouldShowAllWhenSearchMatches()
  {
    shouldShowOrHideSearchedFilters("filter", true, true, true);
  }

  @Test
  public void shouldShowAllWhenNoSearch()
  {
    shouldShowOrHideSearchedFilters("", true, true, true);
  }

  @Test
  public void shouldShowSomwWhenSearchMatches()
  {
    shouldShowOrHideSearchedFilters("2", false, true, false);
  }

  private void shouldShowOrHideSearchedFilters(String searchText, boolean nullRadioVisible,
                                               boolean lighterRadioVisible, boolean darkerRadioVisible)
  {
    JRadioButtonFixture radioButton1 = getRadioButton(NO_FILTER_NAME);
    JRadioButtonFixture radioButton2 = getRadioButton(LIGHTER_FILTER_NAME);
    JRadioButtonFixture radioButton3 = getRadioButton(DARKER_FILTER_NAME);

    JTextComponentFixture searchField = frameFixture.textBox("filter.selector.search");
    searchField.enterText(searchText);

    requireVisible(radioButton1, nullRadioVisible);
    requireVisible(radioButton2, lighterRadioVisible);
    requireVisible(radioButton3, darkerRadioVisible);

    verify(filterSearchHistory, never()).registerSearchString(any(String.class));
  }

  private void requireVisible(JRadioButtonFixture radioButton, boolean visible)
  {
    if (visible) radioButton.requireVisible();
    else         radioButton.requireNotVisible();
  }

  private JRadioButtonFixture getRadioButton(String filterName)
  {
    final String SUFFIX_AUTOMATICALLY_ADDED_TO_KEYS = ".name";
    return frameFixture.radioButton(filterName + SUFFIX_AUTOMATICALLY_ADDED_TO_KEYS);
  }
}
