
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.skyllias.alomatia.display.DisplayFitPolicy;
import org.springframework.stereotype.Component;

/** Preferences for zoom. */

@Component
public class ZoomPreferences
{
  private static final String PREFKEY_FIT  = "fitPolicy";
  private static final String PREFKEY_ZOOM = "zoomSlider";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public int getSliderValue(int defaultValue)
  {
    return preferences.getInt(PREFKEY_ZOOM, defaultValue);
  }

//------------------------------------------------------------------------------

  public void setSilderValue(int amountOfLines)
  {
    preferences.putInt(PREFKEY_ZOOM, amountOfLines);
  }

//------------------------------------------------------------------------------

  public DisplayFitPolicy getDisplayFitPolicy()
  {
    final DisplayFitPolicy DEFAULT_POLICY = DisplayFitPolicy.FREE;
    try {return DisplayFitPolicy.valueOf(preferences.get(PREFKEY_FIT, DEFAULT_POLICY.toString()));}
    catch (Exception e) {return DEFAULT_POLICY;}
  }

//------------------------------------------------------------------------------

  public void setDisplayFitPolicy(DisplayFitPolicy displayFitPolicy)
  {
    preferences.put(PREFKEY_FIT, displayFitPolicy.toString());
  }

//------------------------------------------------------------------------------

}
