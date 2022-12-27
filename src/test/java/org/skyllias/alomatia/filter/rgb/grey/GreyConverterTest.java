
package org.skyllias.alomatia.filter.rgb.grey;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.awt.Color;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GreyConverterTest
{
  @Mock
  private GreyShadeProvider greyShadeProvider;

  @InjectMocks
  private GreyConverter greyConverter;

  @Test
  public void shouldReturnGreyShadeFromProvider()
  {
    when(greyShadeProvider.getShade(120,  240,  40)).thenReturn(100);

    Color result = greyConverter.convertColour(new Color(120,  240,  40));
    assertEquals(new Color(100,  100,  100), result);
  }

}
