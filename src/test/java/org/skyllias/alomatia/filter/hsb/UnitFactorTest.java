
package org.skyllias.alomatia.filter.hsb;

import static org.junit.Assert.*;

import org.junit.*;

public class UnitFactorTest
{
  @Test
  public void shouldRemainZeroWhenOriginalMagnitudeIsZeroAndFactorIsNegative()
  {
    UnitFactor factor = new UnitFactor(-14);
    assertEquals("The magnitude should remain zero when a decreasing factor is applied", 0, factor.apply(0), 0.0001);
  }

  @Test
  public void shouldNotRemainZeroWhenOriginalMagnitudeIsZeroAndFactorIsPositive()
  {
    UnitFactor factor = new UnitFactor(14);
    assertTrue("The magnitude should be increased", 0 < factor.apply(0));
  }

  @Test
  public void shouldRemainOneWhenOriginalMagnitudeIsOneAndFactorIsPositive()
  {
    UnitFactor factor = new UnitFactor(14);
    assertEquals("The magnitude should remain one when an increasing factor is applied", 1, factor.apply(1), 0.0001);
  }

  @Test
  public void shouldNotRemainOneWhenOriginalMagnitudeIsOneAndFactorIsNegative()
  {
    UnitFactor factor = new UnitFactor(-14);
    assertTrue("The magnitude should be decreased", 1 > factor.apply(1));
  }

  @Test
  public void shouldNotChangeWhenOriginalMagnitudeIsLowAndFactorIsZero()
  {
    UnitFactor factor = new UnitFactor(0);
    assertEquals("The magnitude should remain the same when zero factor is applied", 0.3, factor.apply(0.3f), 0.0001);
  }

  @Test
  public void shouldNotChangeWhenOriginalMagnitudeIsHighAndFactorIsZero()
  {
    UnitFactor factor = new UnitFactor(0);
    assertEquals("The magnitude should remain the same when zero factor is applied", 0.7, factor.apply(0.7f), 0.0001);
  }
}
