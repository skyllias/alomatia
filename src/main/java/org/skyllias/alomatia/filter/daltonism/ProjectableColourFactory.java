
package org.skyllias.alomatia.filter.daltonism;

import java.awt.Color;

/** Provider of instances of {@link ProjectableColour} for the sake of testability. */

public class ProjectableColourFactory
{
//==============================================================================

  public ProjectableColour createProjectableColour(Color originalColor)
  {
    return new ProjectableColour(originalColor);
  }

//------------------------------------------------------------------------------

}
