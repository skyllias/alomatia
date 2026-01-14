
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for single file source. */

@Component
public class SourceSingleFilePreferences
{
  private static final String PREFKEY_DEFAULTFILE = "defaultSourceFile";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public String getDefaultFilePath()
  {
    return preferences.get(PREFKEY_DEFAULTFILE, null);
  }

//------------------------------------------------------------------------------

  public void setDefaultFilePath(String filePath)
  {
    preferences.put(PREFKEY_DEFAULTFILE, filePath);
  }

//------------------------------------------------------------------------------

}
