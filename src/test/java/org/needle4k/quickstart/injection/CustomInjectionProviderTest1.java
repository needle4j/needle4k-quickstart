package org.needle4k.quickstart.injection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockingDetails;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.needle4k.annotation.ObjectUnderTest;
import org.needle4k.junit5.NeedleExtension;

/**
 * See declaration of injection provider in needle.properties file.
 */
@ExtendWith(NeedleExtension.class)
public class CustomInjectionProviderTest1
{
  @ObjectUnderTest
  private CustomInjectionTestComponent component;

  @Test
  public void testCustomInjectionProvider()
  {
    assertThat(component.getMap()).isSameAs(CustomMapInjectionProvider.MAP);
    assertThat(component.getQueue1()).isNotNull();
    assertThat(mockingDetails(component.getQueue1()).isMock()).isTrue();
    assertThat(component.getQueue2()).as("Annotation is not registered").isNull();
  }
}
