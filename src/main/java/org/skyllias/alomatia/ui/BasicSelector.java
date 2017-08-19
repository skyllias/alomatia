
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.prefs.*;

import javax.swing.*;

import org.skyllias.alomatia.i18n.*;

/** Superclass for the configuration selectors.
 *  <p>
 *  Each panel has an HTML-like fieldgroup.
 *  <p>
 *  A generic type is used for related objects to the radio buttons used to
 *  select them. */

@SuppressWarnings("serial")
public abstract class BasicSelector<SELECTABLE> extends BasicControlPanel
                                                implements ActionListener
{
  private ButtonGroup radioGroup = new ButtonGroup();                           // group for all radio buttons of this selector

  private Map<String, SELECTABLE> commandObjects     = new LinkedHashMap<String, SELECTABLE>();       // radio buttons do not support referencing objects, only string as action commands, so this will contain the relationship between each action commands and the real object selected by each radio button
  private Map<SELECTABLE, JRadioButton> objectRadios = new LinkedHashMap<SELECTABLE, JRadioButton>(); // same as commandObjects, so that objectRadios.get(commandObjects.get(actionCommand).equals(actionCommand), unless there is some null anywhere

//==============================================================================

  /** Creates a new panel with the passed title localized. */

  protected BasicSelector(LabelLocalizer localizer, String legendKey)
  {
    super(localizer, legendKey);
  }

//==============================================================================

  /** Creates a new radio button with the passed actionCommand and the localized
   *  label corresponding to te action command, relating it to the passed object,
   *  and then adds to the passed parent.
   *  <p>
   *  This BasicSelector is added as ActionListener to the new radio button, so
   *  that subclasses can learn that there has been a selection change by
   *  implementing {@link #onSelectionChanged(SELECTABLE)}.
   *  <p>
   *  All radio buttons are added to the same ButtonGroup, selecting by default
   *  the first one added.
   *  <p>
   *  All action commands should be different. */

  protected void addRadioObject(String actionCommand, SELECTABLE object, Container parent)
  {
    JRadioButton radio = new JRadioButton(getLabelLocalizer().getString(actionCommand));
    radio.setActionCommand(actionCommand);
    radio.setName(actionCommand);
    radio.setSelected(!radioGroup.getElements().hasMoreElements());
    radio.addActionListener(this);
    radioGroup.add(radio);

    commandObjects.put(actionCommand, object);
    objectRadios.put(object, radio);

    parent.add(radio);
  }

//------------------------------------------------------------------------------

  /** Returns the object related to the currently selected radio in the group, if any. */

  protected SELECTABLE getCurrentSelection()
  {
    return commandObjects.get(radioGroup.getSelection().getActionCommand());
  }

//------------------------------------------------------------------------------

  /** Returns the action command of the currently selected radio in the group, if any.
   * <p>
   * {@link #getCurrentSelection()} is preferred over this, which is reserved to
   * cases where the selected object cannot be used (like in Preferences). */

  protected String getCurrentSelectionAsActionCommand()
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

  protected void setSelection(SELECTABLE newSelection)
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
   *  {@link #setSelection(SELECTABLE)} is prefered over this, wihch is reserved to cases
   *  where the selected object cannot be obtained (like in Preferences). */

  protected void setSelectionByActionCommand(String newSelectionCommand)
  {
    SELECTABLE newlySelectedObject = commandObjects.get(newSelectionCommand);
    if (newlySelectedObject != null) setSelection(newlySelectedObject);

  }

//------------------------------------------------------------------------------

  /** Invokes {@link #onSelectionChanged(SELECTABLE)} with the object related in
   *  {@link #addRadioObject(String, Object, Container)} to the selected radio button. */

  @Override
  public void actionPerformed(ActionEvent e)
  {
    String actionName        = e.getActionCommand();
    SELECTABLE relatedObject = commandObjects.get(actionName);
    onSelectionChanged(relatedObject);
  }

//------------------------------------------------------------------------------

  /** Invoked whenever a change occurs in the selected object. */

  protected abstract void onSelectionChanged(SELECTABLE selectedObject);

//------------------------------------------------------------------------------

  /** Shortcut method to get preferences by subclasses that store the last user selection. */

  protected Preferences getPreferences() {return Preferences.userNodeForPackage(getClass());}

//------------------------------------------------------------------------------

}
