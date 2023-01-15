
package org.skyllias.alomatia;

/** "Producer" of images that will be displayed (in an {@link ImageDisplay})
 *  verbatim or after some modification.
 *  Images produced in a source are passed directly to one ImageDisplay, if present.
 *  Their manipulation, if any, is left to the display. Therefore, sources know
 *  nothing about filters to apply to the original images.
 *  {@link ImageDisplay.setOriginalImage(Image)} is invoked whenever the source
 *  considers a new image has to be displayed. As displays are assumed to show
 *  images in visual components, calls happen always in the event dispatch
 *  thread.
 *  Different images may be produced over time at the implementation's wish.
 *  Since the generation may be a resource-consuming operation, sources can be
 *  turned on and off. Multiple sources can share a single display, but if so
 *  at most one of them should be on at a given time. That control is not carried
 *  out by sources themselves. */

public interface ImageSource
{
  /** Turns the production of images on (if active is true) or off (if active is false).
   *  A new image may or may not be provided immediately. */

  void setActive(boolean active);
}
