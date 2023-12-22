
package org.skyllias.alomatia.filter.buffered.surround;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class LightMeterTest
{
  private static final float ERROR_TOLERANCE = 0.0001f;

  @Test
  public void shouldReturnZeroWhenAllBlack()
  {
    Collection<Color> colours = Arrays.asList(new Color(0, 0, 0),
                                              new Color(0, 0, 0),
                                              new Color(0, 0, 0),
                                              new Color(0, 0, 0));

    assertEquals(0f, new LightMeter().getLight(colours), ERROR_TOLERANCE);
  }

  @Test
  public void shouldReturnOneWhenAllWhite()
  {
    Collection<Color> colours = Arrays.asList(new Color(255, 255, 255),
                                              new Color(255, 255, 255),
                                              new Color(255, 255, 255),
                                              new Color(255, 255, 255));

    assertEquals(1f, new LightMeter().getLight(colours), ERROR_TOLERANCE);
  }

  @Test
  public void shouldReturnHalfWhenMixed()
  {
    Collection<Color> colours = Arrays.asList(new Color(255, 255, 255),
                                              new Color(255, 255, 0),
                                              new Color(0, 127, 128),
                                              new Color(0, 0, 0));

    assertEquals(0.5f, new LightMeter().getLight(colours), ERROR_TOLERANCE);
  }

  @Test
  public void shouldNotOverflowWhenManyColours()
  {
    Collection<Color> colours = Stream.generate(() -> new Color(255, 0, 0))
                                      .limit(6_000_000)
                                      .collect(Collectors.toList());

    assertEquals(0.3333f, new LightMeter().getLight(colours), ERROR_TOLERANCE);
  }

}
