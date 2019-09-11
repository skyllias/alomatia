
package org.skyllias.alomatia.filter.factor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AntiBoostingFactorTest
{
  @Test
  public void shouldRemainZeroWhenOriginalMagnitudeIsZeroAndFactorIsNegative()
  {
    UnitFactor factor = new AntiBoostingFactor(-14);
    assertEquals("The magnitude should remain zero when a decreasing factor is applied", 0, factor.apply(0), 0.000001);
  }

  @Test
  public void shouldRemainZeroWhenOriginalMagnitudeIsZeroAndBoostIsAvoided()
  {
    UnitFactor factor = new AntiBoostingFactor(14);
    assertEquals("The magnitude should remain zero when a boost is avoided", 0, factor.apply(0), 0.000001);
  }

  @Test
  public void shouldRemainOneWhenOriginalMagnitudeIsOneAndFactorIsPositive()
  {
    UnitFactor factor = new AntiBoostingFactor(14);
    assertEquals("The magnitude should remain one when an increasing factor is applied", 1, factor.apply(1), 0.000001);
  }

  @Test
  public void shouldNotRemainOneWhenOriginalMagnitudeIsOneAndFactorIsNegative()
  {
    UnitFactor factor = new AntiBoostingFactor(-14);
    assertTrue("The magnitude should be decreased", 1 > factor.apply(1));
  }

  @Test
  public void shouldNotChangeWhenOriginalMagnitudeIsLowAndFactorIsZero()
  {
    UnitFactor factor = new AntiBoostingFactor(0);
    assertEquals("The magnitude should remain the same when zero factor is applied", 0.3, factor.apply(0.3f), 0.000001);
  }

  @Test
  public void shouldNotChangeWhenOriginalMagnitudeIsHighAndFactorIsZero()
  {
    UnitFactor factor = new AntiBoostingFactor(0);
    assertEquals("The magnitude should remain the same when zero factor is applied", 0.7, factor.apply(0.7f), 0.000001);
  }
}
