package org.needle4k.quickstart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.needle4k.db.TransactionHelper;
import org.needle4k.junit5.JPANeedleExtension;
import org.needle4k.quickstart.user.Address;
import org.needle4k.quickstart.user.Person;

import jakarta.inject.Inject;

@ExtendWith(JPANeedleExtension.class)
public class JPABasicTest
{
  @Inject
  private TransactionHelper transactionHelper;

  @Test
  public void testFindByUsername() throws Exception {
    final Person person = new Person("demo", new Address("Bülowstr. 66", "10783 Berlin"));

    transactionHelper.saveObject(person);

    final Person result = (Person) transactionHelper.execute(
        entityManager -> entityManager.createQuery("SELECT p FROM Person p WHERE p.name = :name")
            .setParameter("name", "demo").getSingleResult()
    );

    assertThat(result).isEqualTo(person);

    assertThatThrownBy(() -> result.getAddresses()).as("out of transaction scope")
        .isInstanceOf(LazyInitializationException.class);
  }
}
