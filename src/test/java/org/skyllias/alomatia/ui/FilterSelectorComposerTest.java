
package org.skyllias.alomatia.ui;

import static org.assertj.swing.fixture.Containers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.*;

import javax.swing.*;

import org.assertj.swing.edt.*;
import org.assertj.swing.fixture.*;
import org.junit.*;
import org.mockito.*;
import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.filter.rgb.*;
import org.skyllias.alomatia.i18n.*;

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

  private NamedFilter nullFilter     = new NamedFilter(null,                 NO_FILTER_NAME);
  private NamedFilter brighterFilter = new NamedFilter(new BrighterFilter(), LIGHTER_FILTER_NAME);
  private NamedFilter darkerFilter   = new NamedFilter(new DarkerFilter(),   DARKER_FILTER_NAME);

  @BeforeClass
  public static void setUpOnce()
  {
    FailOnThreadViolationRepaintManager.install();
  }

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    when(filterFactory.getAllAvailableFilters()).
         thenReturn(Arrays.asList(new NamedFilter[] {nullFilter,
                                                     brighterFilter,
                                                     darkerFilter}));

    final FilterSelectorComposer filterSelector = new FilterSelectorComposer(new KeyLabelLocalizer(), filterableDisplay, filterFactory);

    JComponent filterPanel = GuiActionRunner.execute(new Callable<JComponent>()
    {
      @Override
      public JComponent call() throws Exception
      {
        return filterSelector.getComponent();
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
  public void shouldSetNullFilterWhenVoidOptionSelected()
  {
    JRadioButtonFixture radioButton = frameFixture.radioButton(NO_FILTER_NAME);
    radioButton.uncheck();                                                      // always uncheck in case this was the initial selection
    radioButton.check();
    verify(filterableDisplay, times(1)).setImageFilter(nullFilter);
  }

  /* TODO test all filters */
  
  @Test
  public void shouldSetBrighterFilterWhenLighterOptionSelected()
  {
    JRadioButtonFixture radioButton = frameFixture.radioButton(LIGHTER_FILTER_NAME);
    radioButton.uncheck();                                                      // always uncheck in case this was the initial selection
    radioButton.check();
    verify(filterableDisplay, times(1)).setImageFilter(brighterFilter);
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
    JRadioButtonFixture radioButton1 = frameFixture.radioButton(NO_FILTER_NAME);
    JRadioButtonFixture radioButton2 = frameFixture.radioButton(LIGHTER_FILTER_NAME);
    JRadioButtonFixture radioButton3 = frameFixture.radioButton(DARKER_FILTER_NAME);
    
    JTextComponentFixture searchField = frameFixture.textBox("filter.selector.search");
    searchField.enterText(searchText);
    
    requireVisible(radioButton1, nullRadioVisible);
    requireVisible(radioButton2, lighterRadioVisible);
    requireVisible(radioButton3, darkerRadioVisible);
  }

  private void requireVisible(JRadioButtonFixture radioButton, boolean visible)
  {
    if (visible) radioButton.requireVisible();
    else         radioButton.requireNotVisible();
  }
}
