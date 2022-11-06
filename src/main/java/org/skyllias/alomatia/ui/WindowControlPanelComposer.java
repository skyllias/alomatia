
package org.skyllias.alomatia.ui;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.MessageFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.skyllias.alomatia.display.Repeater;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.FramePolicyPreferences;
import org.skyllias.alomatia.preferences.WindowControlPreferences;
import org.skyllias.alomatia.ui.DisplayFrameController.DisplayFrameCloseListener;
import org.skyllias.alomatia.ui.DisplayFrameManager.DisplayAmountChangeListener;
import org.skyllias.alomatia.ui.component.BorderedLabel;

/** Composer of panels to manage display frames: Create, count and rearrange them.
 *  This takes care of the UI, while a {@link DisplayFrameManager} is in charge
 *  of the logic.
 *  <p>
 *  Some configurations are stored as user preferences.
 *  <p>
 *  A global key listener is added to open windows when Ctrl + N is pressed. */

@org.springframework.stereotype.Component
public class WindowControlPanelComposer implements DisplayFrameCloseListener
{
  private static final String TITLE_LABEL          = "frame.control.title";
  private static final String AMOUNT_LABEL         = "frame.control.amount";
  private static final String ADD_LABEL            = "frame.control.add";
  private static final String AUTOOPEN_LABEL       = "frame.control.autoopen";
  private static final String INTERNALFRAMES_LABEL = "frame.control.internalframes";
  private static final String LINES_LABEL          = "frame.control.arrange.info";
  private static final String COLUMNS_LABEL        = "frame.control.arrange.columns";
  private static final String ROWS_LABEL           = "frame.control.arrange.rows";
  private static final String REARRANGE_LABEL      = "frame.control.arrange.rearrange";
  private static final String AUTOAPPLY_LABEL      = "frame.control.filters.autoapply";
  private static final String REAPPLY_LABEL        = "frame.control.filters.reapply";

  protected static final String AMOUNT_LABEL_NAME            = "label.amount";         // name for the components
  protected static final String ADD_BUTTON_NAME              = "button.add";
  protected static final String AUTOOPEN_CHECKBOX_NAME       = "checkbox.autoopen";
  protected static final String INTERNALFRAMES_CHECKBOX_NAME = "checkbox.internalframes";
  protected static final String LINES_SPINNER_NAME           = "spinner.lines";
  protected static final String COMBO_HORIZONTAL_NAME        = "combobox.horizontal";
  protected static final String ARRANGE_BUTTON_NAME          = "button.arrange";
  protected static final String AUTOAPPLY_FILTER_NAME        = "checkbox.autoapply";
  protected static final String REFILTER_BUTTON_NAME         = "button.refilter";

  private DisplayFrameManager manager;
  private final FramePolicyPreferences framePolicyPreferences;

  private final BarePanelComposer bareControlPanelComposer;

  private Repeater repeaterDisplay;

  private LabelLocalizer labelLocalizer;

  private DropTargetListener dropListener;

  private boolean applySequentialFilters = false;

  private final WindowControlPreferences windowControlPreferences;

//==============================================================================

  /** Does NOT open a new window automatically. {@link #openNewWindowIfRequired()}
   *  must be explicitly called. */

  public WindowControlPanelComposer(LabelLocalizer localizer, Repeater displayRepeater,
                                    DropTargetListener dropTargetListener,
                                    DisplayFrameManager frameManager, FramePolicyPreferences policy,
                                    BarePanelComposer panelComposer,
                                    WindowControlPreferences preferences)
  {
    labelLocalizer           = localizer;
    repeaterDisplay          = displayRepeater;
    manager                  = frameManager;
    dropListener             = dropTargetListener;
    framePolicyPreferences   = policy;
    bareControlPanelComposer = panelComposer;
    windowControlPreferences = preferences;

    addNewDisplayKeyListener();
  }

//==============================================================================

  /** Returns a new panel with the required controls. */

