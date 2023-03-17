package org.needle4k.quickstart.mock;

import jakarta.ejb.Stateless;

@Stateless
public class MyEjbComponentBean implements MyEjbComponent
{
  @Override
  public String doSomething()
  {
    return "Hello World";
  }
}
