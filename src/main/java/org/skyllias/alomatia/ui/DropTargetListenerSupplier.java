
package org.skyllias.alomatia.ui;

import java.awt.dnd.DropTargetListener;

import org.skyllias.alomatia.source.DropSource;
import org.skyllias.alomatia.source.SourceCatalogue;
import org.springframework.stereotype.Component;

/** Supplier of a {@link DropTargetListener} related to a {@link DropSource}. */

@Component
public class DropTargetListenerSupplier
{
  private final DropTargetListener dropTargetListener;

//==============================================================================

  public DropTargetListenerSupplier(SourceCatalogue sourceCatalogue)
  {
    dropTargetListener = sourceCatalogue.get(DropSource.class);
  }

//==============================================================================

  /** Potentially null. */

  public DropTargetListener getDropTargetListener()
  {
    return dropTargetListener;
  }

//------------------------------------------------------------------------------

}
