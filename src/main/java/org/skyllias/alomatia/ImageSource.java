
package org.skyllias.alomatia;

/** "Producer" of images that will be displayed (in an {@link ImageDisplay})
 *  verbatim or after some modification.
 *  <p>
 *  Images produced in a source are passed directly to one ImageDisplay, if present.
 *  Their manipulation, if any, is left to the display. Therefore, sources know
 *  nothing about filters to apply to the original images.
 *  <p>
 *  Different images may be produced over time at the implementation's wish.
 *  Since the generation may be a resource-consuming operation, sources can be
 *  turned on and off. Multiple sources can share a single display, but if so
 *  at most one of them should be on at a given time. That control is not carried
 *  out by sources themselves. */

public interface ImageSource
{
  /** Sets the display where original images must be sent.
   *  If null, no images are produced. */

  void setDisplay(ImageDisplay display);

  /** Turns the production of images on (if active is true) or off (if active is false). */

  void setActive(boolean active);
}
