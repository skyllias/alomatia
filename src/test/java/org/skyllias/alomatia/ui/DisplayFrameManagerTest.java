
package org.skyllias.alomatia.ui;

import static org.mockito.Mockito.*;

import java.awt.*;
import java.util.*;

import org.junit.*;
import org.mockito.*;
import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.ui.frame.*;

public class DisplayFrameManagerTest
{
  @Mock
  private FrameAdaptorFactory adaptorFactory;

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldSpanAllBoundsWhenSingleWindowRearranged()
  {
    when(adaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(20, 30, 500, 200));

    DisplayFrame displayFrame   = mock(DisplayFrame.class);
    DisplayFrameManager manager = new DisplayFrameManager(new KeyLabelLocalizer(), new FixedFilterFactory(),
                                                          adaptorFactory, Arrays.asList(displayFrame));
    manager.rearrangeWindows(1, false);
    verify(displayFrame).setLocation(20, 30);
    verify(displayFrame).setSize(500, 200);
  }

  @Test
  public void shouldSplitBoundsHorizontallyWhenTwoWindowsRearrangedInTwoColumns()
  {
    when(adaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(20, 30, 500, 200));

    DisplayFrame frame1         = mock(DisplayFrame.class);
    DisplayFrame frame2         = mock(DisplayFrame.class);
    DisplayFrameManager manager = new DisplayFrameManager(new KeyLabelLocalizer(), new FixedFilterFactory(),
                                                          adaptorFactory, Arrays.asList(frame1, frame2));
    manager.rearrangeWindows(2, true);
    verify(frame1).setLocation(20, 30);
    verify(frame2).setLocation(270, 30);
    verify(frame1).setSize(250, 200);
    verify(frame2).setSize(250, 200);
  }

  @Test
  public void shouldSplitBoundsHorizontallyWhenTwoWindowsRearrangedInOneRow()
  {
    when(adaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(20, 30, 500, 200));

    DisplayFrame frame1         = mock(DisplayFrame.class);
    DisplayFrame frame2         = mock(DisplayFrame.class);
    DisplayFrameManager manager = new DisplayFrameManager(new KeyLabelLocalizer(), new FixedFilterFactory(),
                                                          adaptorFactory, Arrays.asList(frame1, frame2));
    manager.rearrangeWindows(1, false);
    verify(frame1).setLocation(20, 30);
    verify(frame2).setLocation(270, 30);
    verify(frame1).setSize(250, 200);
    verify(frame2).setSize(250, 200);
  }

  @Test
  public void shouldSplitBoundsVerticallyWhenTwoWindowsRearrangedInTwoRows()
  {
    when(adaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(20, 30, 500, 200));

    DisplayFrame frame1         = mock(DisplayFrame.class);
    DisplayFrame frame2         = mock(DisplayFrame.class);
    DisplayFrameManager manager = new DisplayFrameManager(new KeyLabelLocalizer(), new FixedFilterFactory(),
                                                          adaptorFactory, Arrays.asList(frame1, frame2));
    manager.rearrangeWindows(2, false);
    verify(frame1).setLocation(20, 30);
    verify(frame2).setLocation(20, 130);
    verify(frame1).setSize(500, 100);
    verify(frame2).setSize(500, 100);
  }

  @Test
  public void shouldSplitBoundsVerticallyWhenTwoWindowsRearrangedInOneColumn()
  {
    when(adaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(20, 30, 500, 200));

    DisplayFrame frame1         = mock(DisplayFrame.class);
    DisplayFrame frame2         = mock(DisplayFrame.class);
    DisplayFrameManager manager = new DisplayFrameManager(new KeyLabelLocalizer(), new FixedFilterFactory(),
                                                          adaptorFactory, Arrays.asList(frame1, frame2));
    manager.rearrangeWindows(1, true);
    verify(frame1).setLocation(20, 30);
    verify(frame2).setLocation(20, 130);
    verify(frame1).setSize(500, 100);
    verify(frame2).setSize(500, 100);
  }

  @Test
  public void shouldLeaveEmptyCellWhenFiveWindowsRearrangedInThreeLines()
  {
    when(adaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(100, 200, 600, 300));

    DisplayFrame frame1         = mock(DisplayFrame.class);
    DisplayFrame frame2         = mock(DisplayFrame.class);
    DisplayFrame frame3         = mock(DisplayFrame.class);
    DisplayFrame frame4         = mock(DisplayFrame.class);
    DisplayFrame frame5         = mock(DisplayFrame.class);
    DisplayFrameManager manager = new DisplayFrameManager(new KeyLabelLocalizer(), new FixedFilterFactory(),
                                                          adaptorFactory, Arrays.asList(frame1, frame2, frame3, frame4, frame5));
    manager.rearrangeWindows(3, true);
    verify(frame1).setLocation(100, 200);
    verify(frame2).setLocation(300, 200);
    verify(frame3).setLocation(500, 200);
    verify(frame4).setLocation(100, 350);
    verify(frame5).setLocation(300, 350);
    verify(frame1).setSize(200, 150);
    verify(frame2).setSize(200, 150);
    verify(frame3).setSize(200, 150);
    verify(frame4).setSize(200, 150);
    verify(frame5).setSize(200, 150);
  }
}
