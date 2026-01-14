
package org.skyllias.alomatia.dependency;

import javax.swing.Timer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Instantiator of timers for UI components that need one.
 *  If different instances are needed in the future, other qualifiers should be
 *  used to avoid cross-effects. */

@Configuration
public class TimerConfiguration
{
  public static final String SCREEN_CAPTURE_TIMER_QUALIFIER = "screenCaptureTimer";

  private static final int DEFAULT_DELAY_MS = 40;

//==============================================================================

  @Bean(SCREEN_CAPTURE_TIMER_QUALIFIER)
  public Timer screenCaptureTimer() {return new Timer(DEFAULT_DELAY_MS, null);}

//------------------------------------------------------------------------------

}
