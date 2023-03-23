package org.needle4k.quickstart.injection;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.needle4k.injection.InjectionProvider;
import org.needle4k.injection.InjectionTargetInformation;

public class CustomMapInjectionProvider implements InjectionProvider<Map<Object, Object>>
{
  public static final Map<Object, Object> MAP = new HashMap<>();

  @NotNull
  @Override
  public Object getKey(@NotNull final InjectionTargetInformation<?> injectionTargetInformation)
  {
    return injectionTargetInformation.getInjectedObjectType();
  }

  @Override
  public boolean verify(@NotNull final InjectionTargetInformation<?> injectionTargetInformation)
  {
    return injectionTargetInformation.getInjectedObjectType() == Map.class;
  }

  @Nullable
  @Override
  public Map<Object, Object> getInjectedObject(@NotNull final Class<?> aClass)
  {
    return MAP;
  }
}
