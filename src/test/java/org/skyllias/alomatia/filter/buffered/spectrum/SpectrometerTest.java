
package org.skyllias.alomatia.filter.buffered.spectrum;

import static org.junit.Assert.assertArrayEquals;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.Test;

public class SpectrometerTest
{
  private static final float ERROR_TOLERANCE = 0.005f;

  @Test
  public void shouldCalculateSpectrum()
  {
    Spectrometer spectrometer = new Spectrometer(4);
    Spectrum spectrum         = spectrometer.calculate(buildInputImage());

    assertArrayEquals(new float[] {0.06f, 0.30f, 0.72f, 0.09f},
                      spectrum.getDistribution(), ERROR_TOLERANCE);
  }


  private BufferedImage buildInputImage()
  {
    BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
    image.setRGB(0, 0, Color.HSBtoRGB(0.1f, 0.2f, 0.3f));
    image.setRGB(0, 1, Color.HSBtoRGB(0.4f, 0.5f, 0.6f));
    image.setRGB(1, 0, Color.HSBtoRGB(0.7f, 0.8f, 0.9f));
    image.setRGB(1, 1, Color.HSBtoRGB(0.9f, 0.1f, 0.9f));

    return image;
  }

}
