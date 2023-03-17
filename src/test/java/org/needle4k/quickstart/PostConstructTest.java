package org.needle4k.quickstart;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.needle4k.annotation.InjectIntoMany;
import org.needle4k.annotation.ObjectUnderTest;
import org.needle4k.junit5.NeedleExtension;
import org.needle4k.quickstart.annotations.CustomInjectionAnnotation2;
import org.needle4k.quickstart.bootstrap.Service1;
import org.needle4k.quickstart.bootstrap.Service2;
import org.needle4k.quickstart.bootstrap.StartupService;
import org.needle4k.registries.AnnotationRegistry;

public class PostConstructTest
{
  @ObjectUnderTest(postConstruct = true)
  private StartupService objectUnderTest;

  @InjectIntoMany
  private final Service1 service1 = service1();

  @InjectIntoMany
  private final Service2 service2 = service2();

  // Add CustomInjectionAnnotation1 as post construct life cycle annotation, @PostConstruct is already added by default
  @RegisterExtension
  private static final NeedleExtension needleExtension = new NeedleExtension()
  {
    {
      final AnnotationRegistry registry = getNeedleConfiguration().getPostconstructAnnotationRegistry();
      registry.addAnnotation(CustomInjectionAnnotation2.class);
    }
  };

  @Test
  public void testPostConstruct()
  {
    verify(service1, times(1)).callMe();
    verify(service2, times(1)).callMe();

    Assertions.assertThat(objectUnderTest.messages).containsExactlyInAnyOrder("jens", 42);
  }

  // Training has to be done earlier than usual, because init() is called before test is run actually
  private Service1 service1()
  {
    final Service1 service1 = Mockito.mock(Service1.class);
    when(service1.callMe()).thenReturn("jens");
    return service1;
  }

  private Service2 service2()
  {
    final Service2 service2 = Mockito.mock(Service2.class);
    when(service2.callMe()).thenReturn(42);
    return service2;
  }
}
