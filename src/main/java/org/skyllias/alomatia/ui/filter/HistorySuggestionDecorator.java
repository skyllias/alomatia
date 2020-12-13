
package org.skyllias.alomatia.ui.filter;

import javax.swing.JTextField;

/** Decorator of text fields that get suggestions from a {@link FilterSearchHistory}. */

public class HistorySuggestionDecorator
{
//==============================================================================

  public void decorate(JTextField inputField, FilterSearchHistory filterSearchHistory)
  {
    new HistorySuggestionDropDownDecoration(inputField, filterSearchHistory);
  }

//------------------------------------------------------------------------------

}
