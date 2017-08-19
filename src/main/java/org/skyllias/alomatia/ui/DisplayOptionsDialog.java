
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.prefs.*;

import javax.swing.*;
import javax.swing.event.*;

import org.skyllias.alomatia.filter.*;
import org.skyllias.alomatia.i18n.*;

/** Non-modal dialog with the display options for a given {@link DisplayFrame}.
 *  <p>
 *  By default it is immediately shown, but it can be configured to hide by
 *  using preferences.
 *  <p>
 *  The dialog can be hidden both with the close button and the ESC key. */

@SuppressWarnings("serial")
public class DisplayOptionsDialog extends JDialog
{
  private static final String TITLE                  = "display.options.title";
  private static final String OPEN_IMMEDIATELY_LABEL = "display.options.show";
  private static final String CLOSE_LABEL            = "display.options.close";

  private static final boolean SHOW_IMMEDIATELY_DEFAULT = true;

  private static final String PREFKEY_SHOW = "showDialogImmediately";

  private static final int SCROLL_UNIT = 16;

  private static boolean showImmediately = SHOW_IMMEDIATELY_DEFAULT;            // static value to be synchronized between all the instances everytime the checkbox value is changed

  private static Collection<JCheckBox> allShowCheckBoxes = new HashSet<>();     // since there is no checkbox model, to keep all the checboxes with the same value they have to be collected. They should be removed sometime after the dialog disposal

  private LabelLocalizer labelLocalizer;

//==============================================================================

  static
  {
    showImmediately = getPreferences().getBoolean(PREFKEY_SHOW, SHOW_IMMEDIATELY_DEFAULT);
  }

//==============================================================================

  public DisplayOptionsDialog(LabelLocalizer localizer, DisplayFrame ownerFrame,
                              FilterFactory filterFactory)
  {
    super(ownerFrame, localizer.getString(TITLE), false);                       // making it modal blocks the opening window, and any subsequent that may be created afterwards, and offers no special advantage in this case

    labelLocalizer = localizer;

    FilterSelector filterSelector = new FilterSelector(labelLocalizer, ownerFrame, filterFactory);
    ZoomSelector zoomSelector     = new ZoomSelector(labelLocalizer, ownerFrame.getDisplayPanel());

    JPanel selectorsPanel     = new JPanel();
    JScrollPane filtersScroll = new JScrollPane(filterSelector);
    filtersScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    filtersScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    filtersScroll.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT);         // TODO the scroll must be inside the filter selector, not outside
    filtersScroll.setPreferredSize(zoomSelector.getPreferredSize());
    selectorsPanel.add(filtersScroll);
    selectorsPanel.add(zoomSelector);

    JPanel optionsPanel = new JPanel();
    optionsPanel.setLayout(new BorderLayout());
    optionsPanel.add(selectorsPanel,  BorderLayout.CENTER);
    optionsPanel.add(getNewShowCheckbox(), BorderLayout.SOUTH);
    getContentPane().add(optionsPanel, BorderLayout.CENTER);

    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    JButton closeButton = new JButton(labelLocalizer.getString(CLOSE_LABEL));
    closeButton.addActionListener(new CloseListener());
    registerEscKeyAction();
    buttonsPanel.add(closeButton);
    getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

    setVisible(showImmediately);
    pack();
    setLocationRelativeTo(ownerFrame);
    setResizable(false);
  }

//==============================================================================

  /* Returns the preferences where the selection is stored. */

  private static Preferences getPreferences() {return Preferences.userNodeForPackage(DisplayOptionsDialog.class);}

//------------------------------------------------------------------------------

  /* Returns a new checkbox initialized with showImmediately and with a listener
   * that will update all the existing checkboxes when its value changes. */

  private JCheckBox getNewShowCheckbox()
  {
    JCheckBox checkBox = new JCheckBox(labelLocalizer.getString(OPEN_IMMEDIATELY_LABEL),
                                       showImmediately);
    checkBox.addChangeListener(new CheckShowBoxChangeListener());
    allShowCheckBoxes.add(checkBox);
    return checkBox;
  }

//------------------------------------------------------------------------------

  /* Registers the ESC key so that the dialog is hidden when closed.
   * It cannot be achieved by adding a key listener because the focus is in the
   * inner components, so the frame's input map (due to the fact that JDialogs
   * do not extend JComponent, it cannot be on its own InputMap) is modified. */

  private void registerEscKeyAction()
  {
    final String ACTION_NAME = getClass().getName() + "ESC-ACTION";

    KeyStroke escStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
    JRootPane rootPane  = getRootPane();
    rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escStroke, ACTION_NAME);
    rootPane.getActionMap().put(ACTION_NAME, new CloseListener());
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /* Listener notified whenever a show checkbox changes its value, updating
   * showImmediately and all the other checkboxes. The new value is stored in
   * the preferences. */

  private static class CheckShowBoxChangeListener implements ChangeListener
  {
    @Override
    public void stateChanged(ChangeEvent event)
    {
      JCheckBox sourceCheckbox = (JCheckBox) event.getSource();
      showImmediately          = sourceCheckbox.isSelected();
      for (JCheckBox currentCheckbox : allShowCheckBoxes)
      {
        if (currentCheckbox.isSelected() != showImmediately) currentCheckbox.setSelected(showImmediately);  // probably the verification is redundant and there will not be a cascade of change events, but it does not hurt just in case
      }

      getPreferences().putBoolean(PREFKEY_SHOW, showImmediately);
    }
  }

//******************************************************************************

  /* Listener notified whenever the close button or the ESC key are pressed.
   * The dialog is hidden. */

  private class CloseListener extends AbstractAction
  {
    @Override
    public void actionPerformed(ActionEvent event)
    {
      hideDialog();
    }

    private void hideDialog() {setVisible(false);}
  }

//******************************************************************************

}
