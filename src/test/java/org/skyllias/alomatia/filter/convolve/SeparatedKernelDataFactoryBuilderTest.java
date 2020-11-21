
package org.skyllias.alomatia.filter.convolve;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;

public class SeparatedKernelDataFactoryBuilderTest
{
  @Test
  public void shouldGenerateHorizontalAndVerticalKernels()
  {
    BlurLineProfile profile = mock(BlurLineProfile.class);
    when(profile.getProfile(5)).thenReturn(new float[] {1, 2, 3, 4, 5});

    SeparatedKernelDataFactoryBuilder builder = new SeparatedKernelDataFactoryBuilder();
    KernelDataFactory[] kernelDataFactories   = builder.createSeparatedKernelDataFactories(profile, 5);

    assertEquals(2, kernelDataFactories.length);
    assertTrue(Arrays.deepEquals(new float[][] {{1, 2, 3, 4, 5}}, kernelDataFactories[0].getKernelData()));
    assertTrue(Arrays.deepEquals(new float[][] {{1}, {2}, {3}, {4}, {5}}, kernelDataFactories[1].getKernelData()));
  }

}
