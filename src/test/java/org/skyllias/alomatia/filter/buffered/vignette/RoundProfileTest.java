
package org.skyllias.alomatia.filter.buffered.vignette;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RoundProfileTest
{
  RoundProfile profile = new RoundProfile();

  @Test
  public void shouldReturnZeroWhenPixelIsCentered()
  {
    assertEquals("The profile should be zero when the pixel is centered", 0, profile.getVignetteEffect(50, 50, 100, 100), 0.000001);
  }

  @Test
  public void shouldReturnASmallValueWhenPixelIsCenteredInQuarter()
  {
    assertEquals("The profile should be small when the pixel is centered in a quarter", 0.25, profile.getVignetteEffect(25, 25, 100, 100), 0.0001);
  }

  @Test
  public void shouldReturnHalfWhenPixelIsAt12()
  {
    assertEquals("The profile should be 0.5 when the pixel is at 12", 0.5, profile.getVignetteEffect(50, 0, 100, 100), 0.0001);
  }

  @Test
  public void shouldReturnHalfWhenPixelIsAt3()
  {
    assertEquals("The profile should be 0.5 when the pixel is at 3", 0.5, profile.getVignetteEffect(100, 50, 100, 100), 0.0001);
  }

  @Test
  public void shouldReturnHalfWhenPixelIsAt6()
  {
    assertEquals("The profile should be 0.5 when the pixel is at 6", 0.5, profile.getVignetteEffect(50, 100, 100, 100), 0.0001);
  }

  @Test
  public void shouldReturnHalfWhenPixelIsAt9()
  {
    assertEquals("The profile should be 0.5 when the pixel is at 9", 0.5, profile.getVignetteEffect(0, 50, 100, 100), 0.0001);
  }

  @Test
  public void shouldReturnOneWhenPixelIsAtTopLeft()
  {
    assertEquals("The profile should be one when the pixel is on a corner", 1, profile.getVignetteEffect(0, 0, 100, 100), 0.000001);
  }

  @Test
  public void shouldReturnOneWhenPixelIsAtTopRight()
  {
    assertEquals("The profile should be one when the pixel is on a corner", 1, profile.getVignetteEffect(100, 0, 100, 100), 0.000001);
  }

  @Test
  public void shouldReturnOneWhenPixelIsAtBottomRight()
  {
    assertEquals("The profile should be one when the pixel is on a corner", 1, profile.getVignetteEffect(100, 100, 100, 100), 0.000001);
  }

  @Test
  public void shouldReturnOneWhenPixelIsAtBottomLeft()
  {
    assertEquals("The profile should be one when the pixel is on a corner", 1, profile.getVignetteEffect(0, 100, 100, 100), 0.000001);
  }
}
