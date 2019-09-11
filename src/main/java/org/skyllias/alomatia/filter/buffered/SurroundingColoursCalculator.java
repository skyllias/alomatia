
package org.skyllias.alomatia.filter.buffered;

import java.awt.Color;
import java.util.Collection;

/** Selector of a colour for some pixel considering the colours of the
 *  surrounding pixels (including the pixel at the location being replaced).
 *  This interface does not care about the position of each colour or how many
 *  there are. */

public interface SurroundingColoursCalculator
{
  /** Returns the colour to apply when in a region there are the passed colours. */

  Color getColour(Collection<Color> surroundingColours);

}
