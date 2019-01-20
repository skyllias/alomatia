
package org.skyllias.alomatia.ui;


import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.skyllias.alomatia.display.FilterableDisplay;
import org.skyllias.alomatia.filter.FilterFactory;
import org.skyllias.alomatia.filter.NamedFilter;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.ui.RadioSelector.RadioSelectorListener;

/** Composer of a selector of the filter to apply in a {@link FilterableDisplay}. */

public class FilterSelectorComposer implements RadioSelectorListener<NamedFilter>
{
  protected static final String FILTER_LABEL = "filter.selector.title";
  protected static final String SEARCH_LABEL = "filter.selector.search";

  private final LabelLocalizer labelLocalizer;

  private final FilterableDisplay filterableDisplay;
  private final FilterFactory factory;

  private RadioSelector<NamedFilter> radioSelector;

//==============================================================================

  /** Creates a new selector that will modify the passed display's filter with
   *  one of the items from filterFactory. */

  public FilterSelectorComposer(LabelLocalizer localizer, FilterableDisplay imageDisplay,
                                FilterFactory filterFactory)
  {
    labelLocalizer    = localizer;
    filterableDisplay = imageDisplay;
    factory           = filterFactory;

    radioSelector = new RadioSelector<>(labelLocalizer, this);
  }

//==============================================================================

  /** Returns a component with the filter controls set up. */

  public JComponent getComponent()
  {
    final JPanel panel = new BasicControlPanelComposer().getPanel(labelLocalizer.getString(FILTER_LABEL));

    JTextField searchField = new JTextField();
    searchField.setName(SEARCH_LABEL);
    searchField.setToolTipText(labelLocalizer.getString(SEARCH_LABEL));
    searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                             searchField.getPreferredSize().height));       // prevent the containing layout from streching the field vertically
    searchField.getDocument().addDocumentListener(new SearchFieldListener(panel));
    panel.add(searchField);
    
    for (NamedFilter namedFilter : factory.getAllAvailableFilters())            // consider sorting them
    {
      panel.add(radioSelector.createRadioObject(namedFilter.getNameKey(), namedFilter));
    }

    return panel;
  }

//------------------------------------------------------------------------------

  /** Changes the selection to the named filter at the passed position, being 0
   *  the first one.
   *  If index is below zero or above the amount of filters, nothing happens. */

  public void selectFilterAt(int index)
  {
    radioSelector.setSelectionByIndex(index);
  }

//------------------------------------------------------------------------------

  /** Sets the selected filter to the image display. */

  @Override
  public void onSelectionChanged(NamedFilter filter)
  {
    filterableDisplay.setImageFilter(filter);
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

}
