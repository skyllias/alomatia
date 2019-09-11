
package org.skyllias.alomatia.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import org.skyllias.alomatia.i18n.LabelLocalizer;

/** Composer of a panel to display the current language and to select the next one. */

public class LanguagePanelComposer
{
  private static final String TITLE_LABEL    = "language.selector.title";
  private static final String NEXTLANG_LABEL = "language.selector.next";

  protected static final String LANG_SELECTOR_NAME = "language.selector";       // name for the combobox

  private LabelLocalizer labelLocalizer;

//==============================================================================

  /** Creates a new panel with the components to display and change the language. */

  protected LanguagePanelComposer(LabelLocalizer localizer)
  {
    labelLocalizer = localizer;
  }

//==============================================================================

  /** Returns a new panel with the required controls */

  public JComponent getComponent()
  {
    JPanel panel = new BasicControlPanelComposer().getPanel(labelLocalizer.getString(TITLE_LABEL));

    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

    Vector<Locale> availableLocales = new Vector<>(labelLocalizer.getAvailableLocales());
    Collections.sort(availableLocales, new LocaleComparator());

    JLabel nextLangDescLabel       = new BorderedLabel(labelLocalizer.getString(NEXTLANG_LABEL));
    JComboBox<Locale> langSelector = new JComboBox<Locale>(availableLocales);
    langSelector.setName(LANG_SELECTOR_NAME);
    langSelector.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                              langSelector.getPreferredSize().height));   // prevent it from stretching vertically
    langSelector.setSelectedItem(labelLocalizer.getCurrentLocale());            // do this before adding the listener to avoid redundant events
    langSelector.setRenderer(new LocaleCellRenderer());
    langSelector.addActionListener(new LocaleChangeListener());
    panel.add(nextLangDescLabel);
    panel.add(langSelector);

    return panel;
  }

//------------------------------------------------------------------------------

  /* Returns the name of the language of the locale in the locale's language
   * itself and the current language. */

  private String getLanguageDisplayName(Locale locale)
  {
    final String NAME_PATTERN = "{0} ({1})";                                    // TODO pattern i18n

    Locale currentLocale       = labelLocalizer.getCurrentLocale();
    String nameInSameLocale    = locale.getDisplayLanguage(locale);
    String nameInCurrentLocale = locale.getDisplayLanguage(currentLocale);

    return new MessageFormat(NAME_PATTERN).format(new Object[] {nameInSameLocale,
                                                                nameInCurrentLocale});
  }

//------------------------------------------------------------------------------

//******************************************************************************

  /** Comparator of Locales that uses their display name to sort them. */

  private class LocaleComparator implements Comparator<Locale>
  {
    @Override
    public int compare(Locale locale1, Locale locale2)
    {
      String displayName1 = getLanguageDisplayName(locale1);
      String displayName2 = getLanguageDisplayName(locale2);
      return displayName1.compareTo(displayName2);
    }
  }

//******************************************************************************

  /* Renderer of locales according to the language display name method above
   * rather than the default toString() implementation.
   * The current Locale is flagged with some extra formatting. */

  private class LocaleCellRenderer implements ListCellRenderer<Locale>
  {
    private BasicComboBoxRenderer originalRenderer = new BasicComboBoxRenderer();

    @Override
    public Component getListCellRendererComponent(JList<? extends Locale> list, Locale value,
                                                  int index, boolean isSelected, boolean cellHasFocus)
    {
      final String CURRENT_FLAG_PATTERN = "{0} (*)";                            // TODO pattern i18n

      String displayName = getLanguageDisplayName(value);
      if (value.equals(labelLocalizer.getCurrentLocale()))
      {
        MessageFormat format = new MessageFormat(CURRENT_FLAG_PATTERN);
        displayName          = format.format(new Object[] {displayName});
      }
      return originalRenderer.getListCellRendererComponent(list, displayName, index,
                                                           isSelected, cellHasFocus);
    }
  }

//******************************************************************************

  /* Listener of changes in the language selector that sets the selected locale
   * to the LabelLocalizer. */

  private class LocaleChangeListener implements ActionListener
  {
    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(ActionEvent event)
    {
      JComboBox<Locale> langSelector = (JComboBox<Locale>) event.getSource();
      Locale nextLocale              = (Locale) langSelector.getSelectedItem();
      labelLocalizer.setLocale(nextLocale);
    }
  }

}
