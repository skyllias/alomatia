
package org.skyllias.alomatia.ui.controls;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.skyllias.alomatia.ui.LanguagePanelComposer;
import org.skyllias.alomatia.ui.WindowControlPanelComposer;
import org.skyllias.alomatia.ui.save.FileImageSaver;
import org.skyllias.alomatia.ui.save.SaveFilePanelComposer;
import org.skyllias.alomatia.ui.source.SourceSelectorComposer;
import org.springframework.stereotype.Component;

/** Composer of the container for the visual controls to choose the options.
 *  It just lays them out. */

@Component
public class ControlsPaneComposer
{
  private final SourceSelectorComposer sourceSelectorComposer;
  private final WindowControlPanelComposer windowControlPanelComposer;
  private final SaveFilePanelComposer saveFilePanelComposer;
  private final FileImageSaver fileImageSaver;
  private final LanguagePanelComposer languagePanelComposer;

//==============================================================================

  public ControlsPaneComposer(SourceSelectorComposer sourceSelectorComposer,
                              WindowControlPanelComposer windowControlPanelComposer,
                              SaveFilePanelComposer saveFilePanelComposer,
                              FileImageSaver imageSaver,
                              LanguagePanelComposer languagePanelComposer)
  {
    this.sourceSelectorComposer     = sourceSelectorComposer;
    this.windowControlPanelComposer = windowControlPanelComposer;
    this.saveFilePanelComposer      = saveFilePanelComposer;
    this.fileImageSaver             = imageSaver;
    this.languagePanelComposer      = languagePanelComposer;
  }

//==============================================================================

  /** Returns a new panel containing the controls. */

  public JComponent createComponent()
  {
    JPanel panel = new JPanel();

    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    panel.add(sourceSelectorComposer.getComponent());
    panel.add(windowControlPanelComposer.createComponent());
    panel.add(saveFilePanelComposer.getComponent(fileImageSaver));
    panel.add(languagePanelComposer.getComponent());

    return panel;
  }

//------------------------------------------------------------------------------

}
