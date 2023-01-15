
package org.skyllias.alomatia.ui.source;

/** Provider of {@link SourceSelection} instances.
 *  All instances should be independent. */

public interface SourceSelectionComposer
{
  SourceSelection buildSelector();
  
  /** Returns an identifier that can be used to compose i18n keys and action commands. */
  
  String getSourceKey();
}
