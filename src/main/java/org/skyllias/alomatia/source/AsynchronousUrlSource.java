
package org.skyllias.alomatia.source;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

/** ImageSource that retrieves the image from a URL.
 *  <p>
 *  To prevent blocking the interface, the connection is asynchronous. However,
 *  it does not support full cancellation due to the way blocking IO operations
 *  work. For that, heavier libraries (like Apache's HttpClient) should be
 *  considered.
 *  <p>
 *  This is meant for "easily" accessible resources only. If proxy configuration,
 *  authentication, SSL validation, new protocols support, timeout control or
 *  other features are needed, different sources should be considered.
 *  <p>
 *  HTTP/HTTPS will be the most common protocol, but this class does not enforce it.
 *  <p>
 *  A reactivation is passive in the sense that the previous download, if any,
 *  is not reattempted.
 *  <p>
 *  The inner interface DownloadListener can be implemented in order to be
 *  notified when the download completes, either successfully or not. */

public class AsynchronousUrlSource extends BasicSource
{
  private Downloader currentDownload;
  private Executor executor = Executors.newCachedThreadPool();

//==============================================================================

  /** Begins the download of the image located at url, returning immediately but
   *  notifying listener (if not null) when finished.
   *  Only one download is possible at at time, so if the previous one has not
   *  finished yet, it is cancelled. */

  public void setUrl(String url, DownloadListener listener)
  {
    cancel(true);

    currentDownload = new Downloader(url, listener);
    executor.execute(currentDownload);
  }

//------------------------------------------------------------------------------

  /** If the current download has not finished, when it reaches an interruptable
   *  point it will finish its work without sending the image to the display.
   *  Its listener is notified. */

  public void cancel() {cancel(false);}

//------------------------------------------------------------------------------

  /* If the current download has not finished, when it reaches an interruptable
   * point it will finish its work without sending the image to the display.
   * Its listener is notified only if not silently. */

  private void cancel(boolean silently)
  {
    if (currentDownload != null) currentDownload.cancel(silently);
  }

//------------------------------------------------------------------------------

  /** Modifies the executor used to run the download tasks.
   *  This will be used seldom, but anyway here it is. */

  public void setExecutor(Executor newExecutor) {executor = newExecutor;}

//------------------------------------------------------------------------------

//******************************************************************************

  /* Opens a connection to currentUrl, retrieves its contents and passes the
   * image to the display. Invokes onSuccess or onError of currentListener, if
   * not null.
   * The cancel status is checked after openning the connection and after
   * downloading the contents. */

  private class Downloader implements Runnable
  {
    private String url;
    private DownloadListener listener;
    private boolean cancelled = false;

    public Downloader(String url, DownloadListener listener)
    {
      this.url      = url;
      this.listener = listener;
    }

    @Override
    public void run()
    {
      DownloadListener.ErrorType error = null;
      InputStream resourceStream       = null;
      try                                                                       // TODO log exceptions
      {
        URLConnection connection = new URL(url).openConnection();
        connection.connect();

        if (cancelled) error = DownloadListener.ErrorType.CANCEL;
        else
        {
          resourceStream      = connection.getInputStream();
          BufferedImage image = ImageIO.read(resourceStream);
          if (cancelled) error = DownloadListener.ErrorType.CANCEL;
          else
          {
            if (image == null) error = DownloadListener.ErrorType.IMAGE;
            else               sendImageToDisplay(image);
          }
        }
      }
      catch (MalformedURLException mue) {error = DownloadListener.ErrorType.URL;}
      catch (IOException ioe)           {error = DownloadListener.ErrorType.CONNECTION;}
      catch (Exception e)               {error = DownloadListener.ErrorType.OTHER;}
      finally {IOUtils.closeQuietly(resourceStream);}

      if (listener != null)
      {
        if (error == null) listener.onSuccess();
        else               listener.onError(error);
      }
    }

    public void cancel(boolean silently)
    {
      cancelled = true;
      if (silently) listener = null;
    }
  }

//******************************************************************************

  /** Listener invoked when the connection completes.
   *  Both methods will be called from a thread different from the invoker's. */

  public static interface DownloadListener
  {
    /** Potential errors notified when the image cannot be retrieved from the URL. */

    public enum ErrorType
    {
      /** The URL does not have a proper format. */

      URL,

      /** The network connection could not be established, for example due to
       *  firewalls, unavailable network interface, unknown host, unsupported
       *  protocol, timeout, etc. */

      CONNECTION,

      /** The resource could be downloaded but it cannot be read as an image. */

      IMAGE,

      /** The download has been cancelled. */

      CANCEL,

      /** Undetermined exception. */

      OTHER,
    }

    /** Method invoked when the image is successfully read and passed to the display. */

    void onSuccess();

    /** Method invoked when the image cannot be read.
     *  More details could be provided, but for the time being this is enough. */

    void onError(ErrorType type);
  }
}
