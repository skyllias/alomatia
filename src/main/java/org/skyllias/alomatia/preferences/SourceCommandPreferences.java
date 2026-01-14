
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for the active source. */

@Component
public class SourceCommandPreferences
{
  private static final String PREFKEY_SOURCECOMMAND = "sourceCommandName";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public String getSourceCommandName()
  {
    return preferences.get(PREFKEY_SOURCECOMMAND, null);
  }

//------------------------------------------------------------------------------

  public void setSourceCommandName(String sourceCommandName)
  {
    preferences.put(PREFKEY_SOURCECOMMAND, sourceCommandName);
  }

//------------------------------------------------------------------------------

}
