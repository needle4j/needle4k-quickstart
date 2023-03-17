package org.needle4k.quickstart.bootstrap;

import jakarta.ejb.Remote;

@Remote
public interface Service1
{
  String callMe();
}
