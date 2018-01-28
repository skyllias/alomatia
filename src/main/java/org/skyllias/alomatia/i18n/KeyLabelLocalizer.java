
package org.skyllias.alomatia.i18n;

import java.util.*;

/** Dummy localizer that always returns the key and the same locale.
 *  For stubbing purposes. */

public class KeyLabelLocalizer implements LabelLocalizer
{
//==============================================================================

  @Override
  public String getString(String key) {return key;}

//------------------------------------------------------------------------------

  @Override
  public Locale getCurrentLocale() {return Locale.getDefault();}

//------------------------------------------------------------------------------

  @Override
  public void setLocale(Locale nextLocale) {}

//------------------------------------------------------------------------------

  @Override
  public Collection<Locale> getAvailableLocales() {return new LinkedList<>();}

//------------------------------------------------------------------------------

}
