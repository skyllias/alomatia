package org.skyllias.alomatia.ui.source;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.skyllias.alomatia.ImageSource;
import org.skyllias.alomatia.source.AsynchronousUrlSource;
import org.skyllias.alomatia.ui.UrlDownloadSubcomponentComposer;
import org.skyllias.alomatia.ui.UrlDownloadSubcomponentComposer.UrlDownloadSubcomponent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/** Composer for a selector of {@link AsynchronousUrlSource}.
 *  TODO: Refactor when the URL download subcomponent is modified (most
 *  probably, that class will disappear and the controls will come here). */

@Component
@Order(4)
public class UrlDownloadSourceSelectionComposer implements SourceSelectionComposer
{
  private static final String SOURCE_KEY = "url";

  private final AsynchronousUrlSource urlSource;
  private final UrlDownloadSubcomponentComposer urlDownloadSubcomponentComposer;

//==============================================================================

  public UrlDownloadSourceSelectionComposer(AsynchronousUrlSource urlSource,
                                            UrlDownloadSubcomponentComposer urlDownloadSubcomponentComposer)
  {
    this.urlSource = urlSource;
    this.urlDownloadSubcomponentComposer = urlDownloadSubcomponentComposer;
  }

//==============================================================================

  @Override
  public SourceSelection buildSelector()
  {
    return new UrlSourceSelection(urlDownloadSubcomponentComposer.getUrlDownloadSubcomponent(urlSource));
  }

//------------------------------------------------------------------------------

  @Override
  public String getSourceKey() {return SOURCE_KEY;}

//------------------------------------------------------------------------------

//******************************************************************************

  private class UrlSourceSelection implements SourceSelection
  {
    private final UrlDownloadSubcomponent urlDownloadSubcomponent;
    private final JPanel controlsPanel;

    public UrlSourceSelection(UrlDownloadSubcomponent urlDownloadSubcomponent)
    {
      this.urlDownloadSubcomponent = urlDownloadSubcomponent;

      controlsPanel = new JPanel();
      controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.X_AXIS));
      controlsPanel.add(urlDownloadSubcomponent.getTextField());
      controlsPanel.add(urlDownloadSubcomponent.getButton());
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
          urlDownloadSubcomponent.setEnabled(active);
        }
      };
    }

    @Override
    public JComponent getControls() {return controlsPanel;}
  }

//******************************************************************************

}
