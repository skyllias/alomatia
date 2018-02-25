
package org.skyllias.alomatia.ui;

import java.awt.dnd.*;

import javax.swing.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.source.*;
import org.skyllias.alomatia.ui.frame.*;

/** Container for the visual controls to choose the options.
 *  It includes a source selector, a filter selector and a display zoom selector.
 *  @deprecated User ControlsPaneComposer instead. */

@Deprecated
@SuppressWarnings("serial")
public class ControlsPane extends JPanel
{
//==============================================================================

  /** If a new window is opened, it is done after all the other processing in the
   *  current thread. Otherwise, the display frame appears before this one and
   *  separated from the subsequent windows in the task bar. */

  public ControlsPane(LabelLocalizer localizer, SourceCatalogue catalogue,
                      Repeater displayRepeater, DropTargetListener dropTargetListener,
                      DisplayFrameManager frameManager, FramePolicy framePolicy,
                      FileImageSaver imageSaver)
  {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    add(new SourceSelector(localizer, catalogue));
    final WindowControlPanelComposer windowControlComposer = new WindowControlPanelComposer(localizer, displayRepeater,
                                                                                      dropTargetListener,
                                                                                      frameManager, framePolicy);
    add(windowControlComposer.getComponent());
    add(new SaveFilePanelComposer(localizer, imageSaver).getComponent());
    add(new LanguagePanelComposer(localizer).getComponent());

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run() {windowControlComposer.openNewWindowIfRequired();}
    });
  }

//==============================================================================

}
