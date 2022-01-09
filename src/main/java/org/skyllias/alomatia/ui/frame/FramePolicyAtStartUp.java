
package org.skyllias.alomatia.ui.frame;

import java.util.prefs.Preferences;

import org.springframework.stereotype.Component;

/** Configuration for the behaviour when displaying frames.
 *  Maybe in the future this will become an interface, and a bigger variety of
 *  policies are supported. */

@Component
public class FramePolicyAtStartUp
{
  private static final String VM_ARG_INTERNALFRAMES   = "alomatia.frames.inner";
  private static final String PREFKEY_INTERNALFRAMES  = "useInternalFrames";
  private static final boolean DEFAULT_INTERNALFRAMES = false;

  private Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  /** Returns true if display frames should be internal frames, false if they
   *  should be separate windows.
   *  This value is modified by means of
   *  {@link FramePolicyAtStartUp#setUsingInternalFramesNextTime(boolean)}, but
   *  the behaviour should not really change until next execution.
   *  If a VM argument is passed, it's boolean value is used; otherwise, the
   *  preferences are looked up. */

  public boolean isUsingInternalFrames()
  {
    String vmArgument = System.getProperty(VM_ARG_INTERNALFRAMES);
    if (vmArgument != null) return Boolean.parseBoolean(vmArgument);

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

}
