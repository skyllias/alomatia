
package org.skyllias.alomatia.ui;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.skyllias.alomatia.display.DisplayFitPolicy;
import org.skyllias.alomatia.display.ResizableDisplay;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.ZoomPreferences;
import org.skyllias.alomatia.ui.RadioSelector.RadioSelectorListener;
import org.springframework.stereotype.Component;

/** Composer of a panel with controls to change the zoom of some {@link ResizableDisplay}.
 *  The last selected fit policy and slider values are stored as user preferences.
 *  A keyboard listener is added so that the zoom can be modified from keyboard
 *  shortcuts, which means that all instances will respond at the same time to
 *  zoom changes from the keyboard, while the UI component will work separately. */

@Component
public class ZoomSelectorComposer
{
  protected static final String CUSTOM_LABEL         = "zoom.custom.name";
  protected static final String FIT_FULL_LABEL       = "zoom.full.name";
  protected static final String FIT_HORIZONTAL_LABEL = "zoom.width.name";
  protected static final String FIT_VERTICAL_LABEL   = "zoom.height.name";
  protected static final String FIT_LARGEST_LABEL    = "zoom.largest.name";

  private static final String ZOOM_LABEL           = "zoom.selector.title";
  private static final String ZOOM_TOOLTIP         = "zoom.custom.tooltip";

  private static final int SLIDER_SCALE     = 100;                              // since JSlider does not support decimal values, it will go between -SLIDER_SCALE and SLIDER_SCALE, with 0 being the initial value
  private static final int SLIDER_INCREMENT = 10;
  private static final int SLIDER_INITIAL   = 0;

  private final LabelLocalizer labelLocalizer;
  private final BarePanelComposer bareControlPanelComposer;

  private final ZoomPreferences zoomPreferences;

//==============================================================================

  /** Creates a new selector that will modify some display's zoom. */

  public ZoomSelectorComposer(LabelLocalizer localizer,
                              BarePanelComposer panelComposer,
                              ZoomPreferences preferences)
  {
    labelLocalizer           = localizer;
    bareControlPanelComposer = panelComposer;
    zoomPreferences          = preferences;
  }

//==============================================================================

  /** Returns the component with the zoom controls set up for the image display. */

  public JComponent getComponent(ResizableDisplay resizableDisplay)
  {
    JPanel panel = bareControlPanelComposer.getPanel(labelLocalizer.getString(ZOOM_LABEL));

    JSlider zoomSlider = getNewSlider(resizableDisplay,
                                      zoomPreferences.getSliderValue(SLIDER_INITIAL));
    panel.add(zoomSlider);

    setUpFitPolicyRadioSelector(panel, zoomSlider, resizableDisplay);

    return panel;
  }

//------------------------------------------------------------------------------

  /* Returns a new JSlider instance with all the configurations set to work as a
   * zoom selector.
   * The value of the slider is set to initialValue before any change listener
   * is set up. Therefore, the resizableDisplay does NOT get any value set and
   * is only used for configurations of future events. */

