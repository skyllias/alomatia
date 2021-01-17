
package org.skyllias.alomatia.filter.buffered.spectrum;

import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;

import org.skyllias.alomatia.filter.buffered.BufferedImageOperation;
import org.skyllias.alomatia.filter.buffered.FilteredBufferedImageGenerator;

/** {@link BufferedImageOperation} that applies a hue shift so that some target
 *  hue becomes the most relevant in the image. Therefore the shift depends on
 *  the input image as much as on the target hue. */

public class DominantHueShiftOperation implements BufferedImageOperation
{
  private final MostRelevantHueCalculator mostRelevantHueCalculator;
  private final FilteredBufferedImageGenerator filteredBufferedImageGenerator;
  private final HueShiftFilterFactory hueShiftFilterFactory;
  private final float targetHue;

//==============================================================================

  public DominantHueShiftOperation(MostRelevantHueCalculator mostRelevantHueCalculator,
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
  public void filter(BufferedImage inputImage, BufferedImage outputImage)
  {
    float mostRelevantHueInImage = mostRelevantHueCalculator.getMostRelevantHue(inputImage);
    float requiredShift          = targetHue - mostRelevantHueInImage;

    ImageFilter hueShiftFilter = hueShiftFilterFactory.getHueShiftFilter(requiredShift);
    BufferedImage result       = filteredBufferedImageGenerator.generate(inputImage, hueShiftFilter);
    outputImage.getGraphics().drawImage(result, 0, 0, null);
  }

//------------------------------------------------------------------------------

}
