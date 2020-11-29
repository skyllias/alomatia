
package org.skyllias.alomatia.cli;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.skyllias.alomatia.test.matchers.AlomatiaMatchers.sameImage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyllias.alomatia.cli.exception.FilterNotFoundException;
import org.skyllias.alomatia.cli.exception.InputFileException;
import org.skyllias.alomatia.cli.exception.OutputFileException;
import org.skyllias.alomatia.cli.exception.WrongParametersException;
import org.skyllias.alomatia.filter.FilterFactory;
import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.NamedFilter;
import org.skyllias.alomatia.save.FileSaver;

@RunWith(MockitoJUnitRunner.class)
public class FilterCommandTest
{
  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();
  private File inputFile;

  @Mock
  private FilterFactory filterFactory;
  @Mock
  private FilteredImageGenerator filteredImageGenerator;
  @Mock
  private FileSaver fileSaver;

  @Before
  public void setUp() throws Exception
  {
    inputFile = tmpFolder.newFile();
    ImageIO.write(buildInputImage(), "PNG", inputFile);

    ImageFilter imageFilter = new ImageFilter();
    NamedFilter namedFilter = new NamedFilter(imageFilter, "existing.filter");
    when(filterFactory.getAllAvailableFilters()).thenReturn(Collections.singleton(namedFilter));

    when(filteredImageGenerator.generate(argThat(sameImage(buildInputImage())), eq(imageFilter)))
        .thenReturn(buildOutputImage());
  }

  @Test(expected = WrongParametersException.class)
  public void shouldFailIfTooFewArguments()
  {
    new FilterCommand(new String[2], filterFactory, filteredImageGenerator, fileSaver);
  }

  @Test(expected = WrongParametersException.class)
  public void shouldFailIfTooManyArguments()
  {
    new FilterCommand(new String[4], filterFactory, filteredImageGenerator, fileSaver);
  }

  @Test(expected = FilterNotFoundException.class)
  public void shouldFailIfInvalidFilterKey() throws Exception
  {
    File outputFile = tmpFolder.newFile();

    FilterCommand filterCommand = new FilterCommand(new String[] {inputFile.getAbsolutePath(), "nonexisting.filter", outputFile.getAbsolutePath()},
                                                    filterFactory, filteredImageGenerator, fileSaver);
    filterCommand.execute();
  }

  @Test(expected = InputFileException.class)
  public void shouldFailIfInvalidInputPath() throws Exception
  {
    File outputFile = tmpFolder.newFile();

    FilterCommand filterCommand = new FilterCommand(new String[] {"nonexisting.png", "existing.filter", outputFile.getAbsolutePath()},
                                                    filterFactory, filteredImageGenerator, fileSaver);
    filterCommand.execute();
  }

  @Test(expected = InputFileException.class)
  public void shouldFailIfInvalidInputImage() throws Exception
  {
    File outputFile = tmpFolder.newFile();

    FileUtils.write(inputFile, "text", Charset.defaultCharset());
    FilterCommand filterCommand = new FilterCommand(new String[] {inputFile.getAbsolutePath(), "nonexisting.filter", outputFile.getAbsolutePath()},
                                                    filterFactory, filteredImageGenerator, fileSaver);
    filterCommand.execute();
  }

  @Test(expected = OutputFileException.class)
  public void shouldFailIfSaveThrowsException() throws Exception
  {
    File outputFile = tmpFolder.newFile();
    doThrow(new IOException()).when(fileSaver).save(argThat(sameImage(buildOutputImage())), eq(outputFile));

    FilterCommand filterCommand = new FilterCommand(new String[] {inputFile.getAbsolutePath(), "existing.filter", outputFile.getAbsolutePath()},
                                                    filterFactory, filteredImageGenerator, fileSaver);
    filterCommand.execute();
  }

  @Test
  public void shouldSaveFilteredImage() throws Exception
  {
    File outputFile = tmpFolder.newFile();

    FilterCommand filterCommand = new FilterCommand(new String[] {inputFile.getAbsolutePath(), "existing.filter", outputFile.getAbsolutePath()},
                                                    filterFactory, filteredImageGenerator, fileSaver);
    filterCommand.execute();

    verify(fileSaver).save(argThat(sameImage(buildOutputImage())), eq(outputFile));
  }


  private BufferedImage buildInputImage()
  {
    BufferedImage inputImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    inputImage.setRGB(0, 0, Color.GREEN.getRGB());
    return inputImage;
  }

  private BufferedImage buildOutputImage()
  {
    BufferedImage inputImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    inputImage.setRGB(0, 0, Color.BLUE.getRGB());
    return inputImage;
  }

}
