
package org.skyllias.alomatia.display;

/** Enumeration for the possible ways of fitting an image in some display. */

public enum DisplayFitPolicy
{
  /** The image is not resized or it is zoomed in or out without consideration
   *  of the display dimensions. */

  FREE,

  /** The image is resized so that it is fully visible with the biggest possible size. */

  FULL,

  /** The image is resized so that its width is the same as the container's. */

  HORIZONTAL,

  /** The image is resized so that its height is the same as the container's. */

  VERTICAL,

  /** The image is resized so that either its height or its width is the same as
   *  the container's. The dimension that makes the image largest is chosen. */

  LARGEST,
}
