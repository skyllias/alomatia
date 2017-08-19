
package org.skyllias.alomatia.i18n;

import java.util.*;

/** Provider of labels to be presented to the user in some language.
 *  <p>
 *  This may be seen as a wrapper for a fixed ResourceBundle with embedded
 *  Locale selection.
 *  <p>
 *  This takes care not only of the labels and number or date formats (if ever
 *  used) of the custom components but also of those of Swing, like the FileChooser.
 *  Mind however that the user interface in Java is only available in a very
 *  limited set of languages: http://www.oracle.com/technetwork/java/javase/javase7locales-334809.html
 *  Implementations may or may not be able to localize those too.
 *  <p>
 *  To centralize control, no other i18n-related operation should be carried out
 *  elsewhere. */

public interface LabelLocalizer
{
  /** Returns the localized label with the passed key, or the key itself if not found. */

  String getString(String key);

  /** Returns the locale to which labels are being translated currently.
   *  <p>
   *  This may or may not be affected by a call to {@link #setLocale(Locale)}
   *  (see explanation there). */

  Locale getCurrentLocale();

  /** Stores the passed locale so that labels obtained from getString are localized in it.
   *  <p>
   *  A null value means that the default locale should be used.
   *  <p>
   *  When that really happens is implementation specific: It may be immediate
   *  so that subsequent calls to getString already use nextLocale, or it may be
   *  deferred until the next execution. This is so because an immediate change
   *  would require existing labels to be regenerated to prevent mixing languages. */

  void setLocale(Locale nextLocale);
}
