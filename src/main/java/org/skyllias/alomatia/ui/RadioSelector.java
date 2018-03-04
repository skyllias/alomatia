
package org.skyllias.alomatia.ui;

import java.awt.event.*;
import java.util.*;

import javax.swing.*;

import org.skyllias.alomatia.i18n.*;

/** Creator of {@link JRadioButton}s belonging to the same group and associated
 *  to an object of some generic type SELECTABLE. When a radio button is
 *  selected, a RadioSelectorListener is notified. */

public class RadioSelector<SELECTABLE> implements ActionListener
{
  private ButtonGroup radioGroup = new ButtonGroup();                           // group for all radio buttons of this selector

  private Map<String, SELECTABLE> commandObjects     = new LinkedHashMap<String, SELECTABLE>();       // radio buttons do not support referencing objects, only string as action commands, so this will contain the relationship between each action commands and the real object selected by each radio button
  private Map<SELECTABLE, JRadioButton> objectRadios = new LinkedHashMap<SELECTABLE, JRadioButton>(); // same as commandObjects, so that objectRadios.get(commandObjects.get(actionCommand).equals(actionCommand), unless there is some null anywhere

  private final LabelLocalizer labelLocalizer;
  private final RadioSelectorListener<SELECTABLE> listener;

//==============================================================================

  /** Creates a new instance that will notify selectorListener whenever the
   *  selected radio button changes. */

  public RadioSelector(LabelLocalizer localizer, RadioSelectorListener<SELECTABLE> selectorListener)
  {
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

  public JRadioButton createRadioObject(String actionCommand, SELECTABLE object)
  {
    JRadioButton radio = new JRadioButton(labelLocalizer.getString(actionCommand));
    radio.setActionCommand(actionCommand);
    radio.setName(actionCommand);
    radio.setSelected(!radioGroup.getElements().hasMoreElements());
    radio.addActionListener(this);
    radioGroup.add(radio);

    commandObjects.put(actionCommand, object);
    objectRadios.put(object, radio);

    return radio;
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
    JRadioButton selectedRadio = objectRadios.get(newSelection);
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

//******************************************************************************

  /** Object to be notified when a radio button is selected. */

  public static interface RadioSelectorListener<SELECTABLE>
  {
    void onSelectionChanged(SELECTABLE selectedObject);
  }
}
