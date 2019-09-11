
package org.skyllias.alomatia.filter.factor;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ElasticFactorTest
{
  @Test
  public void shouldRemainZeroWhenOriginalMagnitudeIsZeroAndFactorIsNegative()
  {
    UnitFactor factor = new ElasticFactor(-14);
    assertEquals("The magnitude should remain zero when a decreasing factor is applied", 0, factor.apply(0), 0.000001);
  }

  @Test
  public void shouldRemainZeroWhenOriginalMagnitudeIsZeroAndFactorIsPositive()
  {
    UnitFactor factor = new ElasticFactor(14);
    assertEquals("The magnitude should remain zero when an increasing factor is applied", 0, factor.apply(0), 0.000001);
  }

  @Test
  public void shouldRemainOneWhenOriginalMagnitudeIsOneAndFactorIsPositive()
  {
    UnitFactor factor = new ElasticFactor(14);
    assertEquals("The magnitude should remain one when an increasing factor is applied", 1, factor.apply(1), 0.000001);
  }

  @Test
  public void shouldRemainOneWhenOriginalMagnitudeIsOneAndFactorIsNegative()
  {
    UnitFactor factor = new ElasticFactor(-14);
    assertEquals("The magnitude should remain one when a decreasing factor is applied", 1, factor.apply(1), 0.000001);
  }

  @Test
  public void shouldNotChangeWhenOriginalMagnitudeIsLowAndFactorIsZero()
  {
    UnitFactor factor = new ElasticFactor(0);
    assertEquals("The magnitude should remain the same when zero factor is applied", 0.3, factor.apply(0.3f), 0.000001);
  }

  @Test
  public void shouldNotChangeWhenOriginalMagnitudeIsHighAndFactorIsZero()
  {
    UnitFactor factor = new ElasticFactor(0);
    assertEquals("The magnitude should remain the same when zero factor is applied", 0.7, factor.apply(0.7f), 0.000001);
  }

}
