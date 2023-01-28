
package org.skyllias.alomatia.ui.filter;


import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.commons.lang.StringUtils;
import org.skyllias.alomatia.display.FilterableDisplay;
import org.skyllias.alomatia.filter.NamedFilter;
import org.skyllias.alomatia.filter.factory.FilterFactory;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.ui.BarePanelComposer;
import org.skyllias.alomatia.ui.RadioSelector;
import org.skyllias.alomatia.ui.RadioSelector.RadioSelectorListener;

/** Composer of a selector of the filter to apply in a {@link FilterableDisplay}. */

@org.springframework.stereotype.Component
public class FilterSelectorComposer
{
  protected static final String FILTER_LABEL = "filter.selector.title";
  protected static final String SEARCH_LABEL = "filter.selector.search";

  private final FilterSearchHistory filterSearchHistory;
  private final HistorySuggestionDecorator historySuggestionDecorator;

  private final LabelLocalizer labelLocalizer;
  private final BarePanelComposer bareControlPanelComposer;

  private final FilterFactory factory;

//==============================================================================

  /** Creates a new selector that will modify the passed display's filter with
   *  one of the items from filterFactory. */

  public FilterSelectorComposer(LabelLocalizer localizer, FilterFactory filterFactory,
                                FilterSearchHistory filterSearchHistory,
                                HistorySuggestionDecorator historySuggestionDecorator,
                                BarePanelComposer panelComposer)
  {
    labelLocalizer           = localizer;
    factory                  = filterFactory;
    bareControlPanelComposer = panelComposer;

    this.filterSearchHistory        = filterSearchHistory;
    this.historySuggestionDecorator = historySuggestionDecorator;
  }

//==============================================================================

  /** Returns a new component with the filter controls set up for the display. */

  public FilterSelector createFilterSelector(FilterableDisplay imageDisplay)
  {
    final JPanel panel = bareControlPanelComposer.getPanel(labelLocalizer.getString(FILTER_LABEL));

    JTextField searchField = createSearchField(panel);
    panel.add(searchField);


    FilterSelectionListener filterSelectionListener        = new FilterSelectionListener(searchField, imageDisplay, filterSearchHistory);
    RadioSelector<JRadioButton, NamedFilter> radioSelector = new RadioSelector<>(JRadioButton.class, labelLocalizer, filterSelectionListener);

    for (NamedFilter namedFilter : factory.getAllAvailableFilters())
    {
      panel.add(radioSelector.createRadioObject(namedFilter.getNameKey(), namedFilter));
    }

    return new FilterSelector(panel, radioSelector);
  }

//------------------------------------------------------------------------------

  private JTextField createSearchField(final JPanel panel)
  {
    JTextField searchField = new JTextField();
    searchField.setName(SEARCH_LABEL);
    searchField.setToolTipText(labelLocalizer.getString(SEARCH_LABEL));

    searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                             searchField.getPreferredSize().height));       // prevent the containing layout from streching the field vertically

    searchField.getDocument().addDocumentListener(new SearchFieldDocumentListener(panel));

    historySuggestionDecorator.decorate(searchField);

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

  private final class SearchFieldDocumentListener implements DocumentListener
  {
    private final JPanel panel;

    private SearchFieldDocumentListener(JPanel panel)
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

  /* RadioSelectorListener that registers the non-empty contents of a text
   * field in a FilterSearchHistory after updating the filter in a display. */

  private class FilterSelectionListener implements RadioSelectorListener<NamedFilter>
  {
    private final JTextField searchField;
    private final FilterableDisplay filterableDisplay;
    private final FilterSearchHistory filterSearchHistory;

    public FilterSelectionListener(JTextField searchField,
                                   FilterableDisplay filterableDisplay,
                                   FilterSearchHistory filterSearchHistory)
    {
      this.searchField         = searchField;
      this.filterableDisplay   = filterableDisplay;
      this.filterSearchHistory = filterSearchHistory;
    }

    @Override
    public void onSelectionChanged(NamedFilter filter)
    {
      filterableDisplay.setImageFilter(filter);

      String currentSearch = searchField.getText();
      if (StringUtils.isNotBlank(currentSearch)) filterSearchHistory.registerSearchString(currentSearch);
    }
  }

//******************************************************************************

}
