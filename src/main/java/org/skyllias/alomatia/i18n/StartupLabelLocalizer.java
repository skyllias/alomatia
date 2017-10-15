
package org.skyllias.alomatia.i18n;

import java.util.*;
import java.util.prefs.*;

import javax.swing.*;

/** LabelLocalizer that applies the same locale over a JVM, retrieving it from
 *  the user properties.
 *  <p>
 *  The ResourceBundle manipulation is delegated to {@link UIManager}.
 *  <p>
 *  Due to the lack of out-of-the-box support for Locale modification in Swing
 *  (it is not impossible to achieve but it requires an effort not worth it),
 *  the language cannot be changed at runtime. Instead, a language can be
 *  selected in order to be used the next time the application is run. */

public class StartupLabelLocalizer implements LabelLocalizer
{
  private static final String RESOURCE_BUNDLE_NAME = "org.skyllias.alomatia.Labels";

  private static final String PREFKEY_NEXTLANG = "nextExecLocale";

//==============================================================================

  /* Retrieves the locale to use from the preferences. If not found, the default
   * locale will be used.
   * All operations are delegated to UIManager and its UIDefaults. */

  static
  {
    UIManager.getDefaults().addResourceBundle(RESOURCE_BUNDLE_NAME);
    loadLocaleAsAtStartup();

  }

//==============================================================================

  @Override
  public String getString(String key)
  {
    String localizedValue = UIManager.getString(key);
    if (localizedValue == null) return key;
    else                        return localizedValue;
  }

//------------------------------------------------------------------------------

  @Override
  public Locale getCurrentLocale()
  {
    return UIManager.getDefaults().getDefaultLocale();
  }

//------------------------------------------------------------------------------

  /** Stores the passed locale so that it is used the next time the application is run.
   *  <p>
   *  The current application is unaffected. */

  @Override
  public void setLocale(Locale nextLocale)
  {
    if (nextLocale == null) getPreferences().remove(PREFKEY_NEXTLANG);
    else                    getPreferences().put(PREFKEY_NEXTLANG, nextLocale.getLanguage());
  }

//------------------------------------------------------------------------------

  /** Sets the language to use to the value in the preferences (or the system's
   *  default if there are no preferences yet).
   *  <p>
   *  This should only be called once per execution, or else there is the danger
   *  to get UI labels with mixed languages. However, it is made semi-publicly
   *  available (protected) so that it can be invoked to simulate a new language
   *  whenever that is really required and its consequences are fully assumed. */

  protected void resetLocaleAsAtStartup()
  {
    loadLocaleAsAtStartup();
  }

//------------------------------------------------------------------------------

  /* Sets the language to use to the value in the preferences (or the system's
   * default if there are no preferences yet). */

  private static void loadLocaleAsAtStartup()
  {
    String defaultLangId = AvailableLocaleProvider.DEFAULT_LOCALE.getLanguage();
    String languageId    = getPreferences().get(PREFKEY_NEXTLANG, defaultLangId);
    Locale languageToUse = new Locale(languageId);
    UIManager.getDefaults().setDefaultLocale(languageToUse);
    Locale.setDefault(languageToUse);                                           // for some reason, Swing built-in components do not use UIManager defaults but this one
  }

//------------------------------------------------------------------------------

  /* Returns the preferences where the selection is stored. */

  private static Preferences getPreferences() {return Preferences.userNodeForPackage(LabelLocalizer.class);}

//------------------------------------------------------------------------------

}
