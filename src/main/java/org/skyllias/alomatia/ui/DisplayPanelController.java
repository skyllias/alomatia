
package org.skyllias.alomatia.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.ImageFilter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.display.DisplayFitPolicy;
import org.skyllias.alomatia.display.ResizableDisplay;
import org.skyllias.alomatia.filter.FilteredImageGenerator;

/** Provider of a panel where images are drawn after manipulation, along with
 *  the associated logic.
 *  <p>
 *  For an image to be modified, both an original image and a filter are needed.
 *  If there is no original image, nothing is displayed; if no filter, the
 *  original image is shown without change.
 *  <p>
 *  There is one instance per display panel. */

@SuppressWarnings("serial")
public class DisplayPanelController implements ImageDisplay, ResizableDisplay, ComponentListener
{
  private static final int UNAVAILABLE_SIZE = -1;                               // value returned by Image.getWidth(ImageObserver) and getHeight() when they are still unavailable
  private static final int SCROLL_INCREMENT = 16;

  private final FilteredImageGenerator filteredImageGenerator;

  private final Executor executor = Executors.newSingleThreadExecutor();        // filter application can be very slow, so operations that imply it are thrown in separate threads. Using a single thread ensures that no race conditions will happen, but if this class were to be tested, the Executor implementation could be changed

  private final JScrollPane component;
  private final ImagePanel imagePanel = new ImagePanel();

  private final State state = new State();

//==============================================================================

  /** Creates a new instance with double buffering enabled to reduce flickering. */

  public DisplayPanelController(FilteredImageGenerator filteredImageGenerator)
  {
    this.filteredImageGenerator = filteredImageGenerator;

    component = new JScrollPane();
    component.setDoubleBuffered(true);
    component.setViewportView(imagePanel);

    component.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);        // it would be much nicer to use the default _AS_NEEDED policies, but then the calculations in getResizeFactorToFit would have to account for the scrollbars that would appear or disappear, which is not worth the effort for the moment
    component.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    component.getHorizontalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
    component.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);

    component.addComponentListener(this);
  }

//==============================================================================

  /** The filter is applied in a separate thread, so this returns almost immediately. */

  public void setImageFilter(ImageFilter imageFilter)
  {
    state.filter = imageFilter;

    executor.execute(new Runnable()
    {
      @Override
      public void run()
      {
        filterImage();
        imagePanel.repaint();
      }
    });
  }

//------------------------------------------------------------------------------

  /** Modifies the image to filter and display.
   *  <p>
   *  The pane is resized to the image's size.
   *  <p>
   *  The current filter, if any, is applied in a separate thread, so this
   *  returns almost immediately. */

  @Override
  public void setOriginalImage(Image image)
  {
    state.originalImage = image;

    executor.execute(new Runnable()
    {
      @Override
      public void run()
      {
        filterImage();
        resizeImageToFit();
        repaintAfterResize();                                                   // this is redundant for everything but fitType == FREE
      }
    });
  }

//------------------------------------------------------------------------------

  /** Returns the image with the filter applied.
   *  It may be null if not yet calculated.
   *  Usually this will be the image visible in the panel, but if invoked just
   *  before a repaint or while the filter is being applied, it may not be
   *  updated yet. */

  public Image getFilteredImage() {return state.filteredImage;}

//------------------------------------------------------------------------------

  /** Modifies the zoom over the painted image and forces a repaint. */

  @Override
  public void setZoomFactor(double factor)
  {
    state.scale = factor;

    repaintAfterResize();
  }

//------------------------------------------------------------------------------

  /** Internally modifies the zoom factor so that the passed policy is satisfied.
   *  <p>
   *  If the panel is resized in the future, the factor will be recalculated unless FREE. */

  @Override
  public void setFitZoom(DisplayFitPolicy type)
  {
    state.fitType = type;

    resizeImageToFit();
  }

//------------------------------------------------------------------------------

  /** Returns the UI component used to display images. */

  public JComponent getComponent() {return component;}

//------------------------------------------------------------------------------

  /* Calculates the proper scale for the image to fit the viewport according to
   * the current fitType.
   * If fitType is FREE, nothing happens. Else, the new zoom factor is
   * calculated for the image to fit and it is set by invoking setZoomFactor,
   * which will in turn force a repaint.
   * Everything assuming there is already an image, of course. */

  private void resizeImageToFit()
  {
    double calculatedFactor = getResizeFactorToFit();
    if (calculatedFactor != 0) setZoomFactor(calculatedFactor);
  }

