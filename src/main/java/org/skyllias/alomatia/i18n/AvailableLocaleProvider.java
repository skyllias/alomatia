
package org.skyllias.alomatia.i18n;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

/** Factory of locales selectable to show the UI in.
 *  @deprecated Moved to {@link LabelLocalizer} */

@Deprecated
public class AvailableLocaleProvider
{
  public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

  private String[] langIds = new String[] {DEFAULT_LOCALE.getLanguage(), "es", "el"};   // TODO externalize


//==============================================================================

  /** Returns all the Locales that can be selected by the user.
   *  <p>
   *  Changing the contents of the collection has no effect. */

  public Collection<Locale> getAvailableLocales()
  {
    Collection<Locale> locales = new LinkedList<Locale>();
    for (String currentLangId : langIds)
    {
      Locale currentLocale = new Locale(currentLangId);
      locales.add(currentLocale);
    }
    return locales;
  }

//------------------------------------------------------------------------------

}
