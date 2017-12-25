
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.text.*;
import java.util.prefs.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.plaf.basic.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.ui.DisplayFrame.DisplayFrameCloseListener;
import org.skyllias.alomatia.ui.DisplayFrameManager.*;
import org.skyllias.alomatia.ui.frame.*;

/** Panel to manage display frames: Create, count and rearrange them.
 *  This takes care of the UI, while a {@link DisplayFrameManager} is in charge
 *  of the logic.
 *  <p>
 *  Some configurations are stored as user preferences.
 *  <p>
 *  This class adds a global key listener to open windows when Ctrl + N is pressed. */

@SuppressWarnings("serial")
public class WindowControlPanel extends BasicControlPanel
                                implements DisplayFrameCloseListener, DisplayAmountChangeListener
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

  private static final String PREFKEY_HORIZONTAL  = "arrangeHorizontally";
  private static final String PREFKEY_LINES       = "amountOfLinesToRearrangeIn";
  private static final String PREFKEY_APPLYFILTER = "applySequentialFilterToNewWindow";
  private static final String PREFKEY_AUTOOPEN    = "automaticallyOpenNewWindowOnStartup";

  private DisplayFrameManager manager;
  private FramePolicy framePolicy;

  private Repeater repeaterDisplay;

  private DropTargetListener dropListener;                                      // may be null. Otherwise, used to dispatch drops on the display frames

  private JLabel amountLabel;                                                   // label displaying the amount of open windows

  private boolean applySequentialFilters = false;

  private Preferences preferences;

//==============================================================================

  /** Automatically opens a new display window after creation. */

  public WindowControlPanel(LabelLocalizer localizer, Repeater displayRepeater,
                            DropTargetListener dropTargetListener,
                            DisplayFrameManager frameManager, FramePolicy policy)
  {
    this(getDefaultPreferences(), localizer, displayRepeater,
         dropTargetListener, frameManager, policy);
  }

//------------------------------------------------------------------------------

  /** Only meant for preferences injection in tests. */

  protected WindowControlPanel(Preferences prefs, LabelLocalizer localizer, Repeater displayRepeater,
                               DropTargetListener dropTargetListener, DisplayFrameManager frameManager,
                               FramePolicy policy)
  {
    super(localizer, TITLE_LABEL);

    preferences = prefs;

    repeaterDisplay = displayRepeater;
    manager         = frameManager;
    dropListener    = dropTargetListener;
    framePolicy     = policy;

    manager.addAmountChangeListener(this);

    addAdditionComponents();
    addRearrangeComponents();
    addRefilterComponents();
    addNewDisplayKeyListener();

    openNewWindow();
  }

//==============================================================================

  /** Removes the panel in the closed frame from those notified by the source. */

  @Override
  public void onDisplayFrameClosed(DisplayFrame displayFrame)
  {
    DisplayPanel displayPanel = displayFrame.getDisplayPanel();
    repeaterDisplay.removeReceiver(displayPanel);
  }

//------------------------------------------------------------------------------

  /** Updates the label displaying the amount of open display windows. */

  @Override
  public void onAmountChanged(DisplayFrameManager frameManager)
  {
    amountLabel.setText(getAmountOfWindowsText(frameManager));
  }

//------------------------------------------------------------------------------

  /** Returns true if new {@link DisplayFrame} instances get a filter automatically
   *  selected when open, or false otherwise. */

  public boolean isAutomaticallyApplyingFilters() {return applySequentialFilters;}

//------------------------------------------------------------------------------

  /* Returns true if the preferences contain a value with which the user requests
   * that a new display frame is automatically opened on startup. */

  private boolean isAutoOpenOn() {return getPreferences().getBoolean(PREFKEY_AUTOOPEN, false);}

//------------------------------------------------------------------------------

  /* Opens a new display frame but only if the preferences contain a value
   * requesting it.
   * If a new window is opened, it is done after all the other processing in the
   * current thread. Otherwise, the display frame appears before this one and
   * separated from the subsequent windows in the task bar. */

  private void openNewWindow()
  {
    if (isAutoOpenOn())
    {
      SwingUtilities.invokeLater(new Runnable()
      {
        @Override
        public void run() {getNewDisplayFrame();}
      });
    }
  }

