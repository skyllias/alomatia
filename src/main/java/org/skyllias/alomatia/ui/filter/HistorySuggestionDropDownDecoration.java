
package org.skyllias.alomatia.ui.filter;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** Decorator of a text field that shows a drop drown list of suggestions
 *  extracted from the filter search history as the user types.
 *  SwingX components are the inspiration for this code, but they are not used
 *  directly due to the size of the library and the specific need in this project. */

public class HistorySuggestionDropDownDecoration
{
  private final JTextField inputField;
  private final FilterSearchHistory filterSearchHistory;

  private final JPopupMenu popupMenu;
  private final JList<String> listComponent;
  private final DefaultListModel<String> listModel;

  private State state = new State();

//==============================================================================

  /** Adds listeners to inputField for the drop down to appear and suggest as
   *  the user types. */

  public HistorySuggestionDropDownDecoration(JTextField inputField,
                                             FilterSearchHistory filterSearchHistory)
  {
    this.inputField          = inputField;
    this.filterSearchHistory = filterSearchHistory;

    popupMenu     = new JPopupMenu();
    listModel     = new DefaultListModel<>();
    listComponent = new JList<>(listModel);

    init();
  }

//==============================================================================

  /* Initializes the components and listeners. */

  private void init()
  {
    initPopup();
    initSuggestionComponentListener();
    initInvokerKeyListeners();
  }

//------------------------------------------------------------------------------

  /* Initializes the pop up. */

  private void initPopup()
  {
    listComponent.setBorder(BorderFactory.createEmptyBorder(0, 2, 5, 2));
    listComponent.setFocusable(false);

    popupMenu.setFocusable(false);
    popupMenu.add(listComponent);
  }

//------------------------------------------------------------------------------

  /* Initializes the listeners for the suggestions list. */

  private void initSuggestionComponentListener()
  {
    inputField.getDocument().addDocumentListener(new DocumentListener()
    {
      @Override
      public void removeUpdate(DocumentEvent e) {update(e);}

      @Override
      public void insertUpdate(DocumentEvent e) {update(e);}

      @Override
      public void changedUpdate(DocumentEvent e) {update(e);}

      private void update(DocumentEvent e)
      {
        if (state.mustDispatchDocumentEvents)
        {
          SwingUtilities.invokeLater(new Runnable()
          {
            @Override
            public void run()
            {
              List<String> suggestions = getSuggestions();
              if (!suggestions.isEmpty()) showPopup(suggestions);
              else                        popupMenu.setVisible(false);
            }
          });
        }
      }
    });
  }

//------------------------------------------------------------------------------

  private List<String> getSuggestions()
  {
    return filterSearchHistory.getPastSearchStringsMatching(inputField.getText());
  }

//------------------------------------------------------------------------------

  private void showPopup(List<String> suggestions)
  {
    listModel.clear();
    for (String suggestion : suggestions) listModel.addElement(suggestion);

    Point popupLocationRelativeToInputField = new Point(0, inputField.getPreferredSize().height);

    popupMenu.pack();
    listComponent.setSelectedIndex(0);
    popupMenu.show(inputField, (int) popupLocationRelativeToInputField.getX(),
                   (int) popupLocationRelativeToInputField.getY());
  }

//------------------------------------------------------------------------------

  /* Not using key inputMap because that would override the original handling. */

  private void initInvokerKeyListeners()
  {
    inputField.addKeyListener(new KeyAdapter()
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            if      (e.getKeyCode() == KeyEvent.VK_ENTER)  selectFromList(e);
            else if (e.getKeyCode() == KeyEvent.VK_UP)     moveUp(e);
            else if (e.getKeyCode() == KeyEvent.VK_DOWN)   moveDown(e);
            else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) popupMenu.setVisible(false);
        }
    });
  }

//------------------------------------------------------------------------------

  private void selectFromList(KeyEvent e)
  {
    if (popupMenu.isVisible())
    {
      int selectedIndex = listComponent.getSelectedIndex();
      if (selectedIndex != -1)
      {
        popupMenu.setVisible(false);
        String selectedValue = listComponent.getSelectedValue();
        state.mustDispatchDocumentEvents = false;
        inputField.setText(selectedValue);
        state.mustDispatchDocumentEvents = true;
        e.consume();
      }
    }
  }

//------------------------------------------------------------------------------

  private void moveUp(KeyEvent e)
  {
    if (popupMenu.isVisible() && listModel.getSize() > 0)
    {
      int selectedIndex = listComponent.getSelectedIndex();
      if (selectedIndex > 0)
      {
        listComponent.setSelectedIndex(selectedIndex - 1);
        e.consume();
      }
    }
  }

//------------------------------------------------------------------------------

  private void moveDown(KeyEvent e)
  {
    if (popupMenu.isVisible() && listModel.getSize() > 0)
    {
      int selectedIndex = listComponent.getSelectedIndex();
      if (selectedIndex < listModel.getSize())
      {
        listComponent.setSelectedIndex(selectedIndex + 1);
        e.consume();
      }
    }
  }

//------------------------------------------------------------------------------

  private static class State
  {
    boolean mustDispatchDocumentEvents = true;
  }
}
