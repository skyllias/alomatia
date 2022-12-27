
package org.skyllias.alomatia.filter.rgb.grey;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WeighedGreyShadeProviderTest
{
  @Mock
  private ChannelWeights channelWeights;

  @InjectMocks
  private WeighedGreyShadeProvider weighedGreyShadeProvider;

  @Test
  public void shouldReturnAverageWhenAllWeightsEqual()
  {
    mockWeights(1, 1, 1);

    assertEquals(120, weighedGreyShadeProvider.getShade(120,  120,  120));
    assertEquals(120, weighedGreyShadeProvider.getShade(120,  100,  140));
    assertEquals(60, weighedGreyShadeProvider.getShade(20,  80,  80));
    assertEquals(254, weighedGreyShadeProvider.getShade(254,  255,  254));
    assertEquals(255, weighedGreyShadeProvider.getShade(255,  255,  254));
  }

  @Test
  public void shouldReturnAverageWhenOneWeightHigher()
  {
    mockWeights(2, 2, 5);

    assertEquals(120, weighedGreyShadeProvider.getShade(120,  120,  120));
    assertEquals(127, weighedGreyShadeProvider.getShade(120,  100,  140));
    assertEquals(67, weighedGreyShadeProvider.getShade(20,  80,  80));
    assertEquals(254, weighedGreyShadeProvider.getShade(254,  255,  254));
    assertEquals(254, weighedGreyShadeProvider.getShade(255,  255,  254));
  }

  @Test
  public void shouldReturnAverageWhenOneWeightLower()
  {
    mockWeights(2, 2, 1);

    assertEquals(120, weighedGreyShadeProvider.getShade(120,  120,  120));
    assertEquals(116, weighedGreyShadeProvider.getShade(120,  100,  140));
    assertEquals(56, weighedGreyShadeProvider.getShade(20,  80,  80));
    assertEquals(254, weighedGreyShadeProvider.getShade(254,  255,  254));
    assertEquals(255, weighedGreyShadeProvider.getShade(255,  255,  254));
  }

  @Test
  public void shouldReturnAverageWhenAllWeightsDifferent()
  {
    mockWeights(1, 3, 2);

    assertEquals(120, weighedGreyShadeProvider.getShade(120,  120,  120));
    assertEquals(117, weighedGreyShadeProvider.getShade(120,  100,  140));
    assertEquals(70, weighedGreyShadeProvider.getShade(20,  80,  80));
    assertEquals(255, weighedGreyShadeProvider.getShade(254,  255,  254));
    assertEquals(255, weighedGreyShadeProvider.getShade(255,  255,  254));
  }


  private void mockWeights(int redWeight, int greenWeight, int blueWeight)
  {
    when(channelWeights.getRedWeight()).thenReturn(redWeight);
    when(channelWeights.getGreenWeight()).thenReturn(greenWeight);
    when(channelWeights.getBlueWeight()).thenReturn(blueWeight);
  }
}
