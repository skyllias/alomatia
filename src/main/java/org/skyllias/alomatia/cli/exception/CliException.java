
package org.skyllias.alomatia.cli.exception;

/** Exception thrown when anything prevents a command from completing. */

@SuppressWarnings("serial")
public class CliException extends RuntimeException
{
//==============================================================================

  public CliException(String message, Throwable cause) {super(message, cause);}

  public CliException(String message) {super(message);}

  public CliException(Throwable cause) {super(cause);}

//==============================================================================

}
