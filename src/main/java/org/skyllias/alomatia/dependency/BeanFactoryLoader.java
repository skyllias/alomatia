
package org.skyllias.alomatia.dependency;

import org.skyllias.alomatia.preferences.FramePolicyPreferences;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Loader of a bean factory with all configured dependencies injected.
 *  The load is carried out once per instance. */

public class BeanFactoryLoader
{
  private static final String BASE_PACKAGE_TO_SCAN = "org.skyllias.alomatia";

  private BeanFactory beanFactory;

//==============================================================================

  public <T> T getLoadedBean(Class<T> type)
  {
    if (beanFactory == null) beanFactory = loadBeanFactory();

    return beanFactory.getBean(type);
  }

//------------------------------------------------------------------------------

  private BeanFactory loadBeanFactory()
  {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

    FramePolicyPreferences framePolicyPreferences = new FramePolicyPreferences(); // has to be manually instantiated to set the active profile before component scan
    if (framePolicyPreferences.isUsingInternalFrames()) applicationContext.getEnvironment().setActiveProfiles(Profiles.INTERNAL_WINDOWS);

    applicationContext.scan(BASE_PACKAGE_TO_SCAN);
    applicationContext.refresh();

    return applicationContext;
  }

//------------------------------------------------------------------------------

}
