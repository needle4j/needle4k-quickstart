package org.needle4k.quickstart.user.dao;

import static org.needle4k.quickstart.user.Person.FIND_BY_STREET;

import java.util.List;

import org.needle4k.quickstart.user.Person;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
@LocalBean
public class PersonDao
{
  @PersistenceContext
  private EntityManager entityManager;

  public List<Person> findByStreet(final String street)
  {
    assert street != null : "assert street != null";

    return entityManager.createNamedQuery(FIND_BY_STREET).setParameter("street", street).getResultList();
  }
}
