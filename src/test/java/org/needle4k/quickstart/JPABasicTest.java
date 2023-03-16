package org.needle4k.quickstart;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.needle4k.db.TransactionHelper;
import org.needle4k.junit5.JPANeedleExtension;
import org.needle4k.quickstart.user.Person;

import jakarta.inject.Inject;

@ExtendWith(JPANeedleExtension.class)
public class JPABasicTest
{
  @Inject
  private TransactionHelper transactionHelper;

  @Test
  public void testFindByUsername() throws Exception {
    final Person person = new Person("demo");

    transactionHelper.saveObject(person);

    final Person result = (Person) transactionHelper.executeInTransaction(
        entityManager -> entityManager.createQuery("SELECT p FROM Person p WHERE p.name = :name")
            .setParameter("name", "demo").getSingleResult()
    );

    assertThat(result).isEqualTo(person);
  }
}
