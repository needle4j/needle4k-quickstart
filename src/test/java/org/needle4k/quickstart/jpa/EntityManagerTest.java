package org.needle4k.quickstart.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.Session;
import org.hsqldb.jdbc.JDBCConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.needle4k.db.TransactionHelper;
import org.needle4k.junit5.JPANeedleExtension;
import org.needle4k.quickstart.user.Address;
import org.needle4k.quickstart.user.Person;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Test basic JPA functionality
 */
@SuppressWarnings("CdiInjectionPointsInspection")
@ExtendWith(JPANeedleExtension.class)
public class EntityManagerTest
{
  @Inject
  private TransactionHelper transactionHelper;

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  public void testFindByName()
  {
    final Person person = transactionHelper.saveObject(new Person("demo", new Address("Bülowstr. 66", "10783 Berlin")));
    final Person personFromDB = (Person) entityManager
        .createQuery("SELECT p FROM Person p WHERE p.name = :name")
        .setParameter("name", "demo").getSingleResult();

    assertThat(personFromDB).isEqualTo(person);
    assertThat(personFromDB.getAddresses()).hasSize(1);

    final Session session = entityManager.unwrap(Session.class);
    session.doWork(connection -> assertThat(connection.getClass()).isEqualTo(JDBCConnection.class));
  }
}