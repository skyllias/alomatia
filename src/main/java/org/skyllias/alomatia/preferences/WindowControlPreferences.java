
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for the window management. */

@Component
public class WindowControlPreferences
{
  private static final String PREFKEY_HORIZONTAL  = "arrangeHorizontally";
  private static final String PREFKEY_LINES       = "amountOfLinesToArrangeIn";
  private static final String PREFKEY_APPLYFILTER = "applySequentialFilterToNewWindow";
  private static final String PREFKEY_AUTOOPEN    = "automaticallyOpenNewWindowOnStartup";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public boolean isAutoOpenWindowOnStartup()
  {
    return preferences.getBoolean(PREFKEY_AUTOOPEN, false);
  }

//------------------------------------------------------------------------------

  public void setAutoOpenWindowOnStartup(boolean autoOpen)
  {
    preferences.putBoolean(PREFKEY_AUTOOPEN, autoOpen);
  }

//------------------------------------------------------------------------------

  public boolean isSequentialFilterApplied()
  {
    return preferences.getBoolean(PREFKEY_APPLYFILTER, false);
  }

//------------------------------------------------------------------------------

  public void setSequentialFilterApplied(boolean apply)
  {
    preferences.putBoolean(PREFKEY_APPLYFILTER, apply);
  }

//------------------------------------------------------------------------------

  public boolean isHorizontallyArranged()
  {
    return preferences.getBoolean(PREFKEY_HORIZONTAL, true);
  }

//------------------------------------------------------------------------------

  public void setHorizontallyArranged(boolean horizontal)
  {
    preferences.putBoolean(PREFKEY_HORIZONTAL, horizontal);
  }

//------------------------------------------------------------------------------

  public int getAmountOfLinesToArrangeIn()
  {
    return preferences.getInt(PREFKEY_LINES, 1);
  }

//------------------------------------------------------------------------------

  public void setAmountOfLinesToArrangeIn(int amountOfLines)
  {
    preferences.putInt(PREFKEY_LINES, amountOfLines);
  }

//------------------------------------------------------------------------------

}