  public JComponent createComponent()
  {
    JPanel windowControlPanel = bareControlPanelComposer.getPanel(labelLocalizer.getString(TITLE_LABEL));

    addAdditionComponents(windowControlPanel);
    addRearrangeComponents(windowControlPanel);
    addRefilterComponents(windowControlPanel);

    return windowControlPanel;
  }

//------------------------------------------------------------------------------

  /** Removes the panel in the closed frame from those notified by the source. */

  @Override
  public void onDisplayFrameClosed(DisplayFrameController displayFrame)
  {
    DisplayPanelController displayPanel = displayFrame.getDisplayPanel();
    repeaterDisplay.removeReceiver(displayPanel);
  }

//------------------------------------------------------------------------------

  /** Opens a new display frame but only if the preferences contain a value
   *  requesting it. */

  public void openNewWindowIfRequired()
  {
    if (isAutoOpenOn()) createNewDisplayFrame();
  }

//------------------------------------------------------------------------------

  /* Adds a global listener that invokes createNewDisplayFrame() when Ctrl + N is pressed. */

  private void addNewDisplayKeyListener()
  {
    KeyboardFocusManager keyboardManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    keyboardManager.addKeyEventDispatcher(new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED)
        {
          if (e.getKeyCode() == KeyEvent.VK_N && EventUtils.isControlDown(e)) createNewDisplayFrame();
        }
        return false;                                                           // allow the event to be redispatched
      }
    });
  }

//------------------------------------------------------------------------------

  /* Instantiates a new frame through the manager and adds its panel to the
   * repeater, so that it receives images from sources.
   * This composer is added as close listener in order to remove the panels from
   * the repeater when their window is closed.
   * A drop target is created so that images can be dropped to the display frame
   * too if there is a drop listener. */

  private void createNewDisplayFrame()
  {
    DisplayFrameController frame        = manager.createDisplayFrame(applySequentialFilters);
    DisplayPanelController displayPanel = frame.getDisplayPanel();

    repeaterDisplay.addReceiver(displayPanel);
    frame.addListener(this);
    addDropTarget(frame.getOwnerFrame());
  }

//------------------------------------------------------------------------------

  /* Returns true if the preferences contain a value with which the user requests
   * that a new display frame is automatically opened on startup. */

  private boolean isAutoOpenOn() {return windowControlPreferences.isAutoOpenWindowOnStartup();}

//------------------------------------------------------------------------------
//                        PANEL & COMPONENTS
//------------------------------------------------------------------------------

  /* Sets up components used to display the amount of windows and create new ones,
   * and adds them to panel. */

  private void addAdditionComponents(JPanel panel)
  {
    JLabel amountLabel = getAmountOfFramesLabel();
    JButton addButton  = getAddFrameButton();
    JCheckBox checkBox = getAutoOpenCheckbox();

    JPanel firstRow = new JPanel();
    firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));
    firstRow.add(checkBox);
    firstRow.add(Box.createHorizontalGlue());
    firstRow.add(amountLabel);
    firstRow.add(addButton);
    panel.add(firstRow);
  }

//------------------------------------------------------------------------------

  /* Sets up components used to rearrange the display windows and adds them to panel.
   * The preferences are read and updated conveniently. */

  private void addRearrangeComponents(JPanel panel)
  {
    JCheckBox framesCheckBox = getFramePolicyCheckbox();

    JLabel infoLabel = new BorderedLabel(labelLocalizer.getString(LINES_LABEL));

    JSpinner linesSpinner                 = getLinesSpinner();
    JComboBox<Boolean> horizontalCombobox = getColumnsOrRowsComboBox();
    JButton arrangeButton                 = getArrangeButton(linesSpinner, horizontalCombobox);

    JPanel secondRow = new JPanel();
    secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));
    secondRow.add(framesCheckBox);
    secondRow.add(Box.createHorizontalGlue());
    secondRow.add(infoLabel);
    secondRow.add(linesSpinner);
    secondRow.add(horizontalCombobox);
    secondRow.add(arrangeButton);
    panel.add(secondRow);
  }

