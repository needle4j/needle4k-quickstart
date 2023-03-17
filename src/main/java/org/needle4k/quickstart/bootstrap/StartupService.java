package org.needle4k.quickstart.bootstrap;

import java.util.ArrayList;
import java.util.List;

import org.needle4k.quickstart.annotations.CustomInjectionAnnotation2;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

@Singleton
@Startup
public class StartupService
{
  @EJB
  private Service1 service1;

  @EJB
  private Service2 service2;

  public final List<Object> messages = new ArrayList<>();

  @PostConstruct
  public void init1()
  {
    messages.add(service1.callMe());
  }

  @CustomInjectionAnnotation2
  public void init2()
  {
    messages.add(service2.callMe());
  }
}
