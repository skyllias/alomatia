
package org.skyllias.alomatia.filter.buffered.vignette;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EdgesProfileTest
{
  EdgesProfile profile = new EdgesProfile();

  @Test
  public void shouldReturnZeroWhenPixelIsCentered()
  {
    assertEquals("The profile should be zero when the pixel is centered", 0, profile.getVignetteEffect(50, 50, 100, 100), 0.000001);
  }

  @Test
  public void shouldReturnASmallValueWhenPixelIsCenteredInQuarter()
  {
    assertTrue("The profile should be small when the pixel is centered in a quarter", 0.1 > profile.getVignetteEffect(25, 25, 100, 100));
  }

  @Test
  public void shouldReturnOneWhenPixelIsAt12()
  {
    assertEquals("The profile should be one when the pixel is at 12", 1, profile.getVignetteEffect(50, 0, 100, 100), 0.000001);
  }

  @Test
  public void shouldReturnOneWhenPixelIsAt3()
  {
    assertEquals("The profile should be one when the pixel is at 3", 1, profile.getVignetteEffect(100, 50, 100, 100), 0.000001);
  }

  @Test
  public void shouldReturnOneWhenPixelIsAt6()
  {
    assertEquals("The profile should be one when the pixel is at 6", 1, profile.getVignetteEffect(50, 100, 100, 100), 0.000001);
  }

  @Test
  public void shouldReturnOneWhenPixelIsAt9()
  {
    assertEquals("The profile should be one when the pixel is at 9", 1, profile.getVignetteEffect(0, 50, 100, 100), 0.000001);
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
