
package org.skyllias.alomatia.filter.compose;

import java.awt.image.*;

import org.skyllias.alomatia.filter.rgb.*;

/** {@link ComposedFilter} that sets forces a value of 1.0 in the alpha channel
 *  of all pixels after another filter is applied.
 *  <p>
 *  Ther same could be achieved in different ways, especially favouring
 *  composition over inheritance. */

public class OpaqueFilter extends ComposedFilter
{
  private static ImageFilter opaquingFilter = new VoidFilter();

//==============================================================================

  public OpaqueFilter(ImageFilter filter) {super(filter, opaquingFilter);}

//------------------------------------------------------------------------------

}
