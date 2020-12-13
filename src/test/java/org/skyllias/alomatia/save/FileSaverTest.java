
package org.skyllias.alomatia.save;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.skyllias.alomatia.test.ImageUtils;

public class FileSaverTest
{
  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();

  @Test
  public void shouldFindSavedImageInFile() throws Exception
  {
    BufferedImage imageToSave = new BufferedImage(4, 3, BufferedImage.TYPE_INT_ARGB);
    File destinationFile      = tmpFolder.newFile();

    FileSaver fileSaver = new FileSaver();
    fileSaver.save(imageToSave, destinationFile);

    BufferedImage imageReadFromFile = ImageIO.read(destinationFile);
    assertTrue(ImageUtils.areEqual(imageReadFromFile, imageToSave));
  }

}
