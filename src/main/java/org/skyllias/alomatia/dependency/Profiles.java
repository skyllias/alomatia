
package org.skyllias.alomatia.dependency;

/** Names of the Spring profiles that can be activated. */

public class Profiles
{
  /** Open display frames as internal windows. */

  public static final String INTERNAL_WINDOWS = "internal-windows";

  /** Open display frames as separate windows. */

  public static final String SEPARATE_WINDOWS = "!" + INTERNAL_WINDOWS;
}
