
package org.skyllias.alomatia.filter.buffered.surround;

import java.awt.Color;

/** Provider of a black or white colour depending on some amount of light. */

public interface BlackOrWhiteSelector
{
  Color chooseBlackOrWhite(float lightness);
}
