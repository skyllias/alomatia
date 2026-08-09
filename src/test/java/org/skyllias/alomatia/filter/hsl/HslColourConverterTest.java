
package org.skyllias.alomatia.filter.hsl;

import static org.junit.Assert.assertEquals;

import java.awt.Color;

import org.junit.Test;

public class HslColourConverterTest
{
  private static final float ERROR_TOLERANCE = 0.001f;

  @Test
  public void shouldNotModifyColoursWhenCoordinatesAreNotModified()
  {
    final Color[] SOME_COLOURS = {Color.BLACK, Color.WHITE, Color.GRAY,
                                  Color.DARK_GRAY, Color.LIGHT_GRAY,
                                  Color.RED, Color.GREEN, Color.BLUE,
                                  Color.YELLOW, Color.CYAN, Color.MAGENTA,
                                  new Color(1, 1, 2), new Color(10, 20, 30),
                                  new Color(180, 40, 40), new Color(200, 180, 240),
                                  new Color(255, 128, 0), new Color(254, 255, 250)};

    HslColourConverter converter = new HslColourConverter(new HslConverter() {});

    for (Color colour : SOME_COLOURS) assertEquals(colour, converter.convertColour(colour));
  }

  @Test
  public void shouldCalculateCoordinatesOfBlack()
  {
    RecordingHslConverter recorder = convert(Color.BLACK);

    assertEquals(0f, recorder.getSaturation(), ERROR_TOLERANCE);
    assertEquals(0f, recorder.getLightness(), ERROR_TOLERANCE);
  }

  @Test
  public void shouldCalculateCoordinatesOfWhite()
  {
    RecordingHslConverter recorder = convert(Color.WHITE);

    assertEquals(0f, recorder.getSaturation(), ERROR_TOLERANCE);
    assertEquals(1f, recorder.getLightness(), ERROR_TOLERANCE);
  }

  @Test
  public void shouldCalculateCoordinatesOfDarkGrey()
  {
    RecordingHslConverter recorder = convert(new Color(64, 64, 64));

    assertEquals(0f, recorder.getSaturation(), ERROR_TOLERANCE);
    assertEquals(0.251f, recorder.getLightness(), ERROR_TOLERANCE);
  }

  @Test
  public void shouldCalculateCoordinatesOfPureRed()
  {
    RecordingHslConverter recorder = convert(Color.RED);

    assertEquals(0f, recorder.getHue(), ERROR_TOLERANCE);
    assertEquals(1f, recorder.getSaturation(), ERROR_TOLERANCE);
    assertEquals(0.5f, recorder.getLightness(), ERROR_TOLERANCE);
  }

  @Test
  public void shouldCalculateFullSaturationOfPalePink()
  {
    RecordingHslConverter recorder = convert(new Color(255, 128, 128));

    assertEquals(0f, recorder.getHue(), ERROR_TOLERANCE);
    assertEquals(1f, recorder.getSaturation(), ERROR_TOLERANCE);
    assertEquals(0.751f, recorder.getLightness(), ERROR_TOLERANCE);
  }

  @Test
  public void shouldCalculateHigherLightnessThanBrightnessOfDarkColours()
  {
    RecordingHslConverter recorder = convert(new Color(180, 40, 40));

    assertEquals(0f, recorder.getHue(), ERROR_TOLERANCE);
    assertEquals(0.636f, recorder.getSaturation(), ERROR_TOLERANCE);
    assertEquals(0.431f, recorder.getLightness(), ERROR_TOLERANCE);
  }

  private RecordingHslConverter convert(Color colour)
  {
    RecordingHslConverter recorder = new RecordingHslConverter();
    new HslColourConverter(recorder).convertColour(colour);
    return recorder;
  }

//******************************************************************************

  private static class RecordingHslConverter implements HslConverter
  {
    private float hue;
    private float saturation;
    private float lightness;

    @Override
    public float getNewHue(float hue, float saturation, float lightness)
    {
      this.hue        = hue;
      this.saturation = saturation;
      this.lightness  = lightness;
      return hue;
    }

    public float getHue() {return hue;}

    public float getSaturation() {return saturation;}

    public float getLightness() {return lightness;}
  }

}
