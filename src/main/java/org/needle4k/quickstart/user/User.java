package org.needle4k.quickstart.user;

public class User
{
  private String username;

  public User(final String username)
  {
    assert username != null : "assert username != null";
    this.username = username;
  }

  public String getUsername()
  {
    return username;
  }

  public boolean isAllowed(final String requestedPermission)
  {
    return getUsername().equals("markus");
  }
}
