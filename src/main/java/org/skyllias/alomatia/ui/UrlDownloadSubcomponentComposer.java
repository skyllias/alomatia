
package org.skyllias.alomatia.ui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.DownloadPreferences;
import org.skyllias.alomatia.source.AsynchronousUrlSource;
import org.skyllias.alomatia.ui.component.PathTextField;
import org.springframework.stereotype.Component;

/** Composer of a button with a text field tightly coupled to
 *  {@link SourceSelector} and {@link AsynchronousUrlSource} with three possible
 *  states: Disabled, ready and downloading.
 *  Errors are flagged with a tooltip over the URL field.
 *  It takes care of enabling, disabling and feeding the related field. */

@Component
public class UrlDownloadSubcomponentComposer
{
  protected static final String BUTTON_READY_LABEL   = "source.selector.url.button.ready";
  protected static final String BUTTON_CANCEL_LABEL  = "source.selector.url.button.cancel";
  protected static final String ERROR_TOOLTIP_PREFIX = "source.selector.url.field.tooltip.error.";

  private final LabelLocalizer labelLocalizer;
  private final DownloadPreferences downloadPreferences;

//==============================================================================

  public UrlDownloadSubcomponentComposer(LabelLocalizer localizer,
                                         DownloadPreferences preferences)
  {
    labelLocalizer      = localizer;
    downloadPreferences = preferences;
  }

//==============================================================================

  public UrlDownloadSubcomponent getUrlDownloadSubcomponent(AsynchronousUrlSource source)
  {
    return new UrlDownloadSubcomponent(source, labelLocalizer, downloadPreferences);
  }

//------------------------------------------------------------------------------

//******************************************************************************

  public static class UrlDownloadSubcomponent implements AsynchronousUrlSource.DownloadListener
  {
    private final AsynchronousUrlSource urlSource;
    private final LabelLocalizer labelLocalizer;

    private final JButton button        = new JButton();
    private final UrlTextField urlField = new UrlTextField();

    private boolean isDownloading = false;

//==============================================================================

    public UrlDownloadSubcomponent(AsynchronousUrlSource urlSource,
                                   LabelLocalizer labelLocalizer,
                                   DownloadPreferences preferences)
    {
      this.urlSource      = urlSource;
      this.labelLocalizer = labelLocalizer;

      enableComponents(false);
      button.setText(labelLocalizer.getString(BUTTON_READY_LABEL));

      String initialUrl = preferences.getLastUrl();
      if (initialUrl != null) urlField.setText(initialUrl);

      Action urlAction = new UrlAction(this, preferences);
      button.addActionListener(urlAction);
      urlField.addActionListener(urlAction);                                    // a JTextField action listener is automatically fired when the enter key is pressed
    }

//==============================================================================

    public JButton getButton() {return button;}

    public JTextField getTextField() {return urlField;}

//------------------------------------------------------------------------------

    public void setEnabled(boolean active)
    {
      enableComponents(active);

      if (active) startDownload(getUrl());
      else        cancelDownload();
    }

//------------------------------------------------------------------------------

    /* Changes the enable status of the button and field. */

    private void enableComponents(boolean active)
    {
      button.setEnabled(active);
      urlField.setEnabled(active);
    }

//------------------------------------------------------------------------------

    /* Clears any tooltip from the text field. */

    private void startDownload(String url)
    {
      if (StringUtils.isNotBlank(url))
      {
        button.setText(labelLocalizer.getString(BUTTON_CANCEL_LABEL));
        isDownloading = true;
        urlSource.setUrl(url, this);

        button.requestFocusInWindow();
        urlField.setToolTipText(null);
      }
    }

//------------------------------------------------------------------------------

    private void cancelDownload()
    {
      button.setText(labelLocalizer.getString(BUTTON_READY_LABEL));
      isDownloading = false;
      urlSource.cancel();

      urlField.requestFocusInWindow();                                            // this will not have any effect when this method is called from setEnabled(false)
    }

//------------------------------------------------------------------------------

    /* Returns the contents of the URL field. */

//------------------------------------------------------------------------------

    private String getUrl() {return urlField.getText().trim();}

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
  }

//******************************************************************************

  /* Text field to write URLs in. */

  @SuppressWarnings("serial")
  private static class UrlTextField extends PathTextField
  {
    public UrlTextField()
    {
      setEditable(true);
    }
  }

//******************************************************************************

  /* Action executed when the button is clicked (either in the ready or the
   * downloading status) or when the enter is pressed in the urlField. */

  @SuppressWarnings("serial")
  private static class UrlAction extends AbstractAction
  {
    private final UrlDownloadSubcomponent urlDownloadSubcomponent;
    private final DownloadPreferences preferences;


    public UrlAction(UrlDownloadSubcomponent urlDownloadSubcomponent,
                     DownloadPreferences preferences)
    {
      this.urlDownloadSubcomponent = urlDownloadSubcomponent;
      this.preferences             = preferences;
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
      if (urlDownloadSubcomponent.isDownloading) urlDownloadSubcomponent.cancelDownload();
      else
      {
        String url = urlDownloadSubcomponent.getUrl();
        urlDownloadSubcomponent.startDownload(url);

        preferences.setLastUrl(url);
      }
    }
  }
 }
