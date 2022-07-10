
package org.skyllias.alomatia.preferences;

import java.util.prefs.Preferences;

import org.skyllias.alomatia.ui.SourceSelectorComposer;
import org.springframework.stereotype.Component;

/** Preferences for sources.
 *  TODO Split in multiple classes when {@link SourceSelectorComposer} is refactored. */

@Component
public class SourcePreferences
{
  private static final String PREFKEY_SOURCECOMMAND = "sourceCommandName";
  private static final String PREFKEY_CLIPBOARDAUTO = "sourceClipboardAuto";
  private static final String PREFKEY_DEFAULTDIR    = "defaultSourceDir";
  private static final String PREFKEY_DEFAULTFILE   = "defaultSourceFile";


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

}
