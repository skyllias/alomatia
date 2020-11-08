
package org.skyllias.alomatia.ui.filter;


import java.awt.Component;
import java.awt.Dimension;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.commons.lang.StringUtils;
import org.skyllias.alomatia.display.FilterableDisplay;
import org.skyllias.alomatia.filter.FilterFactory;
import org.skyllias.alomatia.filter.NamedFilter;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.ui.BasicControlPanelComposer;
import org.skyllias.alomatia.ui.RadioSelector;
import org.skyllias.alomatia.ui.RadioSelector.RadioSelectorListener;

/** Composer of a selector of the filter to apply in a {@link FilterableDisplay}. */

public class FilterSelectorComposer implements RadioSelectorListener<NamedFilter>
{
  protected static final String FILTER_LABEL = "filter.selector.title";
  protected static final String SEARCH_LABEL = "filter.selector.search";

  private final FilterSearchHistoryFactory filterSearchHistoryFactory;
  private final HistorySuggestionDecorator historySuggestionDecorator;

  private final LabelLocalizer labelLocalizer;

  private final FilterableDisplay filterableDisplay;
  private final FilterFactory factory;

  private final Collection<FilterSelectionListener> filterSelectionListeners = new LinkedList<>();

//==============================================================================

  /** Creates a new selector that will modify the passed display's filter with
   *  one of the items from filterFactory. */

  public FilterSelectorComposer(LabelLocalizer localizer, FilterableDisplay imageDisplay,
                                FilterFactory filterFactory)
  {
    this(localizer, imageDisplay, filterFactory,
         new FilterSearchHistoryFactory(), new HistorySuggestionDecorator());
  }

//------------------------------------------------------------------------------

  protected FilterSelectorComposer(LabelLocalizer localizer, FilterableDisplay imageDisplay,
                                   FilterFactory filterFactory,
                                   FilterSearchHistoryFactory filterSearchHistoryFactory,
                                   HistorySuggestionDecorator historySuggestionDecorator)
  {
    labelLocalizer    = localizer;
    filterableDisplay = imageDisplay;
    factory           = filterFactory;

    this.filterSearchHistoryFactory = filterSearchHistoryFactory;
    this.historySuggestionDecorator = historySuggestionDecorator;
  }

//==============================================================================

  /** Returns a new component with the filter controls set up. */

  public FilterSelector createFilterSelector()
  {
    final JPanel panel = new BasicControlPanelComposer().getPanel(labelLocalizer.getString(FILTER_LABEL));

    JTextField searchField = createSearchField(panel);
    panel.add(searchField);

    RadioSelector<JRadioButton, NamedFilter> radioSelector = new RadioSelector<>(JRadioButton.class, labelLocalizer, this);

    for (NamedFilter namedFilter : factory.getAllAvailableFilters())            // consider sorting them
    {
      panel.add(radioSelector.createRadioObject(namedFilter.getNameKey(), namedFilter));
    }

    return new FilterSelector(panel, radioSelector);
  }

//------------------------------------------------------------------------------

  /** Sets the selected filter to the image display. */

  @Override
  public void onSelectionChanged(NamedFilter filter)
  {
    filterableDisplay.setImageFilter(filter);

    for (FilterSelectionListener filterSelectionListener : filterSelectionListeners)
    {
      filterSelectionListener.onFilterSelected();
    }
  }

//------------------------------------------------------------------------------

  private JTextField createSearchField(final JPanel panel)
  {
    JTextField searchField = new JTextField();
    searchField.setName(SEARCH_LABEL);
    searchField.setToolTipText(labelLocalizer.getString(SEARCH_LABEL));

    searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                             searchField.getPreferredSize().height));       // prevent the containing layout from streching the field vertically

    searchField.getDocument().addDocumentListener(new SearchFieldListener(panel));

    FilterSearchHistory filterSearchHistory = filterSearchHistoryFactory.newInstance();

    historySuggestionDecorator.decorate(searchField, filterSearchHistory);

    filterSelectionListeners.add(new FilterSearchHistoryUpdateListener(searchField, filterSearchHistory));

    return searchField;
  }

//------------------------------------------------------------------------------

  /* Loops over all the radio buttons in panel and hides those that do not
   * contain text in their label or action command, showing the others. */

  private void searchFilters(String text, JPanel panel)
  {
    String caselessText = text.toLowerCase();

    for (Component childComponent : panel.getComponents())
    {
      if (childComponent instanceof JRadioButton)
      {
        JRadioButton radioButton = (JRadioButton) childComponent;
        String caselessLabel     = radioButton.getText().toLowerCase();
        String caselessCommand   = radioButton.getActionCommand().toLowerCase();

        boolean matching = caselessLabel.contains(caselessText) ||
                           caselessCommand.contains(caselessText);
        radioButton.setVisible(matching);
      }
    }
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private final class SearchFieldListener implements DocumentListener
  {
    private final JPanel panel;

    private SearchFieldListener(JPanel panel)
    {
      this.panel = panel;
    }

    @Override
    public void removeUpdate(DocumentEvent e) {searchFilters(e);}

    @Override
    public void insertUpdate(DocumentEvent e) {searchFilters(e);}

    @Override
    public void changedUpdate(DocumentEvent e) {searchFilters(e);}

    private void searchFilters(DocumentEvent e)
    {
      try
      {
        Document document = e.getDocument();
        int textLength    = document.getLength();
        String fullText   = document.getText(0, textLength);
        FilterSelectorComposer.this.searchFilters(fullText, panel);
      }
      catch (BadLocationException ble) {}                                       // TODO manage exceptions
    }
  }

//******************************************************************************

  /* Interface to get notified whenever a new filter is selected. */

  private static interface FilterSelectionListener
  {
    void onFilterSelected();
  }

//******************************************************************************

  /* FilterSelectionListener that registers the non-empty contents of a text
   * field in a FilterSearchHistory. */

  private static class FilterSearchHistoryUpdateListener implements FilterSelectionListener
  {
    private final JTextField searchField;
    private final FilterSearchHistory filterSearchHistory;

    public FilterSearchHistoryUpdateListener(JTextField searchField,
                                             FilterSearchHistory filterSearchHistory)
    {
      this.searchField         = searchField;
      this.filterSearchHistory = filterSearchHistory;
    }

    @Override
    public void onFilterSelected()
    {
      String currentSearch = searchField.getText();
      if (StringUtils.isNotBlank(currentSearch)) filterSearchHistory.registerSearchString(currentSearch);
    }
  }

//******************************************************************************

}
