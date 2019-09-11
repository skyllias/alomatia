
package org.skyllias.alomatia.source;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.skyllias.alomatia.ImageSource;

/** Collector of {@link ImageSource} instances retrievable by type.
 *  <p>
 *  The instances are created externally and all this class does is to carry
 *  them in a sorted way. */

public class SourceCatalogue
{
  private Map<Class<? extends ImageSource>, ImageSource> sourcesByClass = new HashMap<>();

//==============================================================================

  /** Registers sourceInstance as the instance to return when get(sourceClass)
   *  is invoked. Any previous instance is silently overridden.
   *  <p>
   *  The type is not automatically inferred to allow polymorphism. */

  public <S extends ImageSource> void add(Class<S> sourceClass, S sourceInstance)
  {
    sourcesByClass.put(sourceClass, sourceInstance);
  }

//------------------------------------------------------------------------------

  /** Returns the instance previously added for sourceClass, or null if none.
   *  <p>
   *  If a subclass or superclass of the originally registered class is passed,
   *  the instance will not be found. */

  @SuppressWarnings("unchecked")
  public <S extends ImageSource> S get(Class<S> sourceClass)
  {
    return (S) sourcesByClass.get(sourceClass);
  }

//------------------------------------------------------------------------------

  /** Returns all the currently registered instances.
   *  <p>
   *  The collection is immutable. */

  public Collection<ImageSource> getAllInstances()
  {
    return Collections.unmodifiableSet(new HashSet<>(sourcesByClass.values()));
  }

//------------------------------------------------------------------------------

}
