
package org.skyllias.alomatia.filter.hsb;

import static org.junit.Assert.*;

import org.junit.*;

public class SawAttractionTest
{
  @Test
  public void shouldReturnZeroWhenDistanceIsZero()
  {
    SawAttraction attraction = new SawAttraction(0.5f, 0.2f);
    assertEquals("The attraction on a pole should be zero", 0, attraction.attract(0), 0);
  }

  @Test
  public void shouldReturnZeroWhenDistanceIsFurtherThanPositiveLimit()
  {
    SawAttraction attraction = new SawAttraction(0.5f, 0.2f);
    assertEquals("The attraction far from a pole should be zero", 0, attraction.attract(0.405f), 0);
  }

  @Test
  public void shouldReturnZeroWhenDistanceIsFurtherThanNegativeLimit()
  {
    SawAttraction attraction = new SawAttraction(0.5f, 0.2f);
    assertEquals("The attraction far from a pole should be zero", 0, attraction.attract(-0.405f), 0);
  }

  @Test
  public void shouldReturnSmallPositiveAttractionWhenDistanceIsSmallAndPositive()
  {
    SawAttraction attraction = new SawAttraction(0.5f, 0.2f);
    assertEquals("The attraction near a pole should be small", 0.05f, attraction.attract(0.1f), 0.0001);
  }

  @Test
  public void shouldReturnSmallNegativeAttractionWhenDistanceIsSmallAndNegative()
  {
    SawAttraction attraction = new SawAttraction(0.5f, 0.2f);
    assertEquals("The attraction near a pole should be small", -0.05f, attraction.attract(-0.1f), 0.0001);
  }

  @Test
  public void shouldReturnMaxPositiveAttractionWhenDistanceIsOverPositiveLimit()
  {
    SawAttraction attraction = new SawAttraction(0.5f, 0.2f);
    assertEquals("The attraction at the limit should be maximum", 0.1f, attraction.attract(0.2f), 0.0001);
  }

  @Test
  public void shouldReturnMaxNegativeAttractionWhenDistanceIsOverNegativeLimit()
  {
    SawAttraction attraction = new SawAttraction(0.5f, 0.2f);
    assertEquals("The attraction at the limit should be maximum", -0.1f, attraction.attract(-0.2f), 0.0001);
  }

  @Test
  public void shouldReturnSmallPositiveAttractionWhenDistanceIsNearPositiveLimit()
  {
    SawAttraction attraction = new SawAttraction(0.5f, 0.2f);
    assertEquals("The attraction far from a pole should be small", 0.05f, attraction.attract(0.3f), 0.0001);
  }

  @Test
  public void shouldReturnSmallNegativeAttractionWhenDistanceIsNearNegativeLimit()
  {
    SawAttraction attraction = new SawAttraction(0.5f, 0.2f);
    assertEquals("The attraction far from a pole should be small", -0.05f, attraction.attract(-0.3f), 0.0001);
  }
}
