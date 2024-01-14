package org.skyllias.alomatia.ui.source;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.lang.StringUtils;
import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.i18n.LabelLocalizer;
import org.skyllias.alomatia.preferences.SourceDownloadPreferences;
import org.skyllias.alomatia.source.AsynchronousUrlSource;
import org.skyllias.alomatia.source.AsynchronousUrlSource.DownloadListener;
import org.skyllias.alomatia.ui.component.PathTextField;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Composer for a selector of {@link AsynchronousUrlSource}. */

@Component
@Order(4)
public class UrlDownloadSourceSelectionComposer implements SourceSelectionComposer
{
  protected static final String FIELD_NAME  = "field.url";
  protected static final String BUTTON_NAME = "button.download";

  private static final String SOURCE_KEY = "url";

  private static final String BUTTON_READY_LABEL   = "source.selector.url.button.ready";
  private static final String BUTTON_CANCEL_LABEL  = "source.selector.url.button.cancel";
  private static final String ERROR_TOOLTIP_PREFIX = "source.selector.url.field.tooltip.error.";

  private final AsynchronousUrlSource urlSource;
  private final LabelLocalizer labelLocalizer;
  private final SourceDownloadPreferences preferences;

//==============================================================================

  public UrlDownloadSourceSelectionComposer(AsynchronousUrlSource urlSource,
                                            LabelLocalizer labelLocalizer,
                                            SourceDownloadPreferences preferences)
  {
    this.urlSource      = urlSource;
    this.labelLocalizer = labelLocalizer;
    this.preferences    = preferences;
  }

//==============================================================================

  @Override
  public SourceSelection buildSelector()
  {
    return new UrlSourceSelection();
  }

//------------------------------------------------------------------------------

  @Override
  public String getSourceKey() {return SOURCE_KEY;}

//------------------------------------------------------------------------------

//******************************************************************************

  private class UrlSourceSelection implements SourceSelection, DownloadListener
  {
    private final UrlTextField urlField = new UrlTextField();
    private final JButton button        = new JButton();
    private final JPanel controlsPanel;

    private final State state = new State();

    public UrlSourceSelection()
    {
      controlsPanel = new JPanel();
      controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));
      controlsPanel.add(urlField);
      controlsPanel.add(button);

      enableComponents(false);
      urlField.setName(FIELD_NAME);
      button.setName(BUTTON_NAME);
      button.setText(labelLocalizer.getString(BUTTON_READY_LABEL));

      String initialUrl = preferences.getLastUrl();
      if (initialUrl != null) urlField.setText(initialUrl);

      Action urlAction = new UrlAction(this);
      button.addActionListener(urlAction);
      urlField.addActionListener(urlAction);                                    // a JTextField action listener is automatically fired when the enter key is pressed
    }

    @Override
    public ImageSource getSource()
    {
      return new ImageSource()
      {
        @Override
        public void setActive(boolean active)
        {
          urlSource.setActive(active);
          setEnabled(active);
        }
      };
    }

    @Override
    public JComponent getControls() {return controlsPanel;}

    public void setEnabled(boolean active)
    {
      enableComponents(active);

      if (active) startDownload();
      else        cancelDownload();
    }

    /** Changes the button label and the downloading status. */

    @Override
    public void onSuccess()
    {
      state.isDownloading = false;
      SwingUtilities.invokeLater(new Runnable()
      {
        @Override
        public void run() {button.setText(labelLocalizer.getString(BUTTON_READY_LABEL));}
      });
    }

    /** Changes the button label and adds a tooltip to the text field with a
     *  description of what caused the requested image not to display. */

    @Override
    public void onError(final ErrorType type)
    {
      state.isDownloading = false;
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

    /* Changes the enable status of the button and field. */

    private void enableComponents(boolean active)
    {
      button.setEnabled(active);
      urlField.setEnabled(active);
    }

    /* Clears any tooltip from the text field. */

    private void startDownload()
    {
      String url = urlField.getText().trim();

      if (StringUtils.isNotBlank(url))
      {
        preferences.setLastUrl(url);

        button.setText(labelLocalizer.getString(BUTTON_CANCEL_LABEL));
        state.isDownloading = true;
        urlSource.setUrl(url, this);

        button.requestFocusInWindow();
        urlField.setToolTipText(null);
      }
    }

    private void cancelDownload()
    {
      button.setText(labelLocalizer.getString(BUTTON_READY_LABEL));
      state.isDownloading = false;
      urlSource.cancel();

      urlField.requestFocusInWindow();                                            // this will not have any effect when this method is called from setEnabled(false)
    }

  }

//******************************************************************************

  /* Action executed when the button is clicked (either in the ready or the
   * downloading status) or when the enter is pressed in the urlField. */

  @SuppressWarnings("serial")
  private class UrlAction extends AbstractAction
  {
    private final UrlSourceSelection urlSourceSelection;


    public UrlAction(UrlSourceSelection urlSourceSelection)
    {
      this.urlSourceSelection = urlSourceSelection;
    }


    @Override
    public void actionPerformed(ActionEvent e)
    {
      if (urlSourceSelection.state.isDownloading) urlSourceSelection.cancelDownload();
      else                                        urlSourceSelection.startDownload();
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

  private static class State
  {
    boolean isDownloading = false;
  }

//******************************************************************************

}
