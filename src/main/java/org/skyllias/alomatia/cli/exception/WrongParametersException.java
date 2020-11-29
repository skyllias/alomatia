
package org.skyllias.alomatia.cli.exception;

@SuppressWarnings("serial")
public class WrongParametersException extends CliException
{
//==============================================================================

  public WrongParametersException(String message, Throwable cause) {super(message, cause);}

  public WrongParametersException(String message) {super(message);}

  public WrongParametersException(Throwable cause) {super(cause);}

//==============================================================================

}
