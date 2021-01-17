
package org.skyllias.alomatia.filter.buffered.patch;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Collection;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.map.HueMap;

/** {@link BufferedImageOperation} that tints each patch in the source image
 *  with a hue taken from a {@link HueMap}. */

public class HueMappedPatchBufferedImageOperation implements BufferedImageOperation
{
  private final SimilarPatchesFinder patchesFinder;
  private final HueMap hueMap;

//==============================================================================

  public HueMappedPatchBufferedImageOperation(SimilarPatchesFinder similarPatchesFinder,
                                              HueMap hueMap)
  {
    this.patchesFinder = similarPatchesFinder;
    this.hueMap        = hueMap;
  }

//==============================================================================

  /* Painting pixels on the image happens to be way faster than doing it through
   * its Graphics2D. */

  @Override
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    Collection<Patch> patches = patchesFinder.findPatches(inputImage);
    for (Patch patch : patches)
    {
        Point patchCenter = patch.getCentralPoint();
        float newHue      = hueMap.getHue(patchCenter.x, patchCenter.y,
                                          inputImage.getWidth(), inputImage.getHeight());
        Color patchOriginalColour = patch.getPixels().iterator().next().getColour();
        float[] patchOriginalHsb  = Color.RGBtoHSB(patchOriginalColour.getRed(),
                                                   patchOriginalColour.getGreen(),
                                                   patchOriginalColour.getBlue(), null);
        int patchNewColour = Color.HSBtoRGB(newHue, patchOriginalHsb[1], patchOriginalHsb[2]);

        for (final Pixel pixel : patch.getPixels())
        {
          outputImage.setRGB(pixel.getCoordinates().x, pixel.getCoordinates().y,
                      patchNewColour);
        }
    }
  }

//------------------------------------------------------------------------------

}
