
package org.skyllias.alomatia.i18n;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.UIManager;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

/** LabelLocalizer that applies the same locale over a JVM, retrieving it from
 *  the user properties. It may be overriden by means of VM arguments.
 *  <p>
 *  The ResourceBundle manipulation is delegated to {@link UIManager}.
 *  <p>
 *  Due to the lack of out-of-the-box support for Locale modification in Swing
 *  (it is not impossible to achieve but it requires an effort not worth it),
 *  the language cannot be changed at runtime. Instead, a language can be
 *  selected in order to be used the next time the application is run. */

@Component
public class StartupLabelLocalizer implements LabelLocalizer
{
  protected static final String PREFKEY_NEXTLANG = "nextExecLocale";

  private static final String VM_ARG_LANGUAGE = "alomatia.language";

  private static final Pattern PROPERTIES_FILE_NAME_PATTERN          = Pattern.compile("Labels_(.*)\\.properties");             // all these three strings must be modified consistently
  private static final String LOCALIZED_PROPERTIES_PATHS_ANT_PATTERN = "classpath*:org/skyllias/alomatia/Labels_*.properties";
  private static final String RESOURCE_BUNDLE_NAME                   = "org.skyllias.alomatia.Labels";

  private static final Locale DEFAULT_LANGUAGE = Locale.ENGLISH;

  private final Collection<Locale> availableLocales;

  private Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  /* Initializes the ResourceBundle but does nothing with locales yet. */

  static
  {
    UIManager.getDefaults().addResourceBundle(RESOURCE_BUNDLE_NAME);
  }

//==============================================================================

  public StartupLabelLocalizer()
  {
    availableLocales = findLocalesInClasspath();

    initializeSelectedLocale();
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
    if (nextLocale == null) preferences.remove(PREFKEY_NEXTLANG);
    else                    preferences.put(PREFKEY_NEXTLANG, nextLocale.getLanguage());
  }

//------------------------------------------------------------------------------

  /** Changing the contents of the collection has no effect. */

  @Override
  public Collection<Locale> getAvailableLocales()
  {
    return Collections.unmodifiableCollection(availableLocales);
  }

//------------------------------------------------------------------------------

  /** Meant only for testing purposes.
   *  This must be called before getString. */

  protected void setPreferences(Preferences prefs) {preferences = prefs;}

//------------------------------------------------------------------------------

  /* The default locale is added no matter what else is found in the classpath. */

  private Collection<Locale> findLocalesInClasspath()
  {
    Collection<Locale> result = new LinkedList<>();
    result.add(DEFAULT_LANGUAGE);

    try
    {
      ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

      Resource[] localizedPropertiesFiles = resourcePatternResolver.getResources(LOCALIZED_PROPERTIES_PATHS_ANT_PATTERN);
      for (Resource currentLocalizedPropertiesFile : localizedPropertiesFiles)
      {
        String currentFileName = currentLocalizedPropertiesFile.getFilename();
        Matcher matcher        = PROPERTIES_FILE_NAME_PATTERN.matcher(currentFileName);
        if (matcher.matches())                                                  // although this will always be true, it is required for group() to work
        {
          String languageCode = matcher.group(1);
          result.add(new Locale(languageCode));
        }
      }
    }
    catch (Exception e) {e.printStackTrace();}                                  // TODO log

    return result;
  }

//------------------------------------------------------------------------------

  /* Sets the language to use to the value in the preferences (or the system's
   * default if there are no preferences yet).
   * The resulting language from above is overridden by the VM argument, if present. */

  private void initializeSelectedLocale()
  {
    String defaultLangId = getDefaultLocaleId();
    String languageId    = preferences.get(PREFKEY_NEXTLANG, defaultLangId);
    Locale languageToUse = new Locale(getSelectedLocaleId(languageId));

    UIManager.getDefaults().setDefaultLocale(languageToUse);
    Locale.setDefault(languageToUse);                                           // for some reason, Swing built-in components do not use UIManager defaults but this one
  }

//------------------------------------------------------------------------------

  /* If the system's locale is supported, its id is returned. Else, a fixed
   * default supported id is returned. */

  private String getDefaultLocaleId()
  {
    String systemLangId = Locale.getDefault().getLanguage();
    if (isAvailable(systemLangId)) return systemLangId;
    else                           return DEFAULT_LANGUAGE.getLanguage();
  }

//------------------------------------------------------------------------------

  /* If the system properties contain a supported language id, it is returned.
   * Else, alternativeId is returned. */

  private String getSelectedLocaleId(String alternativeId)
  {
    String vmArgument = System.getProperty(VM_ARG_LANGUAGE);
    if (vmArgument == null || !isAvailable(vmArgument)) return alternativeId;
    else                                                return vmArgument;
  }

//------------------------------------------------------------------------------

  /* Returns true if languageId is among the available languages. */

  private boolean isAvailable(String languageId)
  {
    return availableLocales.stream()
                           .map(Locale::getLanguage)
                           .anyMatch(languageId::equals);
  }

//------------------------------------------------------------------------------

}