//------------------------------------------------------------------------------

  /* Sets up components used to automatically apply filters to display frames
   * and adds then to panel.
   * The preferences are read and updated conveniently. */

  private void addRefilterComponents(JPanel panel)
  {
    JCheckBox checkBox   = getSequentialFiltersCheckbox();
    JButton filterButton = getApplyFiltersButton();

    JPanel thirdRow = new JPanel();
    thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));
    thirdRow.add(checkBox);
    thirdRow.add(Box.createHorizontalGlue());
    thirdRow.add(filterButton);
    panel.add(thirdRow);
  }

//------------------------------------------------------------------------------

  private JLabel getAmountOfFramesLabel()
  {
    JLabel amountLabel = new BorderedLabel(getAmountOfWindowsText(manager));
    amountLabel.setName(AMOUNT_LABEL_NAME);
    AmountOfDisplaysLabelUpdater labelUpdater = new AmountOfDisplaysLabelUpdater(amountLabel);
    manager.addAmountChangeListener(labelUpdater);

    return amountLabel;
  }

//------------------------------------------------------------------------------

  private JButton getAddFrameButton()
  {
    JButton addButton = new JButton(labelLocalizer.getString(ADD_LABEL));
    addButton.setName(ADD_BUTTON_NAME);
    addButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {createNewDisplayFrame();}
    });

    return addButton;
  }

//------------------------------------------------------------------------------

  private JCheckBox getAutoOpenCheckbox()
  {
    JCheckBox checkBox = new JCheckBox(labelLocalizer.getString(AUTOOPEN_LABEL),
                                       isAutoOpenOn());
    checkBox.addChangeListener(new AutoopenCheckBoxSelectionChangeListener());
    checkBox.setName(AUTOOPEN_CHECKBOX_NAME);

    return checkBox;
  }

//------------------------------------------------------------------------------

  private JCheckBox getFramePolicyCheckbox()
  {
    final JCheckBox framesCheckBox = new JCheckBox(labelLocalizer.getString(INTERNALFRAMES_LABEL),
                                                   framePolicyPreferences.isUsingInternalFrames());
    framesCheckBox.addChangeListener(new ChangeListener()
    {
      @Override
      public void stateChanged(ChangeEvent e)
      {
        boolean useInternalFramesNextTime = framesCheckBox.isSelected();
        framePolicyPreferences.setUsingInternalFramesNextTime(useInternalFramesNextTime);
      }
    });
    framesCheckBox.setName(INTERNALFRAMES_CHECKBOX_NAME);

    return framesCheckBox;
  }

//------------------------------------------------------------------------------

  private JSpinner getLinesSpinner()
  {
    final int STEP_LINES = 1;
    final int MIN_LINES  = 1;
    final int MAX_LINES  = 20;

    int initialLinesValue = windowControlPreferences.getAmountOfLinesToArrangeIn();
    if (initialLinesValue < MIN_LINES) initialLinesValue = MIN_LINES;
    if (initialLinesValue > MAX_LINES) initialLinesValue = MAX_LINES;
    JSpinner linesSpinner = new JSpinner(new SpinnerNumberModel(initialLinesValue, MIN_LINES,
                                                                MAX_LINES, STEP_LINES));
    linesSpinner.setName(LINES_SPINNER_NAME);
    linesSpinner.setMaximumSize(linesSpinner.getPreferredSize());               // prevent it from stretching

    return linesSpinner;
  }

//------------------------------------------------------------------------------

  private JComboBox<Boolean> getColumnsOrRowsComboBox()
  {
    boolean initialHorizontal = windowControlPreferences.isHorizontallyArranged();

    JComboBox<Boolean> horizontalCombobox = new JComboBox<>(new Boolean[] {true, false});
    horizontalCombobox.setName(COMBO_HORIZONTAL_NAME);
    horizontalCombobox.setSelectedItem(initialHorizontal);
    horizontalCombobox.setRenderer(new HorizontalCellRenderer());
    horizontalCombobox.setMaximumSize(horizontalCombobox.getPreferredSize());   // prevent it from stretching

    return horizontalCombobox;
  }

