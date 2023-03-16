package org.needle4k.quickstart.user.dao;

import static org.needle4k.quickstart.user.Person.FIND_BY_STREET;

import java.util.List;

import org.needle4k.quickstart.annotations.CurrentUser;
import org.needle4k.quickstart.user.Person;
import org.needle4k.quickstart.user.User;

import jakarta.ejb.EJBAccessException;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
@LocalBean
public class PersonDao
{
  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  @CurrentUser
  private User currentUser;

  public List<Person> findByStreet(final String street) {
    assert street != null : "assert street != null";

    if(currentUser.isAllowed(FIND_BY_STREET)) {
      return entityManager.createNamedQuery(FIND_BY_STREET).setParameter("street", street).getResultList();
    } else {
      throw new EJBAccessException("Action " + FIND_BY_STREET + " is not allowed for user " + currentUser.getUsername());
    }
  }
}
