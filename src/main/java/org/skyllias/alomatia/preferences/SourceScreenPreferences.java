
package org.skyllias.alomatia.preferences;

import java.awt.Rectangle;
import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for screen source. */

@Component
public class SourceScreenPreferences
{
  private static final String PREFKEY_SCREEN_RECT_X = "screenRectangleX";
  private static final String PREFKEY_SCREEN_RECT_Y = "screenRectangleY";
  private static final String PREFKEY_SCREEN_RECT_W = "screenRectangleWidth";
  private static final String PREFKEY_SCREEN_RECT_H = "screenRectangleHeight";

  private final Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public Rectangle getScreenRectangle()
  {
    return new Rectangle(preferences.getInt(PREFKEY_SCREEN_RECT_X, 0),
                         preferences.getInt(PREFKEY_SCREEN_RECT_Y, 0),
                         preferences.getInt(PREFKEY_SCREEN_RECT_W, 600),
                         preferences.getInt(PREFKEY_SCREEN_RECT_H, 450));
  }

//------------------------------------------------------------------------------

  public void setScreenRectangle(Rectangle screenRectangle)
  {
    preferences.putInt(PREFKEY_SCREEN_RECT_X, screenRectangle.x);
    preferences.putInt(PREFKEY_SCREEN_RECT_Y, screenRectangle.y);
    preferences.putInt(PREFKEY_SCREEN_RECT_W, screenRectangle.width);
    preferences.putInt(PREFKEY_SCREEN_RECT_H, screenRectangle.height);
  }

//------------------------------------------------------------------------------

}
