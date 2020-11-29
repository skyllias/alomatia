
package org.skyllias.alomatia.cli.app;

import org.skyllias.alomatia.cli.FilterCommand;
import org.skyllias.alomatia.cli.exception.CliException;
import org.skyllias.alomatia.filter.FilteredImageGenerator;
import org.skyllias.alomatia.filter.FixedFilterFactory;
import org.skyllias.alomatia.save.FileSaver;

/** Application that executes a filter command. */

public class CliAlomatia
{
//==============================================================================

  /** Expected args:
   *  - [0]: Path of a file to read the input image from.
   *  - [1]: Key of the filter to apply.
   *  - [2]: Path of the file to save the result in.
   *  Exit status:
   *  - 0: Operation completed.
   *  - 1: Something wrong, with some information written to the rror output. */

  public static void main(String[] args)
  {
    try
    {
      FilterCommand filterCommand = new FilterCommand(args, new FixedFilterFactory(),
                                                      new FilteredImageGenerator(), new FileSaver());
      filterCommand.execute();

      System.exit(0);
    }
    catch (CliException re)
    {
      System.err.println(re.getMessage());
      System.exit(1);
    }
  }

//------------------------------------------------------------------------------

}
