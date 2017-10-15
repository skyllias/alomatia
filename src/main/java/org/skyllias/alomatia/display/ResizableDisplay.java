
package org.skyllias.alomatia.display;

/** Display of images whose contents can be resized. */

public interface ResizableDisplay
{
//==============================================================================

  /** Modifies the size of the displayed image so that the passed policy is satisfied. */

  void setFitZoom(DisplayFitPolicy type);

//------------------------------------------------------------------------------

  /** Modifies the size of the displayed image.
   *  <p>
   *  A value of 1 means the original size, while values between 0 and 1
   *  proportionally mean smaller display sizes, and values over 1 mean larger sizes.
   *  <p>
   *  The current DisplayFitPolicy is ignored and the factor is applied right through. */

  void setZoomFactor(double factor);

//------------------------------------------------------------------------------

}
