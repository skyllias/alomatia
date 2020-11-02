
package org.skyllias.alomatia.filter;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

/** {@link RGBImageFilter} that filters colours independently from the location
 *  of pixels.
 *  The conversion is delegated to a {@link ColourConverter}. */

public class ColourFilter extends RGBImageFilter
{
  private final ColourConverter converter;

//==============================================================================

  public ColourFilter(ColourConverter converter)
  {
    this.converter = converter;

    super.canFilterIndexColorModel = true;
  }

//==============================================================================

  /** Returns a new int representation of the passed colour without caring about
   *  the pixel coordinates. */

  @Override
  public int filterRGB(int x, int y, int rgb)
  {
    Color originalColor = new Color(rgb);
    Color modifiedColor = converter.convertColour(originalColor);
    return modifiedColor.getRGB();
  }

//------------------------------------------------------------------------------

}
