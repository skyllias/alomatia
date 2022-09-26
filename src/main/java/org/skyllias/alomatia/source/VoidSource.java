
package org.skyllias.alomatia.source;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.springframework.stereotype.Component;

/** Source that never produces any image. */

@Component
public class VoidSource implements ImageSource
{
//==============================================================================

  @Override
  public void setDisplay(ImageDisplay display) {}

  @Override
  public void setActive(boolean active) {}

//------------------------------------------------------------------------------
}
