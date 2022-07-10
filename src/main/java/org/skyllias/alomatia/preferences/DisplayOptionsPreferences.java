
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for the display options. */

@Component
public class DisplayOptionsPreferences
{
  private static final boolean SHOW_IMMEDIATELY_DEFAULT = true;

  private static final String PREFKEY_SHOW = "showDialogImmediately";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public boolean isDialogShownImmediately()
  {
    return preferences.getBoolean(PREFKEY_SHOW, SHOW_IMMEDIATELY_DEFAULT);
  }

//------------------------------------------------------------------------------

  public void setDialogShownImmediately(boolean showImmediately)
  {
    preferences.putBoolean(PREFKEY_SHOW, showImmediately);
  }

//------------------------------------------------------------------------------

}
