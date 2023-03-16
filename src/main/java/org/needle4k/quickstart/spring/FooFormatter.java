package org.needle4k.quickstart.spring;

import org.springframework.stereotype.Component;

@FormatterType("Foo")
@Component
public class FooFormatter implements Formatter {
  public String format() {
    return "foo";
  }
}