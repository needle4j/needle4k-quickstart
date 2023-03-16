package org.needle4k.quickstart.injection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockingDetails;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.needle4k.annotation.ObjectUnderTest;
import org.needle4k.junit5.NeedleExtension;
import org.needle4k.quickstart.annotations.CustomInjectionAnnotation2;

/**
 * Shows how to add a custom annotation programmatically. It also demonstrates how to access internal configuration
 * in general.
 */
@ExtendWith(NeedleExtension.class)
public class CustomInjectionProviderTest2
{
  @ObjectUnderTest
  private CustomInjectionTestComponent component;

  @RegisterExtension
  private static final NeedleExtension needleExtension = new NeedleExtension()
  {
    // CustomInjectionAnnotation1 is added by needle.properties already
    {
      getNeedleInjector().getConfiguration().addGlobalInjectionAnnotationClass(CustomInjectionAnnotation2.class);
    }
  };

  @Test
  public void testCustomInjectionProvider()
  {
    assertThat(component.getMap()).isSameAs(CustomMapInjectionProvider.MAP);
    assertThat(component.getQueue1()).isNotNull();
    assertThat(mockingDetails(component.getQueue1()).isMock()).isTrue();
    assertThat(component.getQueue2()).isNotNull();
    assertThat(mockingDetails(component.getQueue2()).isMock()).isTrue();
  }
}
