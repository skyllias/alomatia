
package org.skyllias.alomatia.filter;

import java.awt.Color;
import java.awt.image.RGBImageFilter;

/** {@link RGBImageFilter} that filters colours independently from the location
 *  of pixels.
 *  The conversion is delegated to a {@link ColorConverter}. */

public class ColorFilter extends RGBImageFilter
{
  private final ColorConverter converter;

//==============================================================================

  public ColorFilter(ColorConverter converter)
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
    Color modifiedColor = converter.convertColor(originalColor);
    return modifiedColor.getRGB();
  }

//------------------------------------------------------------------------------

}
