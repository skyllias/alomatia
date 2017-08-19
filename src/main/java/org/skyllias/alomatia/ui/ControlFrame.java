
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.dnd.*;

import javax.swing.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.source.*;

/** Window where the source and the display images are managed.
 *  <p>
 *  Only one of these is expected in a given application.
 *  <p>
 *  After becoming visible, opens a new {@link DisplayWindow}. */

@SuppressWarnings("serial")
public class ControlFrame extends BasicAlomatiaWindow
{
  private static final String TITLE = "control.window.title";

//==============================================================================

  /** The catalogue is left untouched, although it is expected to have all the
   *  sources with the repeater set as display. If it contains a DropSource,
   *  this frame is added as drop target. */

  public ControlFrame(LabelLocalizer labelLocalizer, SourceCatalogue catalogue,
                      Repeater displayRepeater, FilterFactory filterFactory)
  {
    super(labelLocalizer, TITLE);

    DropTargetListener dropListener = catalogue.get(DropSource.class);          // if not present it will be null
    if (dropListener != null) new DropTarget(this, dropListener);

    DisplayFrameManager frameManager = new DisplayFrameManager(getLabelLocalizer(), filterFactory);
    ControlsPane controlsPane        = new ControlsPane(getLabelLocalizer(), catalogue,
                                                        displayRepeater, dropListener,
                                                        frameManager);
    getContentPane().add(controlsPane, BorderLayout.CENTER);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    setExtendedState(NORMAL);                                                   // this is forced because some desktop managers maximize all windows by default, and this looks better if really packed
    pack();
  }

//==============================================================================

}
