
package org.skyllias.alomatia.i18n;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.Test;
import org.skyllias.alomatia.preferences.LocalePreferences;

/** Mockito annotations cannot be naively used in this class because oddly the
 *  constructor of StartupLabelLocalizer invokes localePreferences.getNextLanguageCode
 *  (a small inner refurbishment could be tried). */

public class StartupLabelLocalizerTest
{
  @Test
  public void shouldUseDefaultLanguageWhenSystemsIsNotSupported()
  {
    Locale.setDefault(new Locale("whatever"));
    LocalePreferences localePreferences = mock(LocalePreferences.class);
    when(localePreferences.getNextLanguageCode(any())).thenReturn("ca");

    new StartupLabelLocalizer(localePreferences);

    verify(localePreferences).getNextLanguageCode("en");
  }

  @Test
  public void shouldUseSystemLanguageWhenSystemsIsSupported()
  {
    Locale.setDefault(new Locale("es"));
    LocalePreferences localePreferences = mock(LocalePreferences.class);
    when(localePreferences.getNextLanguageCode(any())).thenReturn("ca");

    new StartupLabelLocalizer(localePreferences);

    verify(localePreferences).getNextLanguageCode("es");
  }

  @Test
  public void shouldUseEnglishWhenSet()
  {
    LocalePreferences localePreferences = mock(LocalePreferences.class);
    when(localePreferences.getNextLanguageCode(any())).thenReturn("en");

    StartupLabelLocalizer localizer = new StartupLabelLocalizer(localePreferences);
    String result = localizer.getString("control.window.title");

    assertEquals("Next execution locale 'en' should produce messages in English",
                 "Control window - Alomatia", result);
  }

  @Test
  public void shouldUseSpanishWhenSet()
  {
    LocalePreferences localePreferences = mock(LocalePreferences.class);
    when(localePreferences.getNextLanguageCode(any())).thenReturn("es");

    StartupLabelLocalizer localizer = new StartupLabelLocalizer(localePreferences);
    String result = localizer.getString("control.window.title");

    assertEquals("Next execution locale 'es' should produce messages in Spanish",
                 "Ventana de control - Alomatia", result);
  }

  @Test
  public void shouldSaveLocaleWhenSet()
  {
    LocalePreferences localePreferences = mock(LocalePreferences.class);
    when(localePreferences.getNextLanguageCode(any())).thenReturn("fr");

    StartupLabelLocalizer localizer = new StartupLabelLocalizer(localePreferences);
    localizer.setLocale(new Locale("whatever"));

    verify(localePreferences).setNextLanguageCode("whatever");
  }
}
