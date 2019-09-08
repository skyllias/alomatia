
package org.skyllias.alomatia.filter.hsb.pole;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HueDistanceTest
{
  @Test
  public void shouldReturnZeroWhenSameHueIsMeasured()
  {
    HueDistance distance = new HueDistance();
    assertEquals("The distance of a hue to itself should be zero", 0, distance.calculate(0.75f, 0.75f), 0);
  }

  @Test
  public void shouldReturnZeroWhenEquivalentHueIsMeasured()
  {
    HueDistance distance = new HueDistance();
    assertEquals("The distance of a hue to itself plus an integer should be zero", 0, distance.calculate(0.25f, 2.25f), 0.0001);
  }

  @Test
  public void shouldReturnDifferenceWhenHueToTheRightIsMeasured()
  {
    HueDistance distance = new HueDistance();
    assertEquals("The distance of a hue to another should be the difference", 0.4f, distance.calculate(0.25f, 0.65f), 0.0001);
  }

  @Test
  public void shouldReturnAbsoluteDifferenceWhenHueToTheLeftIsMeasured()
  {
    HueDistance distance = new HueDistance();
    assertEquals("The distance of a hue to another should be the difference", 0.4f, distance.calculate(0.65f, 0.25f), 0.0001);
  }

  @Test
  public void shouldGoRoundWhenDistantHueIsMeasured()
  {
    HueDistance distance = new HueDistance();
    assertEquals("The distance of a hue to another should not depend on their offset", 0.3f, distance.calculate(0.85f, 0.15f), 0.0001);
  }

  @Test
  public void shouldReturnZeroWhenSameHueIsCompared()
  {
    HueDistance distance = new HueDistance();
    assertEquals("The difference of a hue to itself should be zero", 0, distance.difference(0.75f, 0.75f), 0);
  }

  @Test
  public void shouldReturnZeroWhenEquivalentHueIsCompared()
  {
    HueDistance distance = new HueDistance();
    assertEquals("The difference of a hue to itself plus an integer should be zero", 0, distance.difference(-3.75f, 2.25f), 0);
  }

  @Test
  public void shouldReturnDifferenceWhenHueToTheRightIsCompared()
  {
    HueDistance distance = new HueDistance();
    assertEquals("The difference of a hue to another should be the substraction", 0.4f, distance.difference(0.25f, 0.65f), 0.0001);
  }

  @Test
  public void shouldReturnDifferenceWhenHueToTheLeftIsCompared()
  {
    HueDistance distance = new HueDistance();
    assertEquals("The difference of a hue to another should be the substraction", -0.4f, distance.difference(0.65f, 0.25f), 0.0001);
  }

  @Test
  public void shouldGoRoundWhenDistantHueIsCompared()
  {
    HueDistance distance = new HueDistance();
    assertEquals("The difference of a hue to another should not depend on their offset", -0.3f, distance.difference(0.15f, 0.85f), 0.0001);
  }
}
