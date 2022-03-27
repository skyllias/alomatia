
package org.skyllias.alomatia.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Rectangle;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.logo.LogoProducer;
import org.skyllias.alomatia.ui.filter.FilterSelectorComposer;
import org.skyllias.alomatia.ui.frame.FrameAdaptorFactory;
import org.skyllias.alomatia.ui.save.FileImageSaver;

public class DisplayFrameManagerTest
{
  @Mock
  private LabelLocalizer labelLocalizer;
  @Mock
  private LogoProducer logoProducer;
  @Mock
  private FrameAdaptorFactory frameAdaptorFactory;
  @Mock
  private FilteredImageGenerator filteredImageGenerator;
  @Mock
  private FilterSelectorComposer filterSelectorComposer;
  @Mock
  private DisplayOptionsDialogComposer displayOptionsDialogComposer;
  @Mock
  private FileImageSaver fileImageSaver;

  @InjectMocks
  private DisplayFrameManager manager;

  @Before
  public void setUp()
  {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldSpanAllBoundsWhenSingleWindowRearranged()
  {
    when(frameAdaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(20, 30, 500, 200));

    DisplayFrameController displayFrame = mock(DisplayFrameController.class);
    manager.addExistingFrames(Arrays.asList(displayFrame));

    manager.rearrangeWindows(1, false);
    verify(displayFrame).setLocation(20, 30);
    verify(displayFrame).setSize(500, 200);
  }

  @Test
  public void shouldSplitBoundsHorizontallyWhenTwoWindowsRearrangedInTwoColumns()
  {
    when(frameAdaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(20, 30, 500, 200));

    DisplayFrameController frame1 = mock(DisplayFrameController.class);
    DisplayFrameController frame2 = mock(DisplayFrameController.class);
    manager.addExistingFrames(Arrays.asList(frame1, frame2));

    manager.rearrangeWindows(2, true);
    verify(frame1).setLocation(20, 30);
    verify(frame2).setLocation(270, 30);
    verify(frame1).setSize(250, 200);
    verify(frame2).setSize(250, 200);
  }

  @Test
  public void shouldSplitBoundsHorizontallyWhenTwoWindowsRearrangedInOneRow()
  {
    when(frameAdaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(20, 30, 500, 200));

    DisplayFrameController frame1 = mock(DisplayFrameController.class);
    DisplayFrameController frame2 = mock(DisplayFrameController.class);
    manager.addExistingFrames(Arrays.asList(frame1, frame2));

    manager.rearrangeWindows(1, false);
    verify(frame1).setLocation(20, 30);
    verify(frame2).setLocation(270, 30);
    verify(frame1).setSize(250, 200);
    verify(frame2).setSize(250, 200);
  }

  @Test
  public void shouldSplitBoundsVerticallyWhenTwoWindowsRearrangedInTwoRows()
  {
    when(frameAdaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(20, 30, 500, 200));

    DisplayFrameController frame1 = mock(DisplayFrameController.class);
    DisplayFrameController frame2 = mock(DisplayFrameController.class);
    manager.addExistingFrames(Arrays.asList(frame1, frame2));

    manager.rearrangeWindows(2, false);
    verify(frame1).setLocation(20, 30);
    verify(frame2).setLocation(20, 130);
    verify(frame1).setSize(500, 100);
    verify(frame2).setSize(500, 100);
  }

  @Test
  public void shouldSplitBoundsVerticallyWhenTwoWindowsRearrangedInOneColumn()
  {
    when(frameAdaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(20, 30, 500, 200));

    DisplayFrameController frame1 = mock(DisplayFrameController.class);
    DisplayFrameController frame2 = mock(DisplayFrameController.class);
    manager.addExistingFrames(Arrays.asList(frame1, frame2));

    manager.rearrangeWindows(1, true);
    verify(frame1).setLocation(20, 30);
    verify(frame2).setLocation(20, 130);
    verify(frame1).setSize(500, 100);
    verify(frame2).setSize(500, 100);
  }

  @Test
  public void shouldLeaveEmptyCellWhenFiveWindowsRearrangedInThreeLines()
  {
    when(frameAdaptorFactory.getRearrengementBounds()).thenReturn(new Rectangle(100, 200, 600, 300));

    DisplayFrameController frame1 = mock(DisplayFrameController.class);
    DisplayFrameController frame2 = mock(DisplayFrameController.class);
    DisplayFrameController frame3 = mock(DisplayFrameController.class);
    DisplayFrameController frame4 = mock(DisplayFrameController.class);
    DisplayFrameController frame5 = mock(DisplayFrameController.class);
    manager.addExistingFrames(Arrays.asList(frame1, frame2, frame3, frame4, frame5));

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
