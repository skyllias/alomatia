
package org.skyllias.alomatia.display;

import static org.mockito.Mockito.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;
import org.skyllias.alomatia.*;

public class RepeaterTest
{
  @Mock
  private ImageDisplay receiver1, receiver2, receiver3;
  private Image image = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_GRAY);

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldDispatchToReceiversFromConstructor()
  {
    Repeater repeater = new Repeater(Arrays.asList(receiver1, receiver2, receiver3));
    repeater.setOriginalImage(image);
    verify(receiver1, times(1)).setOriginalImage(image);
    verify(receiver2, times(1)).setOriginalImage(image);
    verify(receiver3, times(1)).setOriginalImage(image);
  }

  @Test
  public void shouldDispatchToReceiversIndividuallySet()
  {
    Repeater repeater = new Repeater();
    repeater.addReceiver(receiver1);
    repeater.addReceiver(receiver2);
    repeater.addReceiver(receiver3);
    repeater.setOriginalImage(image);
    verify(receiver1, times(1)).setOriginalImage(image);
    verify(receiver2, times(1)).setOriginalImage(image);
    verify(receiver3, times(1)).setOriginalImage(image);
  }

  @Test
  public void shouldDispatchToReceiversAddedAfterImage()
  {
    Repeater repeater = new Repeater();
    repeater.addReceiver(receiver1);
    repeater.addReceiver(receiver2);
    repeater.setOriginalImage(image);
    repeater.addReceiver(receiver3);
    verify(receiver1, times(1)).setOriginalImage(image);
    verify(receiver2, times(1)).setOriginalImage(image);
    verify(receiver3, times(1)).setOriginalImage(image);
  }

  @Test
  public void shouldNotDispatchToReceiversRemovedOrNotAdded()
  {
    Repeater repeater = new Repeater();
    repeater.addReceiver(receiver1);
    repeater.addReceiver(receiver2);
    repeater.removeReceiver(receiver1);
    repeater.setOriginalImage(image);
    verify(receiver1, times(0)).setOriginalImage(image);
    verify(receiver2, times(1)).setOriginalImage(image);
    verify(receiver3, times(0)).setOriginalImage(image);
  }
}
