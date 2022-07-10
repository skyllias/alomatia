
package org.skyllias.alomatia.cli;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.skyllias.alomatia.cli.exception.CliException;
import org.skyllias.alomatia.cli.exception.FilterNotFoundException;
import org.skyllias.alomatia.cli.exception.InputFileException;
import org.skyllias.alomatia.cli.exception.OutputFileException;
import org.skyllias.alomatia.cli.exception.WrongParametersException;
import org.skyllias.alomatia.filter.FilterFactory;
import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.NamedFilter;
import org.skyllias.alomatia.save.FileSaver;

/** Command used by a CLI to apply one filter to an image from a file and save
 *  the result in another file. */

public class FilterCommand
{
  private final String inputPath;
  private final String filterKey;
  private final String outputPath;

  private final FilterFactory filterFactory;
  private final FilteredImageGenerator filteredImageGenerator;
  private final FileSaver fileSaver;

//==============================================================================

  public FilterCommand(String[] args, FilterFactory filterFactory,
                       FilteredImageGenerator filteredImageGenerator, FileSaver fileSaver) throws CliException
  {
    if (args.length != 3) throw new WrongParametersException("Expected arguments: <path of input image> <key of filter> <path of output file>");

    inputPath  = args[0];
    filterKey  = args[1];
    outputPath = args[2];

    this.filterFactory          = filterFactory;
    this.filteredImageGenerator = filteredImageGenerator;
    this.fileSaver              = fileSaver;
  }

//==============================================================================

  /** Reads the input file, applies the filter and saves the result in the
   *  output file. */

  public void execute() throws CliException
  {
    Image inputImage        = readInput();
    ImageFilter imageFilter = getFilter();
    Image outputImage       = applyFilter(inputImage, imageFilter);
    saveOutput(outputImage);
  }

//------------------------------------------------------------------------------

  /* Returns the image contained in the file referenced by inputPath or throws
   * an exception.
   * A big refactor would be needed to make use of FileSource. */

  private Image readInput() throws InputFileException
  {
    File inputFile = new File(inputPath);
    if (!inputFile.exists()) throw new InputFileException("The file " + inputPath + " does not exist.");

    try
    {
      BufferedImage image = ImageIO.read(inputFile);
      if (image == null) throw new InputFileException("The contents of file " + inputPath + " cannot be read as an image.");
      return image;
    }
    catch (IOException ioe)
    {
      throw new InputFileException("The contents of file " + inputPath + " cannot be read as an image. " +
                                   "Cause: " + ioe.getMessage(), ioe);
    }
  }

//------------------------------------------------------------------------------

  /* Returns a filter in filterFactory with a name key equal to filterKey or
   * throws an exception. */

  private ImageFilter getFilter() throws FilterNotFoundException
  {
    for (NamedFilter currentNamedFilter : filterFactory.getAllAvailableFilters())
    {
      if (currentNamedFilter.getFilterKey().equals(filterKey)) return currentNamedFilter.getFilter();
    }

    throw new FilterNotFoundException("No filter can be found with key " + filterKey);
  }

//------------------------------------------------------------------------------

  /* Returns a new image resulting from applying imageFilter to inputImage. */

  private Image applyFilter(Image inputImage, ImageFilter imageFilter)
  {
    return filteredImageGenerator.generate(inputImage, imageFilter);
  }

//------------------------------------------------------------------------------

  /* Stores outputImage in the file referenced by outputPath or throws an exception. */

  private void saveOutput(Image outputImage) throws OutputFileException
  {
    File outputFile = new File(outputPath);
    if (outputFile.exists() && !outputFile.canWrite()) throw new OutputFileException("The file " + outputPath + " cannot be written.");

    try {fileSaver.save(outputImage, outputFile);}
    catch (IOException ioe)
    {
      throw new OutputFileException("The image cannot be written to file " + outputPath + ". " +
                                    "Cause: " + ioe.getMessage(), ioe);
    }
  }

//------------------------------------------------------------------------------

}
