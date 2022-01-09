
package org.skyllias.alomatia.dependency;

import org.skyllias.alomatia.ui.frame.FramePolicyAtStartUp;
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

    FramePolicyAtStartUp framePolicy = new FramePolicyAtStartUp();
    if (framePolicy.isUsingInternalFrames()) applicationContext.getEnvironment().setActiveProfiles(Profiles.INTERNAL_WINDOWS);

    applicationContext.scan(BASE_PACKAGE_TO_SCAN);
    applicationContext.refresh();

    return applicationContext;
  }

//------------------------------------------------------------------------------

}
