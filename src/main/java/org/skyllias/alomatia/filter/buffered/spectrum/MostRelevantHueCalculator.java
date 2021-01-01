
package org.skyllias.alomatia.filter.buffered.spectrum;

import java.awt.image.BufferedImage;

/** Calculator of the most relevant hue in an image.
 *  The heavy work is performed by {@link Spectrometer} and {@link Spectrum},
 *  and this is in charge of orchestrating them. */

public class MostRelevantHueCalculator
{
  private final Spectrometer spectrometer;
  private final MatchingPattern matchingPattern;

//==============================================================================

  public MostRelevantHueCalculator()
  {
    this(new Spectrometer(), buildDefaultMatchingPattern());
  }

//------------------------------------------------------------------------------

  /** Just for testing purposes. */

  protected MostRelevantHueCalculator(Spectrometer spectrometer,
                                      MatchingPattern matchingPattern)
  {
    this.spectrometer    = spectrometer;
    this.matchingPattern = matchingPattern;
  }

//==============================================================================

  /** Returns the most relevant hue in the image according to the defined
   *  matching pattern. */

  public float getMostRelevantHue(BufferedImage image)
  {
    Spectrum spectrum = spectrometer.calculate(image);
    return spectrum.getMostRelevantHue(matchingPattern);
  }

//------------------------------------------------------------------------------

  private static MatchingPattern buildDefaultMatchingPattern()
  {
    return new MatchingPattern()
    {
      @Override
      public float[] getPattern() {return new float[] {0.9f, 0.8f, 0.7f, 0.5f, 0.2f};}
    };
  }

//------------------------------------------------------------------------------

}
