
package org.skyllias.alomatia.filter.compose;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.awt.image.*;

import org.junit.*;
import org.mockito.*;

public class ComposedFilterTest
{
  @Mock
  private ImageFilter imageFilter1;
  @Mock
  private ImageFilter imageFilter2;
  @Mock
  private ImageFilter imageFilter3;
  @Mock
  private ImageFilter imageFilter4;
  @Mock
  private ImageConsumer imageConsumer;
  private ComposedFilter composedFilter;

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);

    when(imageFilter1.getFilterInstance(any(ImageConsumer.class))).thenReturn(imageFilter1);
    when(imageFilter2.getFilterInstance(any(ImageConsumer.class))).thenReturn(imageFilter2);
    when(imageFilter3.getFilterInstance(any(ImageConsumer.class))).thenReturn(imageFilter3);
    when(imageFilter4.getFilterInstance(any(ImageConsumer.class))).thenReturn(imageFilter4);
  }

  @Test
  public void shouldChainAllFilters()
  {
    composedFilter     = new ComposedFilter(imageFilter1, imageFilter2, imageFilter3, imageFilter4);
    ImageFilter result = composedFilter.getFilterInstance(imageConsumer);
    assertEquals("The composition should return the first filter", imageFilter1, result);
    verify(imageFilter1).getFilterInstance(imageFilter2);
    verify(imageFilter2).getFilterInstance(imageFilter3);
    verify(imageFilter3).getFilterInstance(imageFilter4);
    verify(imageFilter4).getFilterInstance(imageConsumer);
  }
}
