
package org.skyllias.alomatia.filter.buffered.spectrum;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.BasicBufferedImageOp;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;

/** {@link BufferedImageOp} that applies a hue shift so that some target hue
 *  becomes the most relevant in the image. Therefore the shift depends on the
 *  input image as much as on the target hue. */

public class DominantHueShiftOp extends BasicBufferedImageOp
{
  private final MostRelevantHueCalculator mostRelevantHueCalculator;
  private final FilteredBufferedImageGenerator filteredBufferedImageGenerator;
  private final HueShiftFilterFactory hueShiftFilterFactory;
  private final float targetHue;

//==============================================================================

  public DominantHueShiftOp(MostRelevantHueCalculator mostRelevantHueCalculator,
                            FilteredBufferedImageGenerator filteredBufferedImageGenerator,
                            HueShiftFilterFactory hueShiftFilterFactory,
                            float targetHue)
  {
    this.mostRelevantHueCalculator      = mostRelevantHueCalculator;
    this.filteredBufferedImageGenerator = filteredBufferedImageGenerator;
    this.hueShiftFilterFactory          = hueShiftFilterFactory;
    this.targetHue                      = targetHue;
  }

//==============================================================================

  @Override
  protected void doFilter(BufferedImage src, BufferedImage dest)
  {
    float mostRelevantHueInImage = mostRelevantHueCalculator.getMostRelevantHue(src);
    float requiredShift          = targetHue - mostRelevantHueInImage;

    ImageFilter hueShiftFilter = hueShiftFilterFactory.getHueShiftFilter(requiredShift);
    BufferedImage result       = filteredBufferedImageGenerator.generate(src, hueShiftFilter);
    dest.getGraphics().drawImage(result, 0, 0, null);
  }

//------------------------------------------------------------------------------

}
