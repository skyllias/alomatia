
package org.skyllias.alomatia.source;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.skyllias.alomatia.test.matchers.AlomatiaMatchers.sameImage;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.ImageDisplay;
import org.skyllias.alomatia.source.AsynchronousUrlSource.DownloadListener;
import org.skyllias.alomatia.source.AsynchronousUrlSource.DownloadListener.ErrorType;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;

@RunWith(MockitoJUnitRunner.class)
public class AsynchronousUrlSourceTest
{
  @Mock
  private ImageDisplay imageDisplay;

  @InjectMocks
  private AsynchronousUrlSource asynchronousUrlSource;

  @Test
  public void shouldDisplayImageWhenNormalResponse() throws Exception
  {
    try (MockWebServer server = new MockWebServer())
    {
      server.start();

      server.setDispatcher(new Dispatcher()
      {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException
        {
          return new MockResponse()
              .setBody(new Buffer().write(buildImageBytes()));
        }
      });
      String imageUrl = server.url("/image.png").toString();

      DownloadListener downloadListener = mock(DownloadListener.class);
      asynchronousUrlSource.setUrl(imageUrl, downloadListener);
      asynchronousUrlSource.awaitTermination();

      verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage())));
      verify(downloadListener).onSuccess();
    }
  }

  @Test
  public void shouldTrackErrorWithWrongUrl() throws Exception
  {
    DownloadListener downloadListener = mock(DownloadListener.class);
    asynchronousUrlSource.setUrl("invalid URL", downloadListener);
    asynchronousUrlSource.awaitTermination();

    verify(imageDisplay, never()).setOriginalImage(any());
    verify(downloadListener).onError(ErrorType.URL);
  }

  @Test
  public void shouldTrackErrorWithConnectionError() throws Exception
  {
    DownloadListener downloadListener = mock(DownloadListener.class);
    asynchronousUrlSource.setUrl("http://example.com/image.png", downloadListener);
    asynchronousUrlSource.awaitTermination();

    verify(imageDisplay, never()).setOriginalImage(any());
    verify(downloadListener).onError(ErrorType.CONNECTION);
  }

  @Test
  public void shouldTrackErrorWith404() throws Exception
  {
    try (MockWebServer server = new MockWebServer())
    {
      server.start();
      server.setDispatcher(new Dispatcher()
      {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException
        {
          return new MockResponse().setResponseCode(404);
        }
      });
      String imageUrl = server.url("/image.png").toString();

      DownloadListener downloadListener = mock(DownloadListener.class);
      asynchronousUrlSource.setUrl(imageUrl, downloadListener);
      asynchronousUrlSource.awaitTermination();

      verify(imageDisplay, never()).setOriginalImage(any());
      verify(downloadListener).onError(ErrorType.CONNECTION);
    }
  }

  @Test
  public void shouldTrackTimeout() throws Exception
  {
    DownloadListener downloadListener = mock(DownloadListener.class);

    try (MockWebServer server = new MockWebServer())
    {
      server.start();
      server.setDispatcher(new Dispatcher()
      {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException
        {
          return new MockResponse()
              .setBodyDelay(10, TimeUnit.SECONDS)
              .setBody(new Buffer().write(buildImageBytes()));
        }
      });
      String imageUrl = server.url("/image.png").toString();

      asynchronousUrlSource.setUrl(imageUrl, downloadListener);
      asynchronousUrlSource.awaitTermination();
    }
    catch (IOException e) {}

    verify(imageDisplay, never()).setOriginalImage(any());
    verify(downloadListener).onError(ErrorType.TIMEOUT);
  }

  @Test
  public void shouldDisplayImageWhenChunkedResponse() throws Exception
  {
    try (MockWebServer server = new MockWebServer())
    {
      server.start();

      server.setDispatcher(new Dispatcher()
      {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException
        {
          return new MockResponse()
              .setChunkedBody(new Buffer().write(buildImageBytes()), 1_024);
        }
      });
      String imageUrl = server.url("/image.png").toString();

      DownloadListener downloadListener = mock(DownloadListener.class);
      asynchronousUrlSource.setUrl(imageUrl, downloadListener);
      asynchronousUrlSource.awaitTermination();

      verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage())));
      verify(downloadListener).onSuccess();
    }
  }

  @Test
  public void shouldDisplayImageWhenSlowChunkedResponse() throws Exception
  {
    try (MockWebServer server = new MockWebServer())
    {
      server.start();

      server.setDispatcher(new Dispatcher()
      {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException
        {
          return new MockResponse()
              .throttleBody(4_096, 500, TimeUnit.MILLISECONDS)
              .setChunkedBody(new Buffer().write(buildImageBytes()), 1_024);
        }
      });
      String imageUrl = server.url("/image.png").toString();

      DownloadListener downloadListener = mock(DownloadListener.class);
      asynchronousUrlSource.setUrl(imageUrl, downloadListener);
      asynchronousUrlSource.awaitTermination();

      verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage())));
      verify(downloadListener).onSuccess();
    }
  }

  @Test
  public void shouldCancelInMiddleOfDownload() throws Exception
  {
    try (MockWebServer server = new MockWebServer())
    {
      server.start();

      server.setDispatcher(new Dispatcher()
      {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException
        {
          return new MockResponse()
              .throttleBody(4_096, 500, TimeUnit.MILLISECONDS)
              .setChunkedBody(new Buffer().write(buildImageBytes()), 1_024);
        }
      });
      String imageUrl = server.url("/image.png").toString();

      DownloadListener downloadListener = mock(DownloadListener.class);
      asynchronousUrlSource.setUrl(imageUrl, downloadListener);
      Thread.sleep(2_000);
      asynchronousUrlSource.cancel();
      asynchronousUrlSource.awaitTermination();

      verify(imageDisplay, never()).setOriginalImage(any());
      verify(downloadListener).onError(ErrorType.CANCEL);
    }
  }

  @Test
  public void shouldTrackImageFormatError() throws Exception
  {
    try (MockWebServer server = new MockWebServer())
    {
      server.start();

      server.setDispatcher(new Dispatcher()
      {
        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException
        {
          return new MockResponse()
              .setBody("not an image");
        }
      });
      String imageUrl = server.url("/image.png").toString();

      DownloadListener downloadListener = mock(DownloadListener.class);
      asynchronousUrlSource.setUrl(imageUrl, downloadListener);
      asynchronousUrlSource.awaitTermination();

      verify(imageDisplay, never()).setOriginalImage(any());
      verify(downloadListener).onError(ErrorType.IMAGE);
    }
  }


  private byte[] buildImageBytes()
  {
    try
    {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      ImageIO.write(buildInputImage(), "png", buffer);
      return buffer.toByteArray();
    }
    catch (Exception e) {throw new RuntimeException(e);}
  }

  private BufferedImage buildInputImage()
  {
    final String IMAGE_PATH = "/sources/streaks.png";

    try (InputStream imageStream = getClass().getResourceAsStream(IMAGE_PATH))
    {
      return ImageIO.read(imageStream);
    }
    catch (IOException e) {throw new RuntimeException(e);}
  }

}
