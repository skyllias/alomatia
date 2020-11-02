
package org.skyllias.alomatia.filter.factor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ComposedUnitFactorTest
{
  @Test
  public void shouldNotChangeWhenOriginalMagnitudeIsLowAndFactorIsZero()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(0);
    assertEquals("The magnitude should remain the same when zero factor is applied", 0.3, factor.apply(0.3f), 0.000001);
  }

  @Test
  public void shouldNotChangeWhenOriginalMagnitudeIsHighAndFactorIsZero()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(0);
    assertEquals("The magnitude should remain the same when zero factor is applied", 0.7, factor.apply(0.7f), 0.000001);
  }

  @Test
  public void shouldRemainZeroWhenOriginalMagnitudeIsZeroAndFactorIsNegative()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(-14);
    assertEquals("The magnitude should remain zero when a decreasing factor is applied", 0, factor.apply(0), 0.000001);
  }

  @Test
  public void shouldRemainZeroWhenOriginalMagnitudeIsZeroAndFactorIsPositive()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(14);
    assertEquals("The magnitude should remain zero when an increasing factor is applied", 0, factor.apply(0), 0.000001);
  }

  @Test
  public void shouldRemainOneWhenOriginalMagnitudeIsOneAndFactorIsNegative()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(-14);
    assertEquals("The magnitude should remain one when a decreasing factor is applied", 1, factor.apply(1), 0.000001);
  }

  @Test
  public void shouldRemainOneWhenOriginalMagnitudeIsOneAndFactorIsPositive()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(14);
    assertEquals("The magnitude should remain one when an increasing factor is applied", 1, factor.apply(1), 0.000001);
  }

  @Test
  public void shouldRemainHalfWhenOriginalMagnitudeIsHalfAndFactorIsNegative()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(-14);
    assertEquals("The magnitude should remain half when a decreasing factor is applied", 0.5, factor.apply(0.5f), 0.000001);
  }

  @Test
  public void shouldRemainHalfWhenOriginalMagnitudeIsHalfAndFactorIsPositive()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(14);
    assertEquals("The magnitude should remain half when an increasing factor is applied", 0.5, factor.apply(0.5f), 0.000001);
  }

  @Test
  public void shouldIncreaseWhenOriginalMagnitudeIsLowAndFactorIsNegative()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(-14);
    assertTrue("The magnitude should increase when a negative factor is applied", 0.3 < factor.apply(0.3f));
  }

  @Test
  public void shouldDecreaseWhenOriginalMagnitudeIsHighAndFactorIsNegative()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(-14);
    assertTrue("The magnitude should decrease when a negative factor is applied", 0.7 > factor.apply(0.7f));
  }

  @Test
  public void shouldDecreaseWhenOriginalMagnitudeIsLowAndFactorIsPositive()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(14);
    assertTrue("The magnitude should decrease when a positive factor is applied", 0.3 > factor.apply(0.3f));
  }

  @Test
  public void shouldIncreaseWhenOriginalMagnitudeIsHighAndFactorIsPositive()
  {
    ComposedUnitFactor factor = new ComposedUnitFactor(14);
    assertTrue("The magnitude should increase when a positive factor is applied", 0.7 < factor.apply(0.7f));
  }
}
