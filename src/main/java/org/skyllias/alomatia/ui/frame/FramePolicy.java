
package org.skyllias.alomatia.ui.frame;

import java.util.prefs.*;

/** Configuration for the behaviour when displaying frames.
 *  Maybe in the future this will become an interface, and a bigger variety of
 *  policies are supported. */

public class FramePolicy
{
  private static final String PREFKEY_INTERNALFRAMES  = "useInternalFrames";
  private static final boolean DEFAULT_INTERNALFRAMES = false;

  private Preferences preferences;

//==============================================================================

  public FramePolicy() {this(getDefaultPreferences());}

//------------------------------------------------------------------------------

  /** Just for testing purposes. */

  protected FramePolicy(Preferences prefs) {preferences = prefs;}

//==============================================================================

  /** Returns true if display frames should be internal frames, false if they
   *  should be separate windows.
   *  This value is modified by means of
   *  {@link FramePolicy#setUsingInternalFramesNextTime(boolean)}, but the
   *  behaviour should not really change until next execution. */

  public boolean isUsingInternalFrames()
  {
    return preferences.getBoolean(PREFKEY_INTERNALFRAMES, DEFAULT_INTERNALFRAMES);
  }

//------------------------------------------------------------------------------

  /** Modifies the frames policy returned by {@link #isUsingInternalFrames()},
   *  although it does not have effect until the next execution. */

  public void setUsingInternalFramesNextTime(boolean internal)
  {
    preferences.putBoolean(PREFKEY_INTERNALFRAMES, internal);
  }

//------------------------------------------------------------------------------

  /* Returns the preferences to use when they are not externally injected. */

  private static Preferences getDefaultPreferences() {return Preferences.userNodeForPackage(FramePolicy.class);}

//------------------------------------------------------------------------------

}