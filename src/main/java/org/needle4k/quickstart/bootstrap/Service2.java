package org.needle4k.quickstart.bootstrap;

import jakarta.ejb.Remote;

@Remote
public interface Service2
{
  int callMe();
}
