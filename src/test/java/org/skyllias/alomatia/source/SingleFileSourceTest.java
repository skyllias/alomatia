
package org.skyllias.alomatia.source;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.skyllias.alomatia.test.matchers.AlomatiaMatchers.sameImage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.ImageDisplay;

@RunWith(MockitoJUnitRunner.class)
public class SingleFileSourceTest
{
  @Mock
  private ImageDisplay imageDisplay;

  @InjectMocks
  private SingleFileSource singleFileSource;

  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();
  private File inputFile;

  @Before
  public void setUp() throws Exception
  {
    inputFile = tmpFolder.newFile();
    ImageIO.write(buildInputImage(), "PNG", inputFile);
  }


  @Test
  public void shouldDoNothingWhenInactive()
  {
    singleFileSource.setActive(false);
    singleFileSource.setFileSource(inputFile);

    verify(imageDisplay, never()).setOriginalImage(any());
  }

  @Test
  public void shouldDoNothingWhenNoFileSet()
  {
    singleFileSource.setActive(true);

    verify(imageDisplay, never()).setOriginalImage(any());
  }

  @Test
  public void shouldSetImageWhenActive()
  {
    singleFileSource.setActive(true);
    singleFileSource.setFileSource(inputFile);

    verify(imageDisplay).setOriginalImage(argThat(sameImage(buildInputImage())));
  }

  @Test
  public void shouldNotSetImageWhenWrongImage() throws Exception
  {
    FileUtils.writeStringToFile(inputFile, "Not an image", StandardCharsets.UTF_8);
    singleFileSource.setActive(true);
    singleFileSource.setFileSource(inputFile);

    verify(imageDisplay, never()).setOriginalImage(any());
  }


  private BufferedImage buildInputImage()
  {
    BufferedImage inputImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    inputImage.setRGB(0, 0, Color.GREEN.getRGB());
    return inputImage;
  }

}
