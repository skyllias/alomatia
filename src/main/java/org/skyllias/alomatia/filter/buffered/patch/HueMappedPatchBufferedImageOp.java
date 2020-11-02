
package org.skyllias.alomatia.filter.buffered.patch;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;

import org.skyllias.alomatia.filter.buffered.BasicBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.map.HueMap;

/** BasicBufferedImageOp that tints each patch in the source image with a hue
 *  taken from a {@link HueMap}. */

public class HueMappedPatchBufferedImageOp extends BasicBufferedImageOp
{
  private final SimilarPatchesFinder patchesFinder;
  private final HueMap hueMap;

//==============================================================================

  public HueMappedPatchBufferedImageOp(SimilarPatchesFinder similarPatchesFinder,
                                       HueMap hueMap)
  {
    this.patchesFinder = similarPatchesFinder;
    this.hueMap        = hueMap;
  }

//==============================================================================

  /* Painting pixels on the image happens to be way faster than doing it through
   * its Graphics2D. */

  @Override
  public void doFilter(BufferedImage src, BufferedImage dest)
  {
    Collection<Patch> patches = patchesFinder.findPatches(src);
    for (Patch patch : patches)
    {
        Point patchCenter = patch.getCentralPoint();
        float newHue      = hueMap.getHue(patchCenter.x, patchCenter.y,
                                          src.getWidth(), src.getHeight());
        Color patchOriginalColour = patch.getPixels().iterator().next().getColour();
        float[] patchOriginalHsb  = Color.RGBtoHSB(patchOriginalColour.getRed(),
                                                   patchOriginalColour.getGreen(),
                                                   patchOriginalColour.getBlue(), null);
        int patchNewColour = Color.HSBtoRGB(newHue, patchOriginalHsb[1], patchOriginalHsb[2]);

        for (final Pixel pixel : patch.getPixels())
        {
          dest.setRGB(pixel.getCoordinates().x, pixel.getCoordinates().y,
                      patchNewColour);
        }
    }
  }

//------------------------------------------------------------------------------

}
