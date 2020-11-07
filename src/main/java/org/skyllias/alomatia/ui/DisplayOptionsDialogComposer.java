
package org.skyllias.alomatia.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.prefs.Preferences;

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

import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.ui.filter.FilterSelectorComposer;

/** Composer of non-modal dialog with the display options for a given {@link DisplayFrameController}.
 *  <p>
 *  By default a new instance is immediately shown when {@link #getDialog()} is
 *  called, but it can be configured to hide by using preferences.
 *  <p>
 *  The dialog can be hidden both with the close button and the ESC key. */

public class DisplayOptionsDialogComposer
{
  private static final String TITLE                  = "display.options.title";
  private static final String OPEN_IMMEDIATELY_LABEL = "display.options.show";
  private static final String CLOSE_LABEL            = "display.options.close";

  private static final boolean SHOW_IMMEDIATELY_DEFAULT = true;

  private static final String PREFKEY_SHOW = "showDialogImmediately";

  private static final int SCROLL_UNIT = 16;

  private static Collection<JCheckBox> allShowCheckBoxes = new HashSet<>();     // since there is no checkbox model, to keep all the checboxes with the same value they have to be collected. TODO remove sometime after the dialog disposal

  private DisplayFrameController ownerDisplayFrame;
  private LabelLocalizer labelLocalizer;
  private FilterSelectorComposer filterSelector;

  private Preferences preferences = Preferences.userNodeForPackage(getClass());

//==============================================================================

  public DisplayOptionsDialogComposer(LabelLocalizer localizer, DisplayFrameController ownerFrame,
                                      FilterSelectorComposer selector)
  {
    labelLocalizer    = localizer;
    ownerDisplayFrame = ownerFrame;
    filterSelector    = selector;
  }

//==============================================================================

  /** The dialog is not modal because doing it so blocks the opening window and
   *  any subsequent that may be created afterwards, and offers no special
   *  advantage in this case. */

  public JDialog getDialog()
  {
    boolean showImmediately = preferences.getBoolean(PREFKEY_SHOW, SHOW_IMMEDIATELY_DEFAULT);

    JDialog dialog = new JDialog(ownerDisplayFrame.getOwnerFrame(), labelLocalizer.getString(TITLE), false);

    JPanel selectorsPanel = getSelectorsPanel();
    JPanel optionsPanel   = getOptionsPanel(selectorsPanel, showImmediately);
    JPanel buttonsPanel   = getButtonsPanel(dialog);

    dialog.getContentPane().add(optionsPanel, BorderLayout.CENTER);
    dialog.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

    dialog.pack();
    dialog.setLocationRelativeTo(ownerDisplayFrame.getOwnerFrame());
    dialog.setResizable(false);
    dialog.setVisible(showImmediately);                                         // do this after setLocationRelativeTo, or the dialog will be left in the upper left corner

    return dialog;
  }

//------------------------------------------------------------------------------

  /* Returns a panel containing the filter selector and a zoom selector. */

  private JPanel getSelectorsPanel()
  {
    ZoomSelectorComposer zoomComposer = new ZoomSelectorComposer(labelLocalizer, ownerDisplayFrame.getDisplayPanel());
    JComponent zoomPanel              = zoomComposer.getComponent();

    JPanel selectorsPanel     = new JPanel();
    JScrollPane filtersScroll = new JScrollPane(filterSelector.getComponent());
    filtersScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    filtersScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    filtersScroll.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT);
    filtersScroll.setPreferredSize(zoomPanel.getPreferredSize());
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

      preferences.putBoolean(PREFKEY_SHOW, showImmediately);
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
