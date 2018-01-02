
package org.skyllias.alomatia.ui;

import java.awt.dnd.*;

import javax.swing.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.source.*;
import org.skyllias.alomatia.ui.frame.*;

/** Container for the visual controls to choose the options.
 *  It includes a source selector, a filter selector and a display zoom selector. */

@SuppressWarnings("serial")
public class ControlsPane extends JPanel
{
//==============================================================================

  public ControlsPane(LabelLocalizer localizer, SourceCatalogue catalogue,
                      Repeater displayRepeater, DropTargetListener dropTargetListener,
                      DisplayFrameManager frameManager, FramePolicy framePolicy,
                      FileImageSaver imageSaver)
  {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    add(new SourceSelector(localizer, catalogue));
    add(new WindowControlPanel(localizer, displayRepeater, dropTargetListener,
                               frameManager, framePolicy));
    add(new SaveFilePanelComposer(localizer, imageSaver).getComponent());
    add(new LanguagePanel(localizer, new AvailableLocaleProvider()));           // TODO move instantiation upwards
  }

//==============================================================================

}
