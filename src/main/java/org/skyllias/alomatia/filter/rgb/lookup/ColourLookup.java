
package org.skyllias.alomatia.filter.rgb.lookup;

/** Mapping of each channel in an original colour into another value.
 *  The relationship between the original value in each channel and the resulting
 *  value is achieved by means of non-null arrays, where the index (between 0
 *  and 255) represents the original channel value, and the array contents
 *  (between 0 and 255 too) represents the result after applying the conversion.
 *  Each channel is independent from the others.
 *  If any of the arrays had less than 255 items, then the last one should be
 *  applied to the missing values; if it had more, then the extra ones should
 *  be ignored. */

public interface ColourLookup
{
  /** Returns the values mapped for the red channel. */

  int[] getRedArray();

  /** Returns the values mapped for the green channel. */

  int[] getGreenArray();

  /** Returns the values mapped for the blue channel. */

  int[] getBlueArray();
}
