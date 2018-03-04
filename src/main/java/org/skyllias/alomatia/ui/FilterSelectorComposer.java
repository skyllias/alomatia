
package org.skyllias.alomatia.ui;

import javax.swing.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.ui.RadioSelector.*;

/** Composer of a selector of the filter to apply in a {@link FilterableDisplay}. */

public class FilterSelectorComposer implements RadioSelectorListener<NamedFilter>
{
  protected static final String FILTER_LABEL = "filter.selector.title";

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
    JPanel panel = new BasicControlPanelComposer().getPanel(labelLocalizer.getString(FILTER_LABEL));

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

}
