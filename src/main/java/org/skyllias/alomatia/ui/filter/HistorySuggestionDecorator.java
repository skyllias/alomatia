
package org.skyllias.alomatia.ui.filter;

import javax.swing.JTextField;

import org.springframework.stereotype.Component;

/** Decorator of text fields that get suggestions from a {@link FilterSearchHistory}. */

@Component
public class HistorySuggestionDecorator
{
  private final FilterSearchHistory filterSearchHistory;

//==============================================================================

  public HistorySuggestionDecorator(FilterSearchHistory filterSearchHistory)
  {
    this.filterSearchHistory = filterSearchHistory;
  }

//==============================================================================

  public void decorate(JTextField inputField)
  {
    new HistorySuggestionDropDownDecoration(inputField, filterSearchHistory);
  }

//------------------------------------------------------------------------------

}
