
package org.skyllias.alomatia.i18n;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class StartupLabelLocalizerTest
{
  @Test
  public void shouldUseDefaultLanguageWhenNoneIsSet()
  {
    StartupLabelLocalizer localizer = new StartupLabelLocalizer();
    localizer.setLocale(null);
    localizer.resetLocaleAsAtStartup();

    assertEquals("Default language should be English",
                 "Control window", localizer.getString("control.window.title"));
  }

  @Test
  public void shouldUseEnglishWhenSet()
  {
    StartupLabelLocalizer localizer = new StartupLabelLocalizer();
    localizer.setLocale(Locale.ENGLISH);
    localizer.resetLocaleAsAtStartup();

    assertEquals("Next execution locale 'en' should produce messages in English",
                 "Control window", localizer.getString("control.window.title"));
  }

  @Test
  public void shouldUseSpanishWhenSet()
  {
    StartupLabelLocalizer localizer = new StartupLabelLocalizer();
    localizer.setLocale(new Locale("es"));
    localizer.resetLocaleAsAtStartup();

    assertEquals("Next execution locale 'es' should produce messages in Spanish",
                 "Ventana de control", localizer.getString("control.window.title"));
  }

}