//------------------------------------------------------------------------------

  /* Sets up the components used to display the amount of windows and create new ones.
   * To be called just once. */

  private void addAdditionComponents()
  {
    amountLabel = new BorderedLabel(getAmountOfWindowsText(manager));
    amountLabel.setName(AMOUNT_LABEL_NAME);

    JButton addButton = new JButton(getLabelLocalizer().getString(ADD_LABEL));
    addButton.setName(ADD_BUTTON_NAME);
    addButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {getNewDisplayFrame();}
    });

    JCheckBox checkBox = new JCheckBox(getLabelLocalizer().getString(AUTOOPEN_LABEL),
                                       isAutoOpenOn());
    checkBox.addChangeListener(new AutoopenCheckBoxAutoopenChangeListener());
    checkBox.setName(AUTOOPEN_CHECKBOX_NAME);

    JPanel firstRow = new JPanel();
    firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));
    firstRow.add(checkBox);
    firstRow.add(Box.createHorizontalGlue());
    firstRow.add(amountLabel);
    firstRow.add(addButton);
    add(firstRow);
  }

//------------------------------------------------------------------------------

  /* Adds a global listener that invokes getNewDisplayFrame() when Ctrl + N is pressed. */

  private void addNewDisplayKeyListener()
  {
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventDispatcher(new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED && (e.getModifiers() & KeyEvent.CTRL_MASK) != 0)
        {
          if (e.getKeyCode() == KeyEvent.VK_N) getNewDisplayFrame();
        }
        return false;                                                           // allow the event to be redispatched
      }
    });
  }

//------------------------------------------------------------------------------

  /* Sets up the components used to rearrange the display windows.
   * The preferences are read and updated conveniently.
   * To be called just once. */

  private void addRearrangeComponents()
  {
    final int STEP_LINES = 1;
    final int MIN_LINES  = 1;
    final int MAX_LINES  = 20;
    final int DEF_LINES  = MIN_LINES;

    final JCheckBox framesCheckBox = new JCheckBox(getLabelLocalizer().getString(INTERNALFRAMES_LABEL),
                                                   framePolicy.isUsingInternalFrames());
    framesCheckBox.addChangeListener(new ChangeListener()
    {
      @Override
      public void stateChanged(ChangeEvent e)
      {
        boolean useInternalFramesNextTime = framesCheckBox.isSelected();
        framePolicy.setUsingInternalFramesNextTime(useInternalFramesNextTime);
      }
    });
    framesCheckBox.setName(INTERNALFRAMES_CHECKBOX_NAME);

    JLabel infoLabel = new BorderedLabel(getLabelLocalizer().getString(LINES_LABEL));

    int initialLinesValue = getPreferences().getInt(PREFKEY_LINES, DEF_LINES);
    if (initialLinesValue < MIN_LINES) initialLinesValue = MIN_LINES;
    if (initialLinesValue > MAX_LINES) initialLinesValue = MAX_LINES;
    final JSpinner linesSpinner = new JSpinner(new SpinnerNumberModel(initialLinesValue, MIN_LINES,
                                                                      MAX_LINES, STEP_LINES));
    linesSpinner.setName(LINES_SPINNER_NAME);
    linesSpinner.setMaximumSize(linesSpinner.getPreferredSize());               // prevent it from stretching

    boolean initialHorizontal                   = getPreferences().getBoolean(PREFKEY_HORIZONTAL, true);
    final JComboBox<Boolean> horizontalCombobox = new JComboBox<>(new Boolean[] {true, false});
    horizontalCombobox.setName(COMBO_HORIZONTAL_NAME);
    horizontalCombobox.setSelectedItem(initialHorizontal);
    horizontalCombobox.setRenderer(new HorizontalCellRenderer());
    horizontalCombobox.setMaximumSize(horizontalCombobox.getPreferredSize());   // prevent it from stretching

    JButton arrangeButton = new JButton(getLabelLocalizer().getString(REARRANGE_LABEL));
    arrangeButton.setName(ARRANGE_BUTTON_NAME);
    arrangeButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        int amountOfLines    = (int) linesSpinner.getValue();
        boolean horizontally = (boolean) horizontalCombobox.getSelectedItem();
        manager.rearrangeWindows(amountOfLines, horizontally);

        getPreferences().putInt(PREFKEY_LINES, amountOfLines);
        getPreferences().putBoolean(PREFKEY_HORIZONTAL, horizontally);
      }
    });

    JPanel secondRow = new JPanel();
    secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));
    secondRow.add(framesCheckBox);
    secondRow.add(Box.createHorizontalGlue());
    secondRow.add(infoLabel);
    secondRow.add(linesSpinner);
    secondRow.add(horizontalCombobox);
    secondRow.add(arrangeButton);
    add(secondRow);
  }

