package org.needle4k.quickstart.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.needle4k.annotation.ObjectUnderTest;
import org.needle4k.injection.InjectionProvider;
import org.needle4k.injection.InjectionTargetInformation;
import org.needle4k.junit5.NeedleExtension;

/**
 * Example shows how to test Spring components in isolation.
 *
 * It also demonstrates that also constructor injection may be used.
 */
public class FooServiceTest
{
  @RegisterExtension
  private static final NeedleExtension needleExtension = new NeedleExtension(new FormatterInjectionProvider(new FooFormatter()));

  @ObjectUnderTest
  private FooService fooService;

  @Test
  public void whenFooFormatterType_thenReturnFoo()
  {
    assertEquals("foo", fooService.doStuff());
  }

  private static class FormatterInjectionProvider implements InjectionProvider<Formatter>
  {
    private final Formatter instance;

    public FormatterInjectionProvider(final Formatter instance) {
      this.instance = instance;
    }

    @Nullable
    @Override
    public <T> T getInjectedObject(@NotNull final Class<T> injectionTargetType)
    {
      return (T) instance;
    }

    @NotNull
    @Override
    public Object getKey(@NotNull final InjectionTargetInformation<?> injectionTargetInformation)
    {
      return injectionTargetInformation.getInjectedObjectType();
    }

    @Override
    public boolean verify(@NotNull final InjectionTargetInformation<?> injectionTargetInformation)
    {
      return injectionTargetInformation.getInjectedObjectType().isAssignableFrom(instance.getClass());
    }
  }
}