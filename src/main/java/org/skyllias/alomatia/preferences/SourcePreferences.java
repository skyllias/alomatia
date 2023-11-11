
package org.skyllias.alomatia.preferences;

import java.awt.Rectangle;
import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Preferences for sources.
 *  TODO Consider splitting. */

@Component
public class SourcePreferences
{
  private static final String PREFKEY_SOURCECOMMAND = "sourceCommandName";
  private static final String PREFKEY_CLIPBOARDAUTO = "sourceClipboardAuto";
  private static final String PREFKEY_DEFAULTDIR    = "defaultSourceDir";
  private static final String PREFKEY_DEFAULTFILE   = "defaultSourceFile";
  private static final String PREFKEY_SCREEN_RECT_X = "screenRectanlgeX";
  private static final String PREFKEY_SCREEN_RECT_Y = "screenRectanlgeY";
  private static final String PREFKEY_SCREEN_RECT_W = "screenRectanlgeWidth";
  private static final String PREFKEY_SCREEN_RECT_H = "screenRectanlgeHeight";

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
