
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for clipboard source. */

@Component
public class SourceClipboardPreferences
{
  private static final String PREFKEY_CLIPBOARDAUTO = "sourceClipboardAuto";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public boolean isClipboardAutoMode()
  {
    return preferences.getBoolean(PREFKEY_CLIPBOARDAUTO, true);
  }

//------------------------------------------------------------------------------

  public void setClipboardAutoMode(boolean autoMode)
  {
    preferences.putBoolean(PREFKEY_CLIPBOARDAUTO, autoMode);
  }

//------------------------------------------------------------------------------

}
