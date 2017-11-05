
package org.skyllias.alomatia.filter.hsb;

import static org.junit.Assert.*;

import org.junit.*;

public class LinearRepulsionTest
{
  @Test
  public void shouldReturnStrengthWhenDifferenceIsZero()
  {
    LinearRepulsion repulsion = new LinearRepulsion(0.2f, 0.4f);
    assertEquals("The repulsion on a pole should be the strength", -0.2f, repulsion.attract(0), 0.001);
  }

  @Test
  public void shouldReturnAlmostStrengthWhenDifferenceIsZeroNegative()
  {
    LinearRepulsion repulsion = new LinearRepulsion(0.2f, 0.4f);
    assertEquals("The repulsion near a pole should be almost the strength", 0.199f, repulsion.attract(-0.002f), 0.00001);
  }

  @Test
  public void shouldReturnZeroWhenDifferenceIsFurtherFromPositiveRange()
  {
    LinearRepulsion repulsion = new LinearRepulsion(0.2f, 0.4f);
    assertEquals("The repulsion outside the range should be zero", 0, repulsion.attract(0.5f), 0);
  }

  @Test
  public void shouldReturnZeroWhenDifferenceIsFurtherFromNegativeRange()
  {
    LinearRepulsion repulsion = new LinearRepulsion(0.2f, 0.4f);
    assertEquals("The repulsion outside the range should be zero", 0, repulsion.attract(-0.5f), 0);
  }

  @Test
  public void shouldReturnAlmostZeroWhenDifferenceIsCloseToPositiveRange()
  {
    LinearRepulsion repulsion = new LinearRepulsion(0.2f, 0.4f);
    assertEquals("The repulsion near the range should be almost zero", -0.001, repulsion.attract(0.398f), 0.00001);
  }

  @Test
  public void shouldReturnAlmostZeroWhenDifferenceIsCloseToNegativeRange()
  {
    LinearRepulsion repulsion = new LinearRepulsion(0.2f, 0.4f);
    assertEquals("The repulsion near the range should be almost zero", 0.001, repulsion.attract(-0.398f), 0.00001);
  }
}