//------------------------------------------------------------------------------

  /* Calculates the proper scale for the image to fit the viewport according to
   * the current fitType.
   * If fitType is FREE or there is no image available, 0 is returned. */

  @SuppressWarnings("incomplete-switch")
  private double getResizeFactorToFit()
  {
    if (state.fitType != DisplayFitPolicy.FREE)
    {
      if (state.originalImage != null)
      {
        int imgHeight = state.originalImage.getHeight(null);                    // an ImageObserver could be used to repaint when the image is available again, but most producers are providing BufferedImages so dimensions will always be available
        int imgWidth  = state.originalImage.getWidth(null);
        if (imgHeight != UNAVAILABLE_SIZE && imgHeight != 0 &&
            imgWidth != UNAVAILABLE_SIZE && imgWidth != 0)
        {
          int portHeight      = component.getViewport().getHeight();            // these lines assume that the scrollbars will not appear or disappear after the resize
          int portWidth       = component.getViewport().getWidth();
          double heightFactor = ((double) portHeight) / (double) imgHeight;     // amount to multiply the image by to fit the viewport
          double widthFactor  = ((double) portWidth) / (double) imgWidth;

          switch (state.fitType)
          {
            case FULL:       return Math.min(heightFactor, widthFactor);
            case LARGEST:    return Math.max(heightFactor, widthFactor);
            case HORIZONTAL: return widthFactor;
            case VERTICAL:   return heightFactor;
          }
        }
      }
    }
    return 0;
  }

//------------------------------------------------------------------------------

  /* Forces a resize of the image panel to recalculate the viewport and then a
   * repaint to display the image, not necessarilly refiltered. */

  private void repaintAfterResize()
  {
    imagePanel.setSize(getImageSize());                                         // this makes the parent recalculate the scrollbars
    component.repaint();                                                        // this updates the image contents
  }

//------------------------------------------------------------------------------

  /* Returns the size of the image, if any, scaled, which should be used as the
   * image panel's size. */

  private Dimension getImageSize()
  {
    if (state.originalImage != null)
    {
      int height = state.originalImage.getHeight(null);                         // see comment at getResizeFactorToFit()
      int width  = state.originalImage.getWidth(null);
      if (height != UNAVAILABLE_SIZE && width != UNAVAILABLE_SIZE)
      {
        height *= state.scale;
        width  *= state.scale;
        return new Dimension(width, height);
      }
    }

    return new Dimension(0, 0);
  }

//------------------------------------------------------------------------------

  /* If there is an original image, updates the filtered image by applying the
   * current filter (if any) to the original one.
   * This method should be called if and only if the original image or the
   * filter change.
   * It can be a consuming calculation, so the cursor is changed while it takes
   * place. */

  private void filterImage()
  {
    if (state.originalImage != null)
    {
      component.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      state.filteredImage = state.originalImage;
      if (state.filter != null)
      {
        state.filteredImage = filteredImageGenerator.generate(state.originalImage, state.filter);     // when scale < 1, some performance improvement could be achieved if the filter were applied to the reduced image, but then it would have to be reapplied upon resizing
      }

      state.filteredImage.getWidth(null);                                       // what really takes time in slow filters and big images is not the filtering lines above, because the image is not really generated until one method like getWidth() is called

      component.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

//------------------------------------------------------------------------------
//                           COMPONENT LISTENER
//------------------------------------------------------------------------------

  /** Does nothing. */

  @Override
  public void componentHidden(ComponentEvent e) {}

  /** Does nothing. */

  @Override
  public void componentMoved(ComponentEvent e) {}

  /** Does nothing. */

  @Override
  public void componentShown(ComponentEvent e) {}

  /** Resizes the image according to the current fit type. */

  @Override
  public void componentResized(ComponentEvent e) {resizeImageToFit();}

//------------------------------------------------------------------------------

//******************************************************************************

  private static class State
  {
    private ImageFilter filter;
    private Image originalImage;
    private Image filteredImage;                                                // image after applying filters, if any. Cached because large images take a long time to filter, and it is redundant to filter them whenever a repaint happens

    private DisplayFitPolicy fitType = DisplayFitPolicy.FREE;
    private double scale = 1;                                                   // zoom factor: 1: normal size; <1: smaller; >1: bigger
  }

//******************************************************************************

  /* Panel contained inside the scroll pane where the image is really drawn. */

  private class ImagePanel extends JPanel
  {

//==============================================================================

    @Override
    public Dimension getPreferredSize() {return getImageSize();}

//------------------------------------------------------------------------------

    /** The filtered image (which may be the original image if there is no filter)
     *  is drawn at the top left corner.
     *  <p>
     *  The image is scaled according to the current zoom factor.
     *   */

    @Override
    public void paint(Graphics g)
    {
      Dimension scaledDimension = getPreferredSize();
      int scaledHeight          = (int) scaledDimension.getHeight();
      int scaledWidth           = (int) scaledDimension.getWidth();

      g.clearRect(0, 0, getWidth(), getHeight());                               // clear the whole panel, not just the region to paint on
      g.drawImage(state.filteredImage, 0, 0, scaledWidth, scaledHeight, null);  // getHeight() and getWidth() cannot be used because they are larger than the image's size if the viewport is bigger
    }

//------------------------------------------------------------------------------

  }

}
