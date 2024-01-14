
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for URL downloads. */

@Component
public class SourceDownloadPreferences
{
  private static final String PREFKEY_LASTURL = "lastUrl";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public String getLastUrl()
  {
    return preferences.get(PREFKEY_LASTURL, null);
  }

//------------------------------------------------------------------------------

  public void setLastUrl(String lastUrl)
  {
    preferences.put(PREFKEY_LASTURL, lastUrl);
  }

//------------------------------------------------------------------------------

}
