
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for the application locale. */

@Component
public class LocalePreferences
{
  private static final String PREFKEY_NEXTLANG = "nextExecLocale";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public String getNextLanguageCode(String defaultValue)
  {
    return preferences.get(PREFKEY_NEXTLANG, defaultValue);
  }

//------------------------------------------------------------------------------

  public void setNextLanguageCode(String languageCode)
  {
    preferences.put(PREFKEY_NEXTLANG, languageCode);
  }

//------------------------------------------------------------------------------

  public void clearNextLanguageCode()
  {
    preferences.remove(PREFKEY_NEXTLANG);
  }

//------------------------------------------------------------------------------

}
