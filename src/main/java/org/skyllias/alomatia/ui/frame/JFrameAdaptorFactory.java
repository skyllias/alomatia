
package org.skyllias.alomatia.ui.frame;

import javax.swing.*;

import org.skyllias.alomatia.ui.*;

/** {@link FrameAdaptorFactory} that returns {@link JFrameAdaptor}s. */

public class JFrameAdaptorFactory implements FrameAdaptorFactory
{
//==============================================================================

  @Override
  public FrameAdaptor getNewFrame(DisplayPanel displayPanel)
  {
    JFrame jFrame = new JFrame();
    jFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    return new JFrameAdaptor(jFrame);
  }

//------------------------------------------------------------------------------

}
