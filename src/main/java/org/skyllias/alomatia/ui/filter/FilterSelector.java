
package org.skyllias.alomatia.ui.filter;

import javax.swing.JComponent;
import javax.swing.JRadioButton;

import org.skyllias.alomatia.filter.NamedFilter;
import org.skyllias.alomatia.ui.radio.RadioSelector;

/** Wrapper of a UI component that allows the selection of filters. */

public class FilterSelector
{
  private final JComponent component;
  private final RadioSelector<JRadioButton, NamedFilter> radioSelector;

//==============================================================================

  public FilterSelector(JComponent component,
                        RadioSelector<JRadioButton, NamedFilter> radioSelector)
  {
    this.component     = component;
    this.radioSelector = radioSelector;
  }

//==============================================================================

  /** Returns the UI component to select named filters, always the same instance. */

  public JComponent getComponent() {return component;}

//------------------------------------------------------------------------------

  /** Changes the selection to the named filter at the passed position, being 0
   *  the first one.
   *  If index is below zero or above the amount of filters, nothing happens. */

  public void selectFilterAt(int index)
  {
    radioSelector.setSelectionByIndex(index);
  }

//------------------------------------------------------------------------------

  /** Changes the selection to the named filter at a position equal to the
   *  currently selected one plus increment, considering index cycles. */

  public void incrementSelectedIndex(int increment)
  {
    radioSelector.setSelectionByIndexIncrement(increment);
  }

//------------------------------------------------------------------------------

}
