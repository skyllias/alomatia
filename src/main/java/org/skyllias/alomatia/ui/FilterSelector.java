
package org.skyllias.alomatia.ui;

import java.awt.event.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;

/** Selector of the filter to apply to the original image.
 *  @deprecated Use FilterSelectorComposer. */

@Deprecated
@SuppressWarnings("serial")
public class FilterSelector extends BasicSelector<NamedFilter>
                            implements ActionListener
{
  protected static final String FILTER_LABEL = "filter.selector.title";

  private FilterableDisplay filterableDisplay;

//==============================================================================

  /** Creates a new selector that will modify the passed display's filter with
   *  one of the items from filterFactory. */

  public FilterSelector(LabelLocalizer localizer, FilterableDisplay imageDisplay,
                        FilterFactory filterFactory)
  {
    super(localizer, FILTER_LABEL);

    filterableDisplay = imageDisplay;

    for (NamedFilter namedFilter : filterFactory.getAllAvailableFilters())      // consider sorting them
    {
      addRadioObject(namedFilter.getNameKey(), namedFilter, this);
    }
  }

//==============================================================================

  /** Changes the selection to the named filter at the passed position, being 0
   *  the first one.
   *  If index is below zero or above the amount of filters, nothing happens. */

  public void selectFilterAt(int index)
  {
    setSelectionByIndex(index);
  }

//------------------------------------------------------------------------------

  /** Sets the selected filter to the image display. */

  @Override
  protected void onSelectionChanged(NamedFilter filter)
  {
    filterableDisplay.setImageFilter(filter);
  }

//------------------------------------------------------------------------------

}
