
package org.skyllias.alomatia.ui.radio;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import org.skyllias.alomatia.i18n.LabelLocalizer;

/** Creator of radio buttons belonging to the same group and associated
 *  to an object of some generic type SELECTABLE. When a radio button is
 *  selected, a RadioSelectorListener is notified.
 *  Since these radio buttons can be used in different components (like panels
 *  and menus), this class is also generic in regards to the Swing component
 *  to use. Most times it will be a JRadioButton, but it may be a
 *  JRadioButtonMenuItem as well. */

public class RadioSelector<RADIO extends AbstractButton, SELECTABLE> implements ActionListener
{
  private final Class<RADIO> radioClass;
  private final LabelLocalizer labelLocalizer;
  private final RadioSelectorListener<SELECTABLE> listener;

  private final ButtonGroup radioGroup = new ButtonGroup();                     // group for all radio buttons of this selector

  private final Map<String, SELECTABLE> commandObjects = new LinkedHashMap<String, SELECTABLE>();  // radio buttons do not support referencing objects, only string as action commands, so this will contain the relationship between each action commands and the real object selected by each radio button
  private final Map<SELECTABLE, RADIO> objectRadios    = new LinkedHashMap<SELECTABLE, RADIO>();   // same as commandObjects, so that objectRadios.get(commandObjects.get(actionCommand)).getActionCommand().equals(actionCommand), unless there is some null anywhere

//==============================================================================

  /** Creates a new instance that will create radio buttons of radioClazz type
   *  and notify selectorListener whenever the selected radio button changes. */

  public RadioSelector(Class<RADIO> radioClazz, LabelLocalizer localizer,
                       RadioSelectorListener<SELECTABLE> selectorListener)
  {
    radioClass     = radioClazz;
    labelLocalizer = localizer;
    listener       = selectorListener;
  }

//==============================================================================

  /** Creates a new radio button with the passed actionCommand and the localized
   *  label corresponding to te action command, relating it to the passed object.
   *  The component is NOT added anywhere.
   *  <p>
   *  This selector is added as ActionListener to the new radio button, so
   *  that the listener can learn that there has been a selection change by
   *  implementing {@link #onSelectionChanged(SELECTABLE)}.
   *  <p>
   *  All radio buttons are added to the same ButtonGroup, selecting by default
   *  the first one added.
   *  <p>
   *  All action commands should be different. */

  public RADIO createRadioObject(String actionCommand, SELECTABLE object)
  {
    try
    {
      RADIO radio = radioClass.newInstance();
      radio.setText(labelLocalizer.getString(actionCommand));
      radio.setActionCommand(actionCommand);
      radio.setName(actionCommand);
      radio.setSelected(!radioGroup.getElements().hasMoreElements());
      radio.addActionListener(this);
      radioGroup.add(radio);

      commandObjects.put(actionCommand, object);
      objectRadios.put(object, radio);

      return radio;
    }
    catch (Exception e) {throw new RuntimeException("Could not instantiate radio component", e);} // this should never happen
  }

//------------------------------------------------------------------------------

  /** Invokes {@link #onSelectionChanged(SELECTABLE)} with the object related in
   *  {@link #createRadioObject(String, Object)} to the selected radio button. */

  @Override
  public void actionPerformed(ActionEvent e)
  {
    String actionName        = e.getActionCommand();
    SELECTABLE relatedObject = commandObjects.get(actionName);
    listener.onSelectionChanged(relatedObject);
  }

//------------------------------------------------------------------------------

  /** Returns the object related to the currently selected radio in the group, if any.
   *  This method should be seldom required. */

  public SELECTABLE getCurrentSelection()
  {
    return commandObjects.get(radioGroup.getSelection().getActionCommand());
  }

//------------------------------------------------------------------------------

  /** Returns the action command of the currently selected radio in the group, if any.
   * <p>
   * {@link #getCurrentSelection()} is preferred over this, which is reserved to
   * cases where the selected object cannot be used (like when storing selection
   * in Preferences). */

  public String getCurrentSelectionAsActionCommand()
  {
    return radioGroup.getSelection().getActionCommand();
  }

//------------------------------------------------------------------------------

  /** Changes the radio currently selected to that with the passed object related.
   *  <p>
   *  If there is none, then nothing happens. If there are more than one, the
   *  last one added is chosen.
   *  <p>
   *  If the selection changes, this will end up firing {@link #onSelectionChanged(SELECTABLE)}. */

  public void setSelection(SELECTABLE newSelection)
  {
    AbstractButton selectedRadio = objectRadios.get(newSelection);
    if (selectedRadio != null) selectedRadio.doClick();
  }

//------------------------------------------------------------------------------

  /** Changes the radio currently selected to that with the passed action command.
   *  <p>
   *  If there is none, then nothing happens. If there are more than one, the
   *  last one added is chosen.
   *  <p>
   *  If the selection changes, this will end up firing {@link #onSelectionChanged(SELECTABLE)}.
   *  <p>
   *  {@link #setSelection(SELECTABLE)} is prefered over this, which is reserved to
   *  cases where the selected object cannot be obtained (like in Preferences). */

  public void setSelectionByActionCommand(String newSelectionCommand)
  {
    SELECTABLE newlySelectedObject = commandObjects.get(newSelectionCommand);
    if (newlySelectedObject != null) setSelection(newlySelectedObject);

  }

//------------------------------------------------------------------------------

  /** Changes the selection to the radio at the passed position, being 0 the first one.
   *  If index is below zero or above the amount of filters, nothing happens.
   *  <p>
   *  If the selection changes, this will end up firing {@link #onSelectionChanged(SELECTABLE)}.
   *  <p>
   *  {@link #setSelection(SELECTABLE)} is prefered over this. */

  public void setSelectionByIndex(int index)
  {
    if (index >= 0 && index < objectRadios.size())
    {
      Enumeration<AbstractButton> radioButtons = radioGroup.getElements();
      AbstractButton selectedRadio             = null;
      int currentIndex                         = 0;
      while (radioButtons.hasMoreElements() && currentIndex++ <= index) selectedRadio = radioButtons.nextElement();
      if (selectedRadio != null) selectedRadio.doClick();
    }
  }

//------------------------------------------------------------------------------

  /** Changes the selection to the radio at a position equal to the currently
   *  selected plus increment. Increment can be any positive or negative number
   *  (zero is possible but does not make much sense). If the resulting index is
   *  below zero or above the amount of radios, the selection cycles.
   *  <p>
   *  If the selection changes, this will end up firing {@link #onSelectionChanged(SELECTABLE)}. */

  public void setSelectionByIndexIncrement(int increment)
  {
    if (commandObjects.size() > 0)
    {
      List<String> actionCommands = new ArrayList<>(commandObjects.keySet());

      String currentActionCommand = radioGroup.getSelection().getActionCommand();
      int currentSelectionIndex   = Math.max(0, actionCommands.indexOf(currentActionCommand));

      int finalSelectionIndex      = Math.floorMod(currentSelectionIndex + increment,
                                                   actionCommands.size());
      String selectedActionCommand = actionCommands.get(finalSelectionIndex);
      AbstractButton selectedRadio = objectRadios.get(commandObjects.get(selectedActionCommand));

      if (selectedRadio != null) selectedRadio.doClick();
    }
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /** Object to be notified when a radio button is selected. */

  public static interface RadioSelectorListener<SELECTABLE>
  {
    void onSelectionChanged(SELECTABLE selectedObject);
  }
}