//------------------------------------------------------------------------------

  /* Sets up the components used to automatically apply filters to display frames.
   * The preferences are read and updated conveniently.
   * To be called just once. */

  private void addRefilterComponents()
  {
    applySequentialFilters = getPreferences().getBoolean(PREFKEY_APPLYFILTER, false);

    final JCheckBox checkBox = new JCheckBox(getLabelLocalizer().getString(AUTOAPPLY_LABEL),
                                             applySequentialFilters);
    checkBox.setName(AUTOAPPLY_FILTER_NAME);
    checkBox.addChangeListener(new ChangeListener()
    {
      @Override
      public void stateChanged(ChangeEvent e)
      {
        applySequentialFilters = checkBox.isSelected();
        getPreferences().putBoolean(PREFKEY_APPLYFILTER, applySequentialFilters);
      }
    });

    JButton filterButton = new JButton(getLabelLocalizer().getString(REAPPLY_LABEL));
    filterButton.setName(REFILTER_BUTTON_NAME);
    filterButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {manager.applySequentialFilters();}
    });

    JPanel thirdRow = new JPanel();
    thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.X_AXIS));
    thirdRow.add(checkBox);
    thirdRow.add(Box.createHorizontalGlue());
    thirdRow.add(filterButton);
    add(thirdRow);
  }

//------------------------------------------------------------------------------

  /* Instantiates a new frame through the manager and adds its panel to the
   * repeater, so that it receives images from sources.
   * This frame is added as close listener in order to remove the panels from
   * the repeater when their window is closed.
   * A drop target is created so that images can be dropped to the display frame
   * too if there is a drop listener. */

  private void getNewDisplayFrame()
  {
    DisplayFrame frame        = manager.getNewDisplayFrame(applySequentialFilters);
    DisplayPanel displayPanel = frame.getDisplayPanel();

    repeaterDisplay.addReceiver(displayPanel);
    frame.addListener(this);
    addDropTarget(frame.getOwnerFrame());
  }

//------------------------------------------------------------------------------

  /* If there is a drop listener, the passed component is set up so that its
   * drop events are handled by it. */

  private void addDropTarget(Component dropComponent)
  {
    if (dropListener != null) new DropTarget(dropComponent, dropListener);
  }

//------------------------------------------------------------------------------

  private String getAmountOfWindowsText(DisplayFrameManager frameManager)
  {
    String messagePattern = getLabelLocalizer().getString(AMOUNT_LABEL);
    int amountOfWindows   = frameManager.getAmountOfOpenDisplayFrames();

    MessageFormat messageFormat = new MessageFormat(messagePattern);
    return messageFormat.format(new Object[] {amountOfWindows});
  }

//------------------------------------------------------------------------------

  /* Shortcut method to get preferences by subclasses that store the last URL. */

  private Preferences getPreferences() {return preferences;}

//------------------------------------------------------------------------------

  /* Returns the preferences to use when they are not externally injected. */

  private static Preferences getDefaultPreferences() {return Preferences.userNodeForPackage(WindowControlPanel.class);}

//------------------------------------------------------------------------------

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
      String displayValue = getLabelLocalizer().getString(labelKey);
      return originalRenderer.getListCellRendererComponent(list, displayValue, index,
                                                           isSelected, cellHasFocus);
    }
  }

//******************************************************************************

  /* Saver of the new autoopen value in the preferences. */

  private class AutoopenCheckBoxAutoopenChangeListener implements ChangeListener
  {
    @Override
    public void stateChanged(ChangeEvent event)
    {
      JCheckBox sourceCheckbox = (JCheckBox) event.getSource();
      boolean autoOpenNextTime = sourceCheckbox.isSelected();
      getPreferences().putBoolean(PREFKEY_AUTOOPEN, autoOpenNextTime);
    }
  }

//******************************************************************************

}
