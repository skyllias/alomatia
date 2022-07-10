
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for saving files. */

@Component
public class SavePreferences
{
  private static final String USER_HOME_SYSTEM_PROPERTY = "user.home";

  private static final String PREFKEY_DESTINATION = "saveDestinationDirectory";
  private static final String PREFKEY_PROMPT      = "promptFileToSave";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public String getDestinationPath()
  {
    return preferences.get(PREFKEY_DESTINATION,
                           System.getProperty(USER_HOME_SYSTEM_PROPERTY));
  }

//------------------------------------------------------------------------------

  public void setDestinationPath(String path)
  {
    preferences.put(PREFKEY_DESTINATION, path);
  }

//------------------------------------------------------------------------------

  public boolean isPromptOn()
  {
    return preferences.getBoolean(PREFKEY_PROMPT, true);
  }

//------------------------------------------------------------------------------

  public void setPromptOn(boolean askUser)
  {
    preferences.putBoolean(PREFKEY_PROMPT, askUser);
  }

//------------------------------------------------------------------------------

}
