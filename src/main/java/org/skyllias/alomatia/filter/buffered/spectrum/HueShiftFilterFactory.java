
package org.skyllias.alomatia.filter.buffered.spectrum;

import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.hsb.HueShiftConverter;
import org.skyllias.alomatia.filter.hsb.factory.HsbFilterFactory;

/** Factory of filters that apply a shift of hue leaving everything else the same.
 *  This delegates directly to {@link HsbFilterFactory}. */

public class HueShiftFilterFactory
{
//==============================================================================

  /** The criteria from {@link HueShiftConverter} apply. */

  public ImageFilter getHueShiftFilter(float shift)
  {
    return HsbFilterFactory.forHueShift(shift);
  }

//------------------------------------------------------------------------------

}
