
package org.skyllias.alomatia.filter.buffered.patch;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import org.skyllias.alomatia.filter.buffered.*;
import org.skyllias.alomatia.filter.buffered.map.*;

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
