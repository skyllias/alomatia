
package org.skyllias.alomatia.filter.buffered.spectrum;

import java.awt.Color;
import java.awt.image.BufferedImage;

/** Calculator of spectra of buffered images. */

public class Spectrometer
{
  private static final int DEFAULT_AMOUNT_OF_BUCKETS = 100;

  private final int amountOfBuckets;

//==============================================================================

  public Spectrometer() {this(DEFAULT_AMOUNT_OF_BUCKETS);}

//------------------------------------------------------------------------------

  /** Just for testing purposes. */

  protected Spectrometer(int amountOfBuckets) {this.amountOfBuckets = amountOfBuckets;}

//==============================================================================

  /** Returns a new spectrum with the data from all the pixels of the image. */

  public Spectrum calculate(BufferedImage image)
  {
    Spectrum spectrum = new Spectrum(amountOfBuckets);

    for (int x = 0; x < image.getWidth(); x++)
    {
      for (int y = 0; y < image.getHeight(); y++)
      {
        addContribution(new Color(image.getRGB(x, y)), spectrum);
      }
    }

    return spectrum;
  }

//------------------------------------------------------------------------------

  /* Adds to spectrum the contribution by colour, considering its hue and how
   * sharp it shows.
   * If anything, blacks, greys and whites contribute very little. */

  private void addContribution(Color colour, Spectrum spectrum)
  {
    float[] hueSaturationBrightness = Color.RGBtoHSB(colour.getRed(), colour.getGreen(), colour.getBlue(), null);
    float hue                       = hueSaturationBrightness[0];
    float saturation                = hueSaturationBrightness[1];
    float brightness                = hueSaturationBrightness[2];

    float amount = saturation * brightness;
    spectrum.addPixelContribution(hue, amount);
  }

//------------------------------------------------------------------------------

}
