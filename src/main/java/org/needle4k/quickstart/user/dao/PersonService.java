package org.needle4k.quickstart.user.dao;

import static org.needle4k.quickstart.user.Person.FIND_BY_STREET;

import java.util.List;

import org.needle4k.quickstart.annotations.CurrentUser;
import org.needle4k.quickstart.user.Person;
import org.needle4k.quickstart.user.SystemType;
import org.needle4k.quickstart.user.User;

import jakarta.ejb.EJB;
import jakarta.ejb.EJBAccessException;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
@LocalBean
public class PersonService
{
  @EJB
  private PersonDao personDao;

  @Inject
  @CurrentUser
  private User currentUser;

  @Inject
  private SystemType systemType;

  public List<Person> findByStreet(final String street)
  {
    if (currentUser.isAllowed(FIND_BY_STREET))
    {
      return personDao.findByStreet(street);
    }
    else
    {
      throw new EJBAccessException(
          "Action " + FIND_BY_STREET + " is not allowed for user " + currentUser.getUsername() + "@" + systemType);
    }
  }
}
