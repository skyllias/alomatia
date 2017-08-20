
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.text.*;
import java.util.prefs.*;

import javax.swing.*;
import javax.swing.plaf.basic.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.ui.DisplayFrame.*;
import org.skyllias.alomatia.ui.DisplayFrameManager.*;

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
  private static final String TITLE_LABEL     = "frame.control.title";
  private static final String AMOUNT_LABEL    = "frame.control.amount";
  private static final String ADD_LABEL       = "frame.control.add";
  private static final String LINES_LABEL     = "frame.control.arrange.info";
  private static final String COLUMNS_LABEL   = "frame.control.arrange.columns";
  private static final String ROWS_LABEL      = "frame.control.arrange.rows";
  private static final String REARRANGE_LABEL = "frame.control.arrange.rearrange";

  protected static final String AMOUNT_LABEL_NAME     = "label.amount";         // name for the components
  protected static final String ADD_BUTTON_NAME       = "button.add";
  protected static final String LINES_SPINNER_NAME    = "spinner.lines";
  protected static final String COMBO_HORIZONTAL_NAME = "combobox.horizontal";
  protected static final String ARRANGE_BUTTON_NAME   = "button.arrange";

  private static final String PREFKEY_HORIZONTAL = "arrangeHorizontally";
  private static final String PREFKEY_LINES      = "amountOfLinesToRearrangeIn";

  private DisplayFrameManager manager;

  private Repeater repeaterDisplay;

  private DropTargetListener dropListener;                                      // may be null. Otherwise, used to dispatch drops on the display frames

  private JLabel amountLabel;                                                   // label displaying the amount of open windows

//==============================================================================

  /** Automatically opens a new display window after creation. */

  public WindowControlPanel(LabelLocalizer localizer, Repeater displayRepeater,
                            DropTargetListener dropTargetListener, DisplayFrameManager frameManager)
  {
    super(localizer, TITLE_LABEL);

    repeaterDisplay = displayRepeater;
    manager         = frameManager;
    dropListener    = dropTargetListener;

    manager.addAmountChangeListener(this);

    addAdditionComponents();
    addRearrangeComponents();
    addNewDisplayKeyListener();

    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run() {getNewDisplayFrame();}                                 // open the display window after this one's, or else it appears before and separated from the subsequent windows in the task bar
    });
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

  /* Sets up the components used to display the amount of windows and create new ones.
   * To be called just once. */

  private void addAdditionComponents()
  {
    amountLabel = new JLabel(getAmountOfWindowsText(manager));   // TODO add margin
    amountLabel.setName(AMOUNT_LABEL_NAME);

    JButton addButton = new JButton(getLabelLocalizer().getString(ADD_LABEL));
    addButton.setName(ADD_BUTTON_NAME);
    addButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {getNewDisplayFrame();}
    });

    JPanel firstRow = new JPanel();
    firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.X_AXIS));
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

    JLabel infoLabel = new JLabel(getLabelLocalizer().getString(LINES_LABEL));      // TODO add margin

    int initialLinesValue       = getPreferences().getInt(PREFKEY_LINES, DEF_LINES);
    final JSpinner linesSpinner = new JSpinner(new SpinnerNumberModel(initialLinesValue, MIN_LINES,
                                                                      MAX_LINES, STEP_LINES));
    linesSpinner.setName(LINES_SPINNER_NAME);
    linesSpinner.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                              linesSpinner.getPreferredSize().height));   // prevent it from stretching vertically

    boolean initialHorizontal                   = getPreferences().getBoolean(PREFKEY_HORIZONTAL, true);
    final JComboBox<Boolean> horizontalCombobox = new JComboBox<>(new Boolean[] {true, false});
    horizontalCombobox.setName(COMBO_HORIZONTAL_NAME);
    horizontalCombobox.setSelectedItem(initialHorizontal);
    horizontalCombobox.setRenderer(new HorizontalCellRenderer());
    horizontalCombobox.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                                    horizontalCombobox.getPreferredSize().height));   // prevent it from stretching vertically

    JButton arrangeButton = new JButton(getLabelLocalizer().getString(REARRANGE_LABEL));
    arrangeButton.setName(ARRANGE_BUTTON_NAME);
    arrangeButton.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        int amountOfLines    = (int) linesSpinner.getValue();
        boolean horizontally = (boolean) horizontalCombobox.getSelectedItem();
        rearrange(amountOfLines, horizontally);

        getPreferences().putInt(PREFKEY_LINES, amountOfLines);
        getPreferences().putBoolean(PREFKEY_HORIZONTAL, horizontally);
      }
    });

    JPanel secondRow = new JPanel();
    secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.X_AXIS));
    secondRow.add(infoLabel);
    secondRow.add(linesSpinner);
    secondRow.add(horizontalCombobox);
    secondRow.add(arrangeButton);
    add(secondRow);
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
    DisplayFrame frame        = manager.getNewDisplayFrame();
    DisplayPanel displayPanel = frame.getDisplayPanel();

    repeaterDisplay.addReceiver(displayPanel);
    frame.addListener(this);
    addDropTarget(frame);
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

  /* Resizes and locates the windows from the manager so that they cover as much
   * of the screen where this component is (unless they have been changed from
   * device) without overlapping. */

  private void rearrange(int amountOfLines, boolean horizontally)
  {
    Rectangle screenBounds = getScreenBounds();
    manager.rearrangeWindows(amountOfLines, screenBounds, horizontally);
  }

//------------------------------------------------------------------------------

  /* Returns a rectangle whose corners are on the corners of the screen where
   * this component is, substracting the system's taskbar, if any.
   * Returns null if theis component has not been added to a parent. */

  private Rectangle getScreenBounds()
  {
    GraphicsConfiguration graphicsConfig = getGraphicsConfiguration();
    if (graphicsConfig == null) return null;

    Rectangle bounds    = graphicsConfig.getBounds();
    Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(graphicsConfig);

    Rectangle effectiveScreenArea = new Rectangle();
    effectiveScreenArea.x      = bounds.x + screenInsets.left;
    effectiveScreenArea.y      = bounds.y + screenInsets.top;
    effectiveScreenArea.height = bounds.height - screenInsets.top  - screenInsets.bottom;
    effectiveScreenArea.width  = bounds.width  - screenInsets.left - screenInsets.right;
    return effectiveScreenArea;
  }

//------------------------------------------------------------------------------

  /** Shortcut method to get preferences by subclasses that store the last user selection. */

  protected Preferences getPreferences() {return Preferences.userNodeForPackage(getClass());}

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

}