  private JSlider getNewSlider(ResizableDisplay resizableDisplay, int initialValue)
  {
    JSlider newSlider = new JSlider(JSlider.HORIZONTAL, -SLIDER_SCALE, SLIDER_SCALE, SLIDER_INITIAL);
    newSlider.setMajorTickSpacing(SLIDER_INCREMENT);
    newSlider.setMinorTickSpacing(SLIDER_INCREMENT);
    newSlider.setPaintTicks(true);
    newSlider.setToolTipText(labelLocalizer.getString(ZOOM_TOOLTIP));

    Dictionary<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
    addSliderLabel(labels, -SLIDER_SCALE);
    addSliderLabel(labels, -SLIDER_SCALE / 2);
    addSliderLabel(labels, SLIDER_INITIAL);
    addSliderLabel(labels, SLIDER_SCALE / 2);
    addSliderLabel(labels, SLIDER_SCALE);
    newSlider.setLabelTable(labels);
    newSlider.setPaintLabels(true);

    newSlider.setValue(initialValue);

    newSlider.addChangeListener(new ZoomSliderChangeListener(newSlider, resizableDisplay));
    newSlider.addMouseListener(new ResetSliderMouseListener(newSlider));

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

  private void setUpFitPolicyRadioSelector(JPanel panel, JSlider zoomSlider,
                                           ResizableDisplay resizableDisplay)
  {
    RadioSelector<JRadioButton, DisplayFitPolicy> radioSelector = new RadioSelector<>(JRadioButton.class,
                                                                                      labelLocalizer,
                                                                                      new FitPolicySelectorListener(zoomSlider, resizableDisplay));

    panel.add(radioSelector.createRadioObject(CUSTOM_LABEL, DisplayFitPolicy.FREE));
    panel.add(radioSelector.createRadioObject(FIT_FULL_LABEL, DisplayFitPolicy.FULL));
    panel.add(radioSelector.createRadioObject(FIT_HORIZONTAL_LABEL, DisplayFitPolicy.HORIZONTAL));
    panel.add(radioSelector.createRadioObject(FIT_VERTICAL_LABEL, DisplayFitPolicy.VERTICAL));
    panel.add(radioSelector.createRadioObject(FIT_LARGEST_LABEL, DisplayFitPolicy.LARGEST));

    addPolicyKeyListener(radioSelector);

    DisplayFitPolicy initialPolicy = zoomPreferences.getDisplayFitPolicy();
    radioSelector.setSelection(initialPolicy);
  }

//------------------------------------------------------------------------------

  /* Sets the zoom factor selected in the slider to the resizable display.
   * The selection is stored in the user preferences. */

  private void setZoomFactorFromSlider(JSlider zoomSlider,
                                       ResizableDisplay resizableDisplay)
  {
    int sliderValue = zoomSlider.getValue();
    double expValue = getZoomFactorFromSlider(sliderValue);
    resizableDisplay.setZoomFactor(expValue);

    zoomPreferences.setSilderValue(sliderValue);
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

  private void addPolicyKeyListener(final RadioSelector<JRadioButton,DisplayFitPolicy> radioSelector)
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

  /* Listener of changes of the fit policy selector. */

  private class FitPolicySelectorListener implements RadioSelectorListener<DisplayFitPolicy>
  {
    private final JSlider zoomSlider;
    private final ResizableDisplay resizableDisplay;

    public FitPolicySelectorListener(JSlider zoomSlider,
                                     ResizableDisplay resizableDisplay)
    {
      this.zoomSlider       = zoomSlider;
      this.resizableDisplay = resizableDisplay;
    }

    /* Sets the selected fit type to the image display.
     * If the type is DisplayFitPolicy.FREE, then the display's zoom factor is
     * updated from the slider's current value.
     * The slider is enabled if and only if the fit policy is FREE.
     * The selection is stored in the user preferences. */

    @Override
    public void onSelectionChanged(DisplayFitPolicy type)
    {
      boolean isFreePolicy = (type == DisplayFitPolicy.FREE);
      zoomSlider.setEnabled(isFreePolicy);
      if (isFreePolicy) setZoomFactorFromSlider(zoomSlider, resizableDisplay);
      resizableDisplay.setFitZoom(type);

      zoomPreferences.setDisplayFitPolicy(type);
    }
  }

//******************************************************************************

  /* Listener of changes in the slider to modify the zoom in the display. */

  private class ZoomSliderChangeListener implements ChangeListener
  {
    private final JSlider zoomSlider;
    private final ResizableDisplay resizableDisplay;

    public ZoomSliderChangeListener(JSlider zoomSlider,
                                    ResizableDisplay resizableDisplay)
    {
      this.zoomSlider       = zoomSlider;
      this.resizableDisplay = resizableDisplay;
    }

    /* Sets the zoom factor selected in the slider to the image display.
     * The linear int value from the slider is converted into an exponential
     * decimal value for the display, which is much more suitable for the
     * behaviour of a zoom. */

    @Override
    public void stateChanged(ChangeEvent e)
    {
      setZoomFactorFromSlider(zoomSlider, resizableDisplay);
    }

  //------------------------------------------------------------------------------
  }

//******************************************************************************

  /* Listener of clicks on the slider to reset it easily. */

  private class ResetSliderMouseListener extends MouseAdapter
  {
    private final JSlider zoomSlider;

    public ResetSliderMouseListener(JSlider zoomSlider)
    {
      this.zoomSlider = zoomSlider;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
      if (e.getClickCount() > 1) zoomSlider.setValue(SLIDER_INITIAL);
    }
  }

}
