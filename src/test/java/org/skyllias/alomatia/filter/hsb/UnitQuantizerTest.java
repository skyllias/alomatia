
package org.skyllias.alomatia.filter.hsb;

import static org.junit.Assert.*;

import org.junit.*;

public class UnitQuantizerTest
{
  @Test
  public void shouldReturnZeroWhenInputIsZeroAndTwoBucketsAndNoCenter()
  {
    UnitQuantizer quantizer = new UnitQuantizer(2, false);
    assertEquals("The quantization of zero should be zero", 0, quantizer.getQuantized(0), 0);
  }

  @Test
  public void shouldReturnZeroWhenInputIsZeroAndManyBucketsAndNoCenter()
  {
    UnitQuantizer quantizer = new UnitQuantizer(20, false);
    assertEquals("The quantization of zero should be zero", 0, quantizer.getQuantized(0), 0);
  }

  @Test
  public void shouldNotReturnZeroWhenInputIsZeroAndTwoBucketsAndCenter()
  {
    UnitQuantizer quantizer = new UnitQuantizer(2, true);
    assertEquals("The quantization of zero should not be zero", 0.25, quantizer.getQuantized(0), 0.001);
  }

  @Test
  public void shouldReturnOneWhenInputIsOneAndTwoBucketsAndNoCenter()
  {
    UnitQuantizer quantizer = new UnitQuantizer(2, false);
    assertEquals("The quantization of one should be one", 1, quantizer.getQuantized(1), 0);
  }

  @Test
  public void shouldReturnOneWhenInputIsOneAndManyBucketsAndNoCenter()
  {
    UnitQuantizer quantizer = new UnitQuantizer(20, false);
    assertEquals("The quantization of one should be one", 1, quantizer.getQuantized(1), 0);
  }

  @Test
  public void shouldNotReturnOneWhenInputIsOneAndTwoBucketsAndCenter()
  {
    UnitQuantizer quantizer = new UnitQuantizer(2, true);
    assertEquals("The quantization of one should not be one", 0.75, quantizer.getQuantized(1), 0.001);
  }

  @Test
  public void shouldReturnHalfWhenInputIsNearHalfAndOddBucketsAndNoCenter()
  {
    UnitQuantizer quantizer = new UnitQuantizer(3, false);
    assertEquals("The quantization of almost 0.5 should be 0.5", 0.5f, quantizer.getQuantized(0.6f), 0.001);
  }

  @Test
  public void shouldReturnHalfWhenInputIsNearHalfAndOddBucketsAndCenter()
  {
    UnitQuantizer quantizer = new UnitQuantizer(3, true);
    assertEquals("The quantization of almost 0.5 should be 0.5", 0.5f, quantizer.getQuantized(0.6f), 0.001);
  }

  @Test
  public void shouldNotReturnHalfWhenInputIsNearHalfAndEvenBucketsAndCenter()
  {
    UnitQuantizer quantizer = new UnitQuantizer(6, true);
    assertNotEquals("The quantization of almost 0.5 should not be 0.5", 0.5f, quantizer.getQuantized(0.6f), 0.001);
  }

  @Test
  public void shouldNotReturnHalfWhenInputIsNearHalfAndEvenBucketsAndNoCenter()
  {
    UnitQuantizer quantizer = new UnitQuantizer(6, false);
    assertNotEquals("The quantization of almost 0.5 should not be 0.5", 0.5f, quantizer.getQuantized(0.6f), 0.001);
  }
}
