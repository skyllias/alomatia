
package org.skyllias.alomatia.filter.buffered.spectrum;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MostRelevantHueCalculatorTest
{
  private static final float ERROR_TOLERANCE = 0.0001f;

  @Mock
  private Spectrometer spectrometer;
  @Mock
  private MatchingPattern matchingPattern;

  @InjectMocks
  private MostRelevantHueCalculator calculator;


  @Test
  public void shouldCallSpectrometer()
  {
    Spectrum spectrum = mock(Spectrum.class);
    when(spectrum.getMostRelevantHue(matchingPattern)).thenReturn(0.341f);

    BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
    when(spectrometer.calculate(image)).thenReturn(spectrum);

    float result = calculator.getMostRelevantHue(image);

    assertEquals(0.341f, result, ERROR_TOLERANCE);
  }

}
