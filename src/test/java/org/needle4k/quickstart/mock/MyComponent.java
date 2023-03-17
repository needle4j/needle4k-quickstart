package org.needle4k.quickstart.mock;

import jakarta.ejb.Local;

@Local
public interface MyComponent {
  String testMock();
}
