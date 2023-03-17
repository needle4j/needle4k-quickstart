package org.needle4k.quickstart.mock;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class MyComponentBean implements MyComponent {
  @EJB
  private MyEjbComponent myEjbComponent;

  @Override
  public String testMock() {
    return myEjbComponent.doSomething();
  }
}
