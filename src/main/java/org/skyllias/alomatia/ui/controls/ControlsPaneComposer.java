
package org.skyllias.alomatia.ui.controls;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.skyllias.alomatia.ui.LanguagePanelComposer;
import org.skyllias.alomatia.ui.SourceSelectorComposer;
import org.skyllias.alomatia.ui.WindowControlPanelComposer;
import org.skyllias.alomatia.ui.save.FileImageSaver;
import org.skyllias.alomatia.ui.save.SaveFilePanelComposer;
import org.springframework.stereotype.Component;

/** Composer of the container for the visual controls to choose the options.
 *  It includes a source selector, a filter selector and a display zoom selector. */

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

  /** Returns a new panel containing the controls.
   *  If a new window is opened, it is done after all the other processing in
   *  the current thread. Otherwise, the display frame would appear before this
   *  one and separated from the subsequent windows in the task bar. */

  public JComponent createComponent()
  {
    JPanel panel = new JPanel();

    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    panel.add(sourceSelectorComposer.getComponent());
    panel.add(windowControlPanelComposer.createComponent());
    panel.add(saveFilePanelComposer.getComponent(fileImageSaver));
    panel.add(languagePanelComposer.getComponent());

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run() {windowControlPanelComposer.openNewWindowIfRequired();}
    });

    return panel;
  }

//------------------------------------------------------------------------------

}
