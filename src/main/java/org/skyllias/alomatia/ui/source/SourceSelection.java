
package org.skyllias.alomatia.ui.source;

import javax.swing.JComponent;

import org.skyllias.alomatia.ImageSource;

/** Composition of an {@link ImageSource} and a Swing component with controls
 *  to configure it.
 *  The source could be wrapped so that activating and deactivating it implies
 *  activating and deactivating some of the controls. It could also have a
 *  delayed activation so that the generation of images is delayed until some
 *  configuration is provided through the controls. */

public interface SourceSelection
{
  ImageSource getSource();

  JComponent getControls();
}
