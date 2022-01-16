
package org.skyllias.alomatia.filter.hsb;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FixedHueConverterTest
{
  private static final float ERROR_TOLERANCE = 0.001f;

  @Test
  public void shouldNotModifySaturation()
  {
    FixedHueConverter converter = new FixedHueConverter(0.4f);
    assertEquals(0.5f, converter.getNewSaturation(0.1f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotModifyBrightness()
  {
    FixedHueConverter converter = new FixedHueConverter(0.4f);
    assertEquals(0.75f, converter.getNewBrightness(0.1f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotModifySameHue()
  {
    FixedHueConverter converter = new FixedHueConverter(0.4f);
    assertEquals(0.4f, converter.getNewHue(0.4f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyOtherHuesToTheLeft()
  {
    FixedHueConverter converter = new FixedHueConverter(0.4f);
    assertEquals(0.4f, converter.getNewHue(0.3f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyOtherHuesToTheRight()
  {
    FixedHueConverter converter = new FixedHueConverter(0.6f);
    assertEquals(0.6f, converter.getNewHue(0.8f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

}
