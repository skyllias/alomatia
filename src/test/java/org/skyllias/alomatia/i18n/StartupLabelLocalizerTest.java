
package org.skyllias.alomatia.i18n;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.prefs.*;

import org.junit.*;

public class StartupLabelLocalizerTest
{
  @Test
  public void shouldUseDefaultLanguageWhenSystemsIsNotSupported()
  {
    Locale.setDefault(new Locale("whatever"));
    Preferences preferences = mock(Preferences.class);
    when(preferences.get(eq(StartupLabelLocalizer.PREFKEY_NEXTLANG), any(String.class))).thenReturn("en");

    StartupLabelLocalizer localizer = new StartupLabelLocalizer();
    localizer.setPreferences(preferences);
    localizer.getString("control.window.title");

    verify(preferences).get(StartupLabelLocalizer.PREFKEY_NEXTLANG, "en");
  }

  @Test
  public void shouldUseSystemLanguageWhenSystemsIsSupported()
  {
    Locale.setDefault(new Locale("es"));
    Preferences preferences = mock(Preferences.class);
    when(preferences.get(eq(StartupLabelLocalizer.PREFKEY_NEXTLANG), any(String.class))).thenReturn("es");

    StartupLabelLocalizer localizer = new StartupLabelLocalizer();
    localizer.setPreferences(preferences);
    localizer.getString("control.window.title");

    verify(preferences).get(StartupLabelLocalizer.PREFKEY_NEXTLANG, "es");
  }

  @Test
  public void shouldUseEnglishWhenSet()
  {
    Preferences preferences = mock(Preferences.class);
    when(preferences.get(eq(StartupLabelLocalizer.PREFKEY_NEXTLANG), any(String.class))).thenReturn("en");

    StartupLabelLocalizer localizer = new StartupLabelLocalizer();
    localizer.setPreferences(preferences);

    assertEquals("Next execution locale 'en' should produce messages in English",
                 "Control window - Alomatia", localizer.getString("control.window.title"));
  }

  @Test
  public void shouldUseSpanishWhenSet()
  {
    Preferences preferences = mock(Preferences.class);
    when(preferences.get(eq(StartupLabelLocalizer.PREFKEY_NEXTLANG), any(String.class))).thenReturn("es");

    StartupLabelLocalizer localizer = new StartupLabelLocalizer();
    localizer.setPreferences(preferences);

    assertEquals("Next execution locale 'es' should produce messages in Spanish",
                 "Ventana de control - Alomatia", localizer.getString("control.window.title"));
  }

  @Test
  public void shouldSaveLocaleWhenSet()
  {
    Preferences preferences = mock(Preferences.class);

    StartupLabelLocalizer localizer = new StartupLabelLocalizer();
    localizer.setPreferences(preferences);
    localizer.setLocale(new Locale("whatever"));

    verify(preferences).put(StartupLabelLocalizer.PREFKEY_NEXTLANG, "whatever");
  }
}
