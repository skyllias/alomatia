
package org.skyllias.alomatia.filter.hsb;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HueReverserConverterTest
{
  private static final float ERROR_TOLERANCE = 0.001f;

  @Test
  public void shouldNotModifySaturation()
  {
    HueReverserConverter converter = new HueReverserConverter(0.4f);
    assertEquals(0.5f, converter.getNewSaturation(0.1f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotModifyBrightness()
  {
    HueReverserConverter converter = new HueReverserConverter(0.4f);
    assertEquals(0.75f, converter.getNewBrightness(0.1f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotModifyCentralHues()
  {
    HueReverserConverter converter = new HueReverserConverter(0.4f);
    assertEquals(0.4f, converter.getNewHue(0.4f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHuesToTheLeft()
  {
    HueReverserConverter converter = new HueReverserConverter(0.4f);
    assertEquals(0.5f, converter.getNewHue(0.3f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHuesToTheLeftWithDifferentCentralHue()
  {
    HueReverserConverter converter = new HueReverserConverter(0.3f);
    assertEquals(0.6f, converter.getNewHue(0, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHuesToTheRight()
  {
    HueReverserConverter converter = new HueReverserConverter(0.4f);
    assertEquals(0.3f, converter.getNewHue(0.5f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

  @Test
  public void shouldModifyHuesToTheRightWithDifferentCentralHue()
  {
    HueReverserConverter converter = new HueReverserConverter(0.5f);
    assertEquals(0.3f, converter.getNewHue(0.7f, 0.5f, 0.75f), ERROR_TOLERANCE);
  }

}
