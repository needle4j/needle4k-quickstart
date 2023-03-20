package org.needle4k.quickstart.injection.cdi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.needle4k.annotation.ObjectUnderTest;
import org.needle4k.junit5.NeedleExtension;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ExtendWith(NeedleExtension.class)
public class InstanceFieldInjectionTest {

  @ObjectUnderTest
  private InstanceFieldInjectionBean component;

  @Inject
  private Instance<InstanceTestBean> instance;

  @Inject
  private Instance<Runnable> runnableInstances;

  @Test
  public void testInstanceFieldInjection() {
    assertNotNull(instance);
    assertNotNull(runnableInstances);

    assertNotSame(instance, runnableInstances);
    assertSame(instance, component.getInstance());
  }
}
