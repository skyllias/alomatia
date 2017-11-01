
package org.skyllias.alomatia.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.prefs.*;

import javax.swing.*;

import org.apache.commons.lang.*;
import org.skyllias.alomatia.i18n.*;
import org.skyllias.alomatia.source.*;

/** Button with a text field tightly coupled to {@link SourceSelector} and
 *  {@link AsynchronousUrlSource} with three possible states: Disabled, ready
 *  and downloading.
 *  Errors are flagged with a tooltip over the URL field.
 *  It takes care of enabling, disabling and feeding the related field.
 *  This favours composition over inheritance and does not extend any Swing class.
 *  TODO Unit test. */

public class UrlDownloadComponent implements AsynchronousUrlSource.DownloadListener,
                                             SourceSelector.Enabable
{
  private static final String BUTTON_READY_LABEL   = "source.selector.url.button.ready";
  private static final String BUTTON_CANCEL_LABEL  = "source.selector.url.button.cancel";
  private static final String ERROR_TOOLTIP_PREFIX = "source.selector.url.field.tooltip.error.";

  private static final String PREFKEY_DEFAULTURL = "defaultSourceUrl";

  private AsynchronousUrlSource urlSource;
  private JButton button;
  private UrlTextField urlField;
  private boolean isDownloading = false;
  private LabelLocalizer labelLocalizer;

//==============================================================================

  public UrlDownloadComponent(LabelLocalizer localizer, AsynchronousUrlSource source)
  {
    labelLocalizer = localizer;
    urlSource       = source;

    button   = new JButton();
    urlField = new UrlTextField();

    setEnabled(false);                                                          // this sets the button label, see below

    String initialUrl = getPreferences().get(PREFKEY_DEFAULTURL, null);
    if (initialUrl != null) urlField.setText(initialUrl);

    Action urlAction = new UrlAction();
    button.addActionListener(urlAction);
    urlField.addActionListener(urlAction);
  }

//==============================================================================

  public JButton getButton() {return button;}

  public JTextField getTextField() {return urlField;}

//------------------------------------------------------------------------------

  @Override
  public void setEnabled(boolean active)
  {
    button.setEnabled(active);
    urlField.setEnabled(active);

    if (active)
    {
      String url = urlField.getText().trim();
      if (StringUtils.isNotBlank(url)) startDownload(url);
    }
    else cancelDownload();
  }

//------------------------------------------------------------------------------

  /* Clears any tooltip from the text field. */

  private void startDownload(String url)
  {
    button.setText(labelLocalizer.getString(BUTTON_CANCEL_LABEL));
    isDownloading = true;
    urlSource.setUrl(url, this);

    button.requestFocusInWindow();
    urlField.setToolTipText(null);
  }

//------------------------------------------------------------------------------

  private void cancelDownload()
  {
    button.setText(labelLocalizer.getString(BUTTON_READY_LABEL));
    isDownloading = false;
    urlSource.cancel();

    urlField.requestFocusInWindow();
  }

//------------------------------------------------------------------------------

  /** Changes the button label and the downloading status. */

  @Override
  public void onSuccess()
  {
    isDownloading = false;
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run() {button.setText(labelLocalizer.getString(BUTTON_READY_LABEL));}
    });
  }

//------------------------------------------------------------------------------

  /** Changes the button label and adds a tooltip to the text field with a
   *  description of what caused the requested image not to display. */

  @Override
  public void onError(final ErrorType type)
  {
    isDownloading = false;
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        button.setText(labelLocalizer.getString(BUTTON_READY_LABEL));

        if (type != null)
        {
          String errorKey = ERROR_TOOLTIP_PREFIX + type.toString().toLowerCase();
          urlField.setToolTipText(labelLocalizer.getString(errorKey));
        }
      }
    });
  }

//------------------------------------------------------------------------------

  /* Shortcut method to get preferences by subclasses that store the last URL. */

  private Preferences getPreferences() {return Preferences.userNodeForPackage(getClass());}

//------------------------------------------------------------------------------

//******************************************************************************

  /* Text field to write URLs in. */

  @SuppressWarnings("serial")
  private class UrlTextField extends JTextField
  {
    public UrlTextField()
    {
      setEditable(true);
      setMaximumSize(new Dimension(Integer.MAX_VALUE,
                                   getPreferredSize().height));                 // prevent the containing layout from streching the field vertically
    }
  }

//******************************************************************************

  /* Action executed when the button is clicked (either in the ready or the
   * downloading status) or when the enter is pressed in the urlField. */

  @SuppressWarnings("serial")
  private class UrlAction extends AbstractAction
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      if (isDownloading) cancelDownload();
      else
      {
        String url = urlField.getText().trim();
        startDownload(url);

        getPreferences().put(PREFKEY_DEFAULTURL, url);
      }
    }
  }
}
