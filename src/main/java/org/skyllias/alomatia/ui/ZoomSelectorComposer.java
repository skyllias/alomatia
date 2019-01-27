
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.prefs.*;

import javax.swing.*;
import javax.swing.event.*;

import org.skyllias.alomatia.display.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.ui.RadioSelector.*;

/** Composer of a panel with controls to change the zoom of some {@link ResizableDisplay}.
 *  The last selected fit policy and slider values are stored as user preferences.
 *  A keyboard listener is added so that the zoom can be modified from keyboard
 *  shortcuts, which means that all instances will respond at the same time to
 *  zoom changes from the keyboard, while the UI component will work separately. */

public class ZoomSelectorComposer implements RadioSelectorListener<DisplayFitPolicy>,
                                             ChangeListener
{
  private static final String ZOOM_LABEL           = "zoom.selector.title";
  private static final String ZOOM_TOOLTIP         = "zoom.custom.tooltip";
  private static final String CUSTOM_LABEL         = "zoom.custom.name";
  private static final String FIT_FULL_LABEL       = "zoom.full.name";
  private static final String FIT_HORIZONTAL_LABEL = "zoom.width.name";
  private static final String FIT_VERTICAL_LABEL   = "zoom.height.name";
  private static final String FIT_LARGEST_LABEL    = "zoom.largest.name";

  private static final int SLIDER_SCALE     = 100;                              // since JSlider does not support decimal values, it will go between -SLIDER_SCALE and SLIDER_SCALE, with 0 being the initial value
  private static final int SLIDER_INCREMENT = 10;
  private static final int SLIDER_INITIAL   = 0;

  private static final String PREFKEY_FIT  = "fitPolicy";
  private static final String PREFKEY_ZOOM = "zoomSlider";

  private final LabelLocalizer labelLocalizer;

  private ResizableDisplay resizableDisplay;

  private JSlider zoomSlider;
  private RadioSelector<JRadioButton, DisplayFitPolicy> radioSelector;

  private Preferences preferences = Preferences.userNodeForPackage(ZoomSelectorComposer.class);

//==============================================================================

  /** Creates a new selector that will modify the passed display's zoom. */

  public ZoomSelectorComposer(LabelLocalizer localizer, ResizableDisplay imageDisplay)
  {
    labelLocalizer   = localizer;
    resizableDisplay = imageDisplay;

    radioSelector = new RadioSelector<>(JRadioButton.class, labelLocalizer, this);
  }

//==============================================================================

  /** Returns the component with the zoom controls set up.
   *  To be called just once. */

  public JComponent getComponent()
  {
    JPanel panel = new BasicControlPanelComposer().getPanel(labelLocalizer.getString(ZOOM_LABEL));

    zoomSlider = getNewSlider();
    panel.add(zoomSlider);

    panel.add(radioSelector.createRadioObject(CUSTOM_LABEL, DisplayFitPolicy.FREE));
    panel.add(radioSelector.createRadioObject(FIT_FULL_LABEL, DisplayFitPolicy.FULL));
    panel.add(radioSelector.createRadioObject(FIT_HORIZONTAL_LABEL, DisplayFitPolicy.HORIZONTAL));
    panel.add(radioSelector.createRadioObject(FIT_VERTICAL_LABEL, DisplayFitPolicy.VERTICAL));
    panel.add(radioSelector.createRadioObject(FIT_LARGEST_LABEL, DisplayFitPolicy.LARGEST));

    addPolicyKeyListener();

    zoomSlider.setValue(preferences.getInt(PREFKEY_ZOOM, SLIDER_INITIAL));
    DisplayFitPolicy initialPolicy = DisplayFitPolicy.FREE;
    try {initialPolicy = DisplayFitPolicy.valueOf(preferences.get(PREFKEY_FIT, DisplayFitPolicy.FREE.toString()));}
    catch (Exception e) {}                                                      // preferences are optional
    radioSelector.setSelection(initialPolicy);

    return panel;
  }

//------------------------------------------------------------------------------

  /** Sets the selected fit type to the image display.
   *  <p>
   *  If the type is {@link org.skyllias.alomatia.window.DisplayFitPolicy.FREE},
   *  then the display's zoom factor is updated from the slider's current value.
   *  <p>
   *  The slider is enabled if and only if the dit policy is FREE.
   *  <p>
   *  The selection is stored in the user preferences. */

  @Override
  public void onSelectionChanged(DisplayFitPolicy type)
  {
    boolean isFreePolicy = (type == DisplayFitPolicy.FREE);
    zoomSlider.setEnabled(isFreePolicy);
    if (isFreePolicy) setZoomFactorFromSlider();
    resizableDisplay.setFitZoom(type);

    preferences.put(PREFKEY_FIT, type.toString());
  }

//------------------------------------------------------------------------------

  /** Sets the zoom factor selected in the slider to the image display.
   *  <p>
   *  The linear int value from the slider is converted into an exponential
   *  decimal value for the display, which is much more suitable for the
   *  behaviour of a zoom. */

  @Override
  public void stateChanged(ChangeEvent e) {setZoomFactorFromSlider();}

//------------------------------------------------------------------------------

  /* Returns a new JSlider instance with all the configurations set to work as a zoom selector. */

  private JSlider getNewSlider()
  {
    JSlider newSlider = new JSlider(JSlider.HORIZONTAL, -SLIDER_SCALE, SLIDER_SCALE, SLIDER_INITIAL);
    newSlider.setMajorTickSpacing(SLIDER_INCREMENT);
    newSlider.setMinorTickSpacing(SLIDER_INCREMENT);
    newSlider.setPaintTicks(true);
    newSlider.addChangeListener(this);
    newSlider.addMouseListener(new SliderMouseListener());
    newSlider.setToolTipText(labelLocalizer.getString(ZOOM_TOOLTIP));

    Dictionary<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
    addSliderLabel(labels, -SLIDER_SCALE);
    addSliderLabel(labels, -SLIDER_SCALE / 2);
    addSliderLabel(labels, SLIDER_INITIAL);
    addSliderLabel(labels, SLIDER_SCALE / 2);
    addSliderLabel(labels, SLIDER_SCALE);
    newSlider.setLabelTable(labels);
    newSlider.setPaintLabels(true);

    return newSlider;
  }

//------------------------------------------------------------------------------

  /* Adds a new pair to the dictionary with key sliderValue and value a label
   * with the percentual zoom factor that would be set to dispalyPanel if the
   * slider had sliderValue.  */

  private void addSliderLabel(Dictionary<Integer, JLabel> labels, int sliderValue)
  {
    final String PERCENT_SYMBOL = "%";

    double zoomPercent = getZoomFactorFromSlider(sliderValue) * 100;
    int percent        = (int) zoomPercent;
    String text        = String.valueOf(percent) + PERCENT_SYMBOL;
    labels.put(sliderValue, new JLabel(text));
  }

//------------------------------------------------------------------------------

  /* Sets the zoom factor selected in the slider to the image display.
   * The selection is stored in the user preferences. */

  private void setZoomFactorFromSlider()
  {
    int sliderValue = zoomSlider.getValue();
    double expValue = getZoomFactorFromSlider(sliderValue);
    resizableDisplay.setZoomFactor(expValue);

    preferences.putInt(PREFKEY_ZOOM, sliderValue);
  }

//------------------------------------------------------------------------------

  /* Returns the zoom factor corresponding to the passed value in the slider.
   * The linear int value from the slider is converted into an exponential
   * decimal value for the display, which is much more suitable for the
   * behaviour of a zoom. */

  private double getZoomFactorFromSlider(int sliderValue)
  {
    final double ZOOM_BASE = 4;                                                 // zoom factor when the slider is at its maximum or its minimum

    double linearValue = (double) sliderValue / (double) SLIDER_SCALE;          // something between -1 and 1
    return Math.pow(ZOOM_BASE, linearValue);
  }

//------------------------------------------------------------------------------

  /* Adds a global listener that changes the DisplayFitPolicy selection:
   * control + 0 for FREE, control + 1 for FULL, control + 2 for HORIZONTAL,
   * control + 3 for VERTICAL, control + 3 for LARGEST. */

  private void addPolicyKeyListener()
  {
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventDispatcher(new KeyEventDispatcher()
    {
      @Override
      public boolean dispatchKeyEvent(KeyEvent e)
      {
        if(e.getID() == KeyEvent.KEY_PRESSED && EventUtils.isControlDown(e))
        {
          DisplayFitPolicy newPolicy = null;
          if (e.getKeyCode() == KeyEvent.VK_0) newPolicy = DisplayFitPolicy.FREE;
          if (e.getKeyCode() == KeyEvent.VK_1) newPolicy = DisplayFitPolicy.FULL;
          if (e.getKeyCode() == KeyEvent.VK_2) newPolicy = DisplayFitPolicy.HORIZONTAL;
          if (e.getKeyCode() == KeyEvent.VK_3) newPolicy = DisplayFitPolicy.VERTICAL;
          if (e.getKeyCode() == KeyEvent.VK_4) newPolicy = DisplayFitPolicy.LARGEST;
          if (newPolicy != null) radioSelector.setSelection(newPolicy);
        }
        return false;                                                           // allow the event to be redispatched
      }
    });
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /* Listener of clicks on the slider to reset it easily. */

  private class SliderMouseListener extends MouseAdapter
  {
    @Override
    public void mouseClicked(MouseEvent e)
    {
      if (e.getClickCount() > 1) zoomSlider.setValue(SLIDER_INITIAL);
    }
  }

}
