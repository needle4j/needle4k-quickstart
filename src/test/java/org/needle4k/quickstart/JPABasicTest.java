package org.needle4k.quickstart;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.needle4k.db.TransactionHelper;
import org.needle4k.junit5.JPANeedleExtension;
import org.needle4k.quickstart.user.Address;
import org.needle4k.quickstart.user.Person;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ExtendWith(JPANeedleExtension.class)
public class JPABasicTest
{
  @Inject
  private TransactionHelper transactionHelper;

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  public void testFindByName() throws Exception
  {
    final Person person = transactionHelper.saveObject(new Person("demo", new Address("Bülowstr. 66", "10783 Berlin")));
    final Person personFromDB = (Person) entityManager
        .createQuery("SELECT p FROM Person p WHERE p.name = :name")
        .setParameter("name", "demo").getSingleResult();

    assertThat(personFromDB).isEqualTo(person);
    assertThat(personFromDB.getAddresses()).hasSize(1);
  }
}
