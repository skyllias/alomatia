
package org.skyllias.alomatia.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.skyllias.alomatia.display.ResizableDisplay;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.DisplayOptionsPreferences;
import org.skyllias.alomatia.ui.filter.FilterSelector;
import org.springframework.stereotype.Component;

/** Composer of non-modal dialog with the display options for a given {@link DisplayFrameController}.
 *  <p>
 *  By default a new instance is immediately shown when {@link #getDialog()} is
 *  called, but it can be configured to hide by using preferences.
 *  <p>
 *  The dialog can be hidden both with the close button and the ESC key. */

@Component
public class DisplayOptionsDialogComposer
{
  private static final String TITLE                  = "display.options.title";
  private static final String OPEN_IMMEDIATELY_LABEL = "display.options.show";
  protected static final String CLOSE_LABEL          = "display.options.close";

  private static final int SCROLL_UNIT = 16;

  private static final Collection<JCheckBox> allShowCheckBoxes = new HashSet<>();     // since there is no checkbox model, to keep all the checboxes with the same value they have to be collected. TODO remove sometime after the dialog disposal

  private final LabelLocalizer labelLocalizer;
  private final ZoomSelectorComposer zoomSelectorComposer;

  private final DisplayOptionsPreferences displayOptionsPreferences;

//==============================================================================

  public DisplayOptionsDialogComposer(LabelLocalizer localizer,
                                      ZoomSelectorComposer zoomComposer,
                                      DisplayOptionsPreferences displayPreferences)
  {
    labelLocalizer            = localizer;
    zoomSelectorComposer      = zoomComposer;
    displayOptionsPreferences = displayPreferences;
  }

//==============================================================================

  /** The dialog is not modal because doing it so blocks the opening window and
   *  any subsequent that may be created afterwards, and offers no special
   *  advantage in this case. */

  public JDialog getDialog(Frame ownerFrame, ResizableDisplay resizableDisplay,
                           FilterSelector filterSelector)
  {
    boolean showImmediately = displayOptionsPreferences.isDialogShownImmediately();

    JDialog dialog = new JDialog(ownerFrame, labelLocalizer.getString(TITLE), false);

    JPanel selectorsPanel = getSelectorsPanel(resizableDisplay, filterSelector);
    JPanel optionsPanel   = getOptionsPanel(selectorsPanel, showImmediately);
    JPanel buttonsPanel   = getButtonsPanel(dialog);

    dialog.getContentPane().add(optionsPanel, BorderLayout.CENTER);
    dialog.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

    dialog.pack();
    dialog.setLocationRelativeTo(ownerFrame);
    dialog.setResizable(false);
    dialog.setVisible(showImmediately);                                         // do this after setLocationRelativeTo, or the dialog will be left in the upper left corner

    return dialog;
  }

//------------------------------------------------------------------------------

  /* Returns a panel containing the filter selector and a zoom selector. */

  private JPanel getSelectorsPanel(ResizableDisplay resizableDisplay,
                                   FilterSelector filterSelector)
  {
    JComponent zoomPanel = zoomSelectorComposer.getComponent(resizableDisplay);

    JPanel selectorsPanel     = new JPanel();
    JComponent filterPanel    = filterSelector.getComponent();
    JScrollPane filtersScroll = new JScrollPane(filterPanel);
    filtersScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    filtersScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    filtersScroll.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT);

    Dimension scrollPaneSize = new Dimension(filtersScroll.getPreferredSize().width,
                                             zoomPanel.getPreferredSize().height);
    filtersScroll.setPreferredSize(scrollPaneSize);

    selectorsPanel.add(filtersScroll);
    selectorsPanel.add(zoomPanel);

    return selectorsPanel;
  }

//------------------------------------------------------------------------------

  /* Returns a panel containing selectorsPanel and a show checkbox. */

  private JPanel getOptionsPanel(JPanel selectorsPanel, boolean showImmediately)
  {
    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new BorderLayout());
    optionsPanel.add(selectorsPanel,  BorderLayout.CENTER);
    optionsPanel.add(getNewShowCheckbox(showImmediately), BorderLayout.SOUTH);

    return optionsPanel;
  }

//------------------------------------------------------------------------------

  /* Returns a panel containing a close button. */

  private JPanel getButtonsPanel(JDialog dialog)
  {
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    JButton closeButton = new JButton(labelLocalizer.getString(CLOSE_LABEL));
    closeButton.setName(CLOSE_LABEL);
    closeButton.addActionListener(new CloseListener(dialog));
    registerEscKeyAction(dialog);
    buttonsPanel.add(closeButton);
    return buttonsPanel;
  }

//------------------------------------------------------------------------------

  /* Returns a new checkbox initialized with showImmediately and with a listener
   * that will update all the existing checkboxes when its value changes. */

  private JCheckBox getNewShowCheckbox(boolean showImmediately)
  {
    JCheckBox checkBox = new JCheckBox(labelLocalizer.getString(OPEN_IMMEDIATELY_LABEL),
                                       showImmediately);
    checkBox.addChangeListener(new CheckShowBoxChangeListener());
    allShowCheckBoxes.add(checkBox);
    return checkBox;
  }

//------------------------------------------------------------------------------

  /* Registers the ESC key so that dialog is hidden when closed.
   * It cannot be achieved by adding a key listener because the focus is in the
   * inner components, so the frame's input map (due to the fact that JDialogs
   * do not extend JComponent, it cannot be on its own InputMap) is modified. */

  private void registerEscKeyAction(JDialog dialog)
  {
    final String ACTION_NAME = getClass().getName() + "ESC-ACTION";

    KeyStroke escStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    JRootPane rootPane  = dialog.getRootPane();
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escStroke, ACTION_NAME);
    rootPane.getActionMap().put(ACTION_NAME, new CloseListener(dialog));
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /* Listener notified whenever a show checkbox changes its value, updating
   * all the other checkboxes. The new value is stored in the preferences. */

  private class CheckShowBoxChangeListener implements ChangeListener
  {
    @Override
    public void stateChanged(ChangeEvent event)
    {
      JCheckBox sourceCheckbox = (JCheckBox) event.getSource();
      boolean showImmediately  = sourceCheckbox.isSelected();
      for (JCheckBox currentCheckbox : allShowCheckBoxes)
      {
        if (currentCheckbox.isSelected() != showImmediately) currentCheckbox.setSelected(showImmediately);  // probably the verification is redundant and there will not be a cascade of change events, but it does not hurt just in case
      }

      displayOptionsPreferences.setDialogShownImmediately(showImmediately);
    }
  }

//******************************************************************************

  /* Listener notified whenever the close button or the ESC key are pressed.
   * The dialog is hidden. */

  @SuppressWarnings("serial")
  private class CloseListener extends AbstractAction
  {
    private JDialog dialog;

    public CloseListener(JDialog dialog)
    {
      this.dialog = dialog;
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
      hideDialog();
    }

    private void hideDialog() {dialog.setVisible(false);}
  }

//******************************************************************************

}
