package org.needle4k.quickstart.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.needle4k.db.JPAInjectorConfiguration;
import org.needle4k.db.TransactionHelper;
import org.needle4k.junit5.JPANeedleExtension;
import org.needle4k.quickstart.user.Address;
import org.needle4k.quickstart.user.Person;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Test basic JPA functionality
 */
@SuppressWarnings("CdiInjectionPointsInspection")
@ExtendWith(JPANeedleExtension.class)
public class TransactionTest
{
  @Inject
  private TransactionHelper transactionHelper1;

  @Inject
  private JPAInjectorConfiguration configuration;

  @Test
  public void withoutTransactionsTwoEntityManagersDontSeeEachOther()
  {
    final EntityManager entityManager1 = transactionHelper1.getEntityManager();

    try (EntityManager entityManager2 = configuration.getEntityManagerFactory().createEntityManager())
    {

      final Person person = new Person("demo", new Address("Bülowstr. 66", "10783 Berlin"));
      entityManager1.persist(person);

      final Person personFromDB = entityManager2.find(Person.class, person.getId());
      assertThat(personFromDB).isNull();
    }

  }

  @Test
  public void withTransactionsEverythingIsFine()
  {
    try (EntityManager entityManager2 = configuration.getEntityManagerFactory().createEntityManager())
    {
      final TransactionHelper transactionHelper2 = new TransactionHelper(entityManager2);

      final Person person = transactionHelper1.saveObject(new Person("demo", new Address("Bülowstr. 66", "10783 Berlin")));
      final Person personFromDB = transactionHelper2.loadObject(Person.class, person.getId());

      assertThat(personFromDB).isEqualTo(person);

      assertThatThrownBy(personFromDB::getAddresses).as("Out of transaction scope, cannot load lazily")
          .isInstanceOf(LazyInitializationException.class);
      final List<Address> addresses = transactionHelper2
          .execute(entityManager -> entityManager.find(Person.class, person.getId()).getAddresses());

      assertThat(addresses).as("When performed within transaction, lazy loading is OK").hasSize(1);
    }
  }
}
