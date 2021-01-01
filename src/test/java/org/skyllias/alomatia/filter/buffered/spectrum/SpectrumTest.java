
package org.skyllias.alomatia.filter.buffered.spectrum;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SpectrumTest
{
  private static final float ERROR_TOLERANCE = 0.0001f;

  @Test
  public void shouldAddPixelContributionToOneBucket()
  {
    Spectrum spectrum = new Spectrum(10);
    spectrum.addPixelContribution(0.15f, 1.5f);

    assertArrayEquals(new float[] {0, 1.5f, 0, 0, 0, 0, 0, 0, 0, 0},
                      spectrum.getDistribution(), ERROR_TOLERANCE);
  }

  @Test
  public void shouldAddPixelContributionToAnotherBucket()
  {
    Spectrum spectrum = new Spectrum(new float[] {0.5f, 1.5f, 0, 2, 4.5f, 1, 3.5f, 2.5f, 5, 5.5f});
    spectrum.addPixelContribution(0.45f, 1.5f);

    assertArrayEquals(new float[] {0.5f, 1.5f, 0, 2, 6f, 1, 3.5f, 2.5f, 5, 5.5f},
                      spectrum.getDistribution(), ERROR_TOLERANCE);
  }

  @Test
  public void shouldGetMostRelevantHueWithSingleValuedPattern()
  {
    Spectrum spectrum     = new Spectrum(new float[] {0, 2, 0, 0, 1.5f, 1.5f, 1.5f, 0.5f, 0, 1});
    float mostRelevantHue = spectrum.getMostRelevantHue(buildSingleValuedPattern());

    assertEquals(0.15f, mostRelevantHue, ERROR_TOLERANCE);
  }

  @Test
  public void shouldGetMostRelevantHueWithDifferentAmountOfBuckets()
  {
    Spectrum spectrum     = new Spectrum(new float[] {1, 2, 1, 1, 1.5f});
    float mostRelevantHue = spectrum.getMostRelevantHue(buildSingleValuedPattern());

    assertEquals(0.3f, mostRelevantHue, ERROR_TOLERANCE);
  }

  @Test
  public void shouldGetMostRelevantHueWithMultiValuedPattern()
  {
    Spectrum spectrum     = new Spectrum(new float[] {0, 2, 0, 0, 1.5f, 1.5f, 1.5f, 0.5f, 0, 1});
    float mostRelevantHue = spectrum.getMostRelevantHue(buildMultiValuedPattern());

    assertEquals(0.55f, mostRelevantHue, ERROR_TOLERANCE);
  }

  @Test
  public void shouldGetMostRelevantHueWithCyclicHueToTheLeft()
  {
    Spectrum spectrum     = new Spectrum(new float[] {1.5f, 1.5f, 0.5f, 0, 1, 0, 2, 0, 0, 1.5f});
    float mostRelevantHue = spectrum.getMostRelevantHue(buildMultiValuedPattern());

    assertEquals(0.05f, mostRelevantHue, ERROR_TOLERANCE);
  }

  @Test
  public void shouldGetMostRelevantHueWithCyclicHueToTheRight()
  {
    Spectrum spectrum     = new Spectrum(new float[] {1.5f, 0.5f, 0, 1f, 0, 2, 0, 0, 1.5f, 1.5f});
    float mostRelevantHue = spectrum.getMostRelevantHue(buildMultiValuedPattern());

    assertEquals(0.95f, mostRelevantHue, ERROR_TOLERANCE);
  }


  private MatchingPattern buildSingleValuedPattern()
  {
    return new MatchingPattern()
    {
      @Override
      public float[] getPattern() {return new float[0];}
    };
  }

  private MatchingPattern buildMultiValuedPattern()
  {
    return new MatchingPattern()
    {
      @Override
      public float[] getPattern() {return new float[] {0.5f};}
    };
  }
}