//------------------------------------------------------------------------------

  private JButton getArrangeButton(final JSpinner linesSpinner, final JComboBox<Boolean> horizontalCombobox)
  {
    JButton arrangeButton = new JButton(labelLocalizer.getString(REARRANGE_LABEL));
    arrangeButton.setName(ARRANGE_BUTTON_NAME);
    arrangeButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        int amountOfLines    = (int) linesSpinner.getValue();
        boolean horizontally = (boolean) horizontalCombobox.getSelectedItem();
        manager.rearrangeWindows(amountOfLines, horizontally);

        windowControlPreferences.setAmountOfLinesToArrangeIn(amountOfLines);
        windowControlPreferences.setHorizontallyArranged(horizontally);
      }
    });

    return arrangeButton;
  }

//------------------------------------------------------------------------------

  private JCheckBox getSequentialFiltersCheckbox()
  {
    applySequentialFilters = windowControlPreferences.isSequentialFilterApplied();

    final JCheckBox checkBox = new JCheckBox(labelLocalizer.getString(AUTOAPPLY_LABEL),
                                             applySequentialFilters);
    checkBox.setName(AUTOAPPLY_FILTER_NAME);
    checkBox.addChangeListener(new ChangeListener()
    {
      @Override
      public void stateChanged(ChangeEvent e)
      {
        applySequentialFilters = checkBox.isSelected();
        windowControlPreferences.setSequentialFilterApplied(applySequentialFilters);
      }
    });

    return checkBox;
  }

//------------------------------------------------------------------------------

  private JButton getApplyFiltersButton()
  {
    JButton filterButton = new JButton(labelLocalizer.getString(REAPPLY_LABEL));
    filterButton.setName(REFILTER_BUTTON_NAME);
    filterButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {manager.applySequentialFilters();}
    });

    return filterButton;
  }

//------------------------------------------------------------------------------

  private String getAmountOfWindowsText(DisplayFrameManager frameManager)
  {
    String messagePattern = labelLocalizer.getString(AMOUNT_LABEL);
    int amountOfWindows   = frameManager.getAmountOfOpenDisplayFrames();

    MessageFormat messageFormat = new MessageFormat(messagePattern);
    return messageFormat.format(new Object[] {amountOfWindows});
  }

//------------------------------------------------------------------------------

  /* If there is a drop listener, the passed component is set up so that its
   * drop events are handled by it. */

  private void addDropTarget(Component dropComponent)
  {
    if (dropListener != null) new DropTarget(dropComponent, dropListener);
  }

//------------------------------------------------------------------------------

//******************************************************************************

  private class AmountOfDisplaysLabelUpdater implements DisplayAmountChangeListener
  {
    private final JLabel labelToUpdate;

    public AmountOfDisplaysLabelUpdater(JLabel labelToUpdate)
    {
      this.labelToUpdate = labelToUpdate;
    }

    @Override
    public void onAmountChanged(DisplayFrameManager manager)
    {
      labelToUpdate.setText(getAmountOfWindowsText(manager));
    }
  }

//******************************************************************************

  /* Saver of the new autoopen value in the preferences. */

  private class AutoopenCheckBoxSelectionChangeListener implements ChangeListener
  {
    @Override
    public void stateChanged(ChangeEvent event)
    {
      JCheckBox sourceCheckbox = (JCheckBox) event.getSource();
      boolean autoOpenNextTime = sourceCheckbox.isSelected();
      windowControlPreferences.setAutoOpenWindowOnStartup(autoOpenNextTime);
    }
  }

//******************************************************************************

  /* Renderer of the options to choose the direction of window rearrangement. */

  private class HorizontalCellRenderer implements ListCellRenderer<Boolean>
  {
    private BasicComboBoxRenderer originalRenderer = new BasicComboBoxRenderer();

    @Override
    public Component getListCellRendererComponent(JList<? extends Boolean> list, Boolean value,
                                                  int index, boolean isSelected, boolean cellHasFocus)
    {
      String labelKey     = value.booleanValue()? COLUMNS_LABEL: ROWS_LABEL;
      String displayValue = labelLocalizer.getString(labelKey);
      return originalRenderer.getListCellRendererComponent(list, displayValue, index,
                                                           isSelected, cellHasFocus);
    }
  }

//******************************************************************************

}
