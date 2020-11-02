
package org.skyllias.alomatia.filter;

import java.awt.Color;

/** Transformer of any {@link Color} into possibly a different one. */

public interface ColourConverter
{
  Color convertColour(Color original);
}
