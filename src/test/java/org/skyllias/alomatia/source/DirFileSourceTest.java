
package org.skyllias.alomatia.source;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.skyllias.alomatia.test.matchers.AlomatiaMatchers.sameImage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.ImageDisplay;

@RunWith(MockitoJUnitRunner.class)
public class DirFileSourceTest
{
  @Mock
  private ImageDisplay imageDisplay;

  @InjectMocks
  private DirFileSource dirFileSource;

  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();


  @Test
  public void shouldDoNothingWhenInactive() throws Exception
  {
    createImageFiles(5);

    dirFileSource.setActive(false);
    dirFileSource.setFileSource(tmpFolder.getRoot());

    verify(imageDisplay, never()).setOriginalImage(any());
  }

  @Test
  public void shouldDoNothingWhenNoDirSet() throws Exception
  {
    dirFileSource.setActive(true);

    verify(imageDisplay, never()).setOriginalImage(any());
  }

  @Test
  public void shouldSetFirstImageWhenActive() throws Exception
  {
    createImageFiles(5);

    dirFileSource.setActive(true);
    dirFileSource.setFileSource(tmpFolder.getRoot());

    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage(0))));
  }

  @Test
  public void shouldSetSecondImageWhenNext() throws Exception
  {
    createImageFiles(5);

    dirFileSource.setActive(true);
    dirFileSource.setFileSource(tmpFolder.getRoot());
    dirFileSource.nextImageFile();
    dirFileSource.nextImageFile();

    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage(0))));
    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage(1))));
    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage(2))));
    verify(imageDisplay, never()).setOriginalImage(argThat(sameImage(buildInputImage(3))));
    verify(imageDisplay, never()).setOriginalImage(argThat(sameImage(buildInputImage(4))));
  }

  @Test
  public void shouldSetFirstImageAgainWhenNextAndPrevious() throws Exception
  {
    createImageFiles(4);

    dirFileSource.setActive(true);
    dirFileSource.setFileSource(tmpFolder.getRoot());
    dirFileSource.nextImageFile();
    dirFileSource.previousImageFile();

    verify(imageDisplay, times(2)).setOriginalImage(argThat(sameImage(buildInputImage(0))));
    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage(1))));
    verify(imageDisplay, never()).setOriginalImage(argThat(sameImage(buildInputImage(2))));
    verify(imageDisplay, never()).setOriginalImage(argThat(sameImage(buildInputImage(3))));
  }

  @Test
  public void shouldSetLastImageWhenFirstAndPrevious() throws Exception
  {
    createImageFiles(3);

    dirFileSource.setActive(true);
    dirFileSource.setFileSource(tmpFolder.getRoot());
    dirFileSource.previousImageFile();

    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage(0))));
    verify(imageDisplay, never()).setOriginalImage(argThat(sameImage(buildInputImage(1))));
    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage(2))));
  }

  @Test
  public void shouldSetFirstImageWhenLastAndNext() throws Exception
  {
    createImageFiles(2);

    dirFileSource.setActive(true);
    dirFileSource.setFileSource(tmpFolder.getRoot());
    dirFileSource.nextImageFile();
    dirFileSource.nextImageFile();

    verify(imageDisplay, times(2)).setOriginalImage(argThat(sameImage(buildInputImage(0))));
    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage(1))));
  }

  @Test
  public void shouldNotSetImageWhenWrongImage() throws Exception
  {
    File wrongImageFile = tmpFolder.newFile("wrong.png");
    FileUtils.writeStringToFile(wrongImageFile, "Not an image", StandardCharsets.UTF_8);

    dirFileSource.setActive(true);
    dirFileSource.setFileSource(tmpFolder.getRoot());

    verify(imageDisplay, never()).setOriginalImage(any());
  }


  private void createImageFiles(int amount) throws IOException
  {
    createImageFiles(amount, true);
  }

  private void createImageFiles(int amount, boolean withExtension) throws IOException
  {
    for (int i = 0; i < amount; i++)
    {
      String filename = i + (withExtension ? ".png" : ".txt");
      File imageFile  = tmpFolder.newFile(filename);

      ImageIO.write(buildInputImage(i), "PNG", imageFile);
    }

  }

  private BufferedImage buildInputImage(int index)
  {
    BufferedImage inputImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    inputImage.setRGB(0, 0, new Color(index, index, index).getRGB());
    return inputImage;
  }

}
