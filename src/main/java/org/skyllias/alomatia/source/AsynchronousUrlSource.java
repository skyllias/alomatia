
package org.skyllias.alomatia.source;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.ImageSource;
import org.springframework.stereotype.Component;

/** ImageSource that retrieves the image from a URL.
 *  <p>
 *  To prevent blocking the interface, the connection is asynchronous. The inner
 *  interface {@link DownloadListener} can be implemented in order to be
 *  notified when the download completes, either successfully or not.
 *  <p>
 *  This is meant for "easily" accessible resources only. If proxy configuration,
 *  authentication, SSL validation, new protocols support, timeout control or
 *  other features are needed, other sources like screen or disk (after download)
 *  should be considered instead.
 *  <p>
 *  HTTP/HTTPS will be the most common protocol, but this class does not enforce it.
 *  <p>
 *  A reactivation is passive in the sense that the previous download, if any,
 *  is not reattempted. */

@Component
public class AsynchronousUrlSource implements ImageSource
{
  private final ImageDisplay imageDisplay;
  private final Executor executor = Executors.newCachedThreadPool();

  private Downloader currentDownload;

//==============================================================================

  public AsynchronousUrlSource(ImageDisplay imageDisplay)
  {
    this.imageDisplay = imageDisplay;
  }

//==============================================================================

  /** Stops the download, if any. */

  @Override
  public void setActive(boolean active)
  {
    if (!active) cancel();
  }

//------------------------------------------------------------------------------

  /** Begins the download of the image located at url, returning immediately but
   *  notifying listener (if not null) when finished.
   *  Only one download is possible at at time, so if the previous one has not
   *  finished yet, it is cancelled. */

  public void setUrl(String url, DownloadListener listener)
  {
    cancel(true);

    currentDownload = new Downloader(url, imageDisplay, listener);
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

//******************************************************************************

  /* Opens a connection to url, retrieves its contents and passes the
   * image to the display. Invokes onSuccess or onError of currentListener, if
   * not null.
   * The cancel status is checked after openning the connection, after each
   * buffer of bytes is read, and after the download completes.
   * The contents of the URL are stored in a temporary file, which is
   * automatically deleted. If the contents are huge, probably a memory problem
   * will rise anyway, but ImageIO may be smart about it internally. */

  private static class Downloader implements Runnable
  {
    private static final int BUFFER_SIZE         = 4_096;
    private static final String TEMP_FILE_PREFIX = "alomatia";
    private static final Duration TIMEOUT        = Duration.ofSeconds(5);

    private final String url;
    private final ImageDisplay imageDisplay;

    private DownloadListener listener;
    private boolean cancelled = false;

    public Downloader(String url, ImageDisplay imageDisplay,
                      DownloadListener listener)
    {
      this.url          = url;
      this.imageDisplay = imageDisplay;
      this.listener     = listener;
    }

    @Override
    public void run()
    {
      DownloadListener.ErrorType error = null;
      try                                                                       // TODO log exceptions
      {
        URLConnection connection = establishConnection();

        if (cancelled) error = DownloadListener.ErrorType.CANCEL;
        else
        {
          Optional<Image> image = readImageWhileNotCancelled(connection);

          if (cancelled) error = DownloadListener.ErrorType.CANCEL;
          else
          {
            if (image.isPresent()) imageDisplay.setOriginalImage(image.get());
            else                   error = DownloadListener.ErrorType.IMAGE;
          }
        }
      }
      catch (SocketTimeoutException ste) {error = DownloadListener.ErrorType.TIMEOUT;}
      catch (MalformedURLException mue)  {error = DownloadListener.ErrorType.URL;}
      catch (IOException ioe)            {error = DownloadListener.ErrorType.CONNECTION;}
      catch (Exception e)                {error = DownloadListener.ErrorType.OTHER;}

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

    private URLConnection establishConnection() throws MalformedURLException, IOException
    {
      URLConnection connection = new URL(url).openConnection();
      connection.setConnectTimeout((int) TIMEOUT.toMillis());
      connection.setReadTimeout((int) TIMEOUT.toMillis());

      connection.connect();

      return connection;
    }

    private Optional<Image> readImageWhileNotCancelled(URLConnection connection) throws IOException
    {
      File tempFile = File.createTempFile(TEMP_FILE_PREFIX, null);
      try
      {
        try (OutputStream tempFileStream = new FileOutputStream(tempFile))
        {
          copyWhileNotCancelled(connection, tempFileStream);
        }

        if (cancelled) return Optional.empty();
        else           return Optional.ofNullable(ImageIO.read(tempFile));
      }
      finally {FileUtils.deleteQuietly(tempFile);}
    }

    private void copyWhileNotCancelled(URLConnection connection,
                                       OutputStream destinationStream) throws IOException
    {
      try (InputStream resourceStream = connection.getInputStream())
      {
        byte[] buffer = new byte[BUFFER_SIZE];
        int amountRead;
        while (!cancelled && (amountRead = resourceStream.read(buffer)) > 0)
        {
          destinationStream.write(buffer, 0, amountRead);
        }
      }
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
       *  protocol, etc. */

      CONNECTION,

      /** The download could not be completed due to some timeout, either when
       *  establishing the connection or when reading the data. */

      TIMEOUT,

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
