
package org.skyllias.alomatia.filter.daltonism;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Color;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DaltonizingColourConverterTest
{
  @Mock
  private ProjectableColourFactory projectableColourFactory;
  @Mock
  private ColourProjector colourProjector;

  @InjectMocks
  private DaltonizingColourConverter converter;

  @Before
  public void setUp()
  {
    ProjectableColour projectableColour = mock(ProjectableColour.class);
    when(projectableColour.project(colourProjector))
      .thenReturn(new Color(250, 200, 150));

    when(projectableColourFactory.createProjectableColour(new Color(50, 100, 150)))
      .thenReturn(projectableColour);
  }

  @Test
  public void shouldProject()
  {
    assertEquals(new Color(250, 200, 150), converter.convertColour(new Color(50, 100, 150)));
  }

}
