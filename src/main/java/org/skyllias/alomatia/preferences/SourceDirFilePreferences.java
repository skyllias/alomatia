
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for dir source. */

@Component
public class SourceDirFilePreferences
{
  private static final String PREFKEY_DEFAULTDIR = "defaultSourceDir";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public String getDefaultDirPath()
  {
    return preferences.get(PREFKEY_DEFAULTDIR, null);
  }

//------------------------------------------------------------------------------

  public void setDefaultDirPath(String dirPath)
  {
    preferences.put(PREFKEY_DEFAULTDIR, dirPath);
  }

//------------------------------------------------------------------------------

}
