
package org.skyllias.alomatia.ui;

import java.awt.dnd.DropTargetListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.skyllias.alomatia.display.Repeater;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.source.SourceCatalogue;
import org.skyllias.alomatia.ui.frame.FramePolicy;

/** Composer of the container for the visual controls to choose the options.
 *  It includes a source selector, a filter selector and a display zoom selector. */

public class ControlsPaneComposer
{
  private LabelLocalizer labelLocalizer;
  private SourceCatalogue sourceCatalogue;
  private Repeater repeater;
  private DropTargetListener dropListener;
  private DisplayFrameManager frameManager;
  private FramePolicy framePolicy;
  private FileImageSaver fileImageSaver;

//==============================================================================

  public ControlsPaneComposer(LabelLocalizer localizer, SourceCatalogue catalogue,
                              Repeater displayRepeater, DropTargetListener dropTargetListener,
                              DisplayFrameManager displayFrameManager, FramePolicy policy,
                              FileImageSaver imageSaver)
  {
    labelLocalizer  = localizer;
    sourceCatalogue = catalogue;
    repeater        = displayRepeater;
    dropListener    = dropTargetListener;
    frameManager    = displayFrameManager;
    framePolicy     = policy;
    fileImageSaver  = imageSaver;
  }

//==============================================================================

  /** Returns a new panel containing the controls.
   *  If a new window is opened, it is done after all the other processing in the
   *  current thread. Otherwise, the display frame appears before this one and
   *  separated from the subsequent windows in the task bar. */

  public JComponent getComponent()
  {
    JPanel panel = new JPanel();

    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    final WindowControlPanelComposer windowControlComposer = new WindowControlPanelComposer(labelLocalizer, repeater,
                                                                                            dropListener,
                                                                                            frameManager, framePolicy);

    SourceRadioSelector<JRadioButton> radioSelector = new SourceRadioSelector<>(JRadioButton.class, labelLocalizer);
    panel.add(new SourceSelectorComposer(labelLocalizer, sourceCatalogue,
                                         new CaptureFrameComposer(labelLocalizer),
                                         radioSelector).getComponent());
    panel.add(windowControlComposer.getComponent());
    panel.add(new SaveFilePanelComposer(labelLocalizer, fileImageSaver).getComponent());
    panel.add(new LanguagePanelComposer(labelLocalizer).getComponent());

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run() {windowControlComposer.openNewWindowIfRequired();}
    });

    return panel;
  }
}
