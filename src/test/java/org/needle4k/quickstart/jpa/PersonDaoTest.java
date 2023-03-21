package org.needle4k.quickstart.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockingDetails;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.needle4k.annotation.ObjectUnderTest;
import org.needle4k.db.TransactionHelper;
import org.needle4k.junit5.JPANeedleExtension;
import org.needle4k.quickstart.user.Address;
import org.needle4k.quickstart.user.Person;
import org.needle4k.quickstart.user.dao.PersonDao;
import org.needle4k.reflection.ReflectionUtil;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

/**
 * Shows how to create mocks as well as real instance and use provided internal utilities.
 * <p>
 * Example for testing DAOs.
 */
@ExtendWith(JPANeedleExtension.class)
@SuppressWarnings("CdiInjectionPointsInspection")
public class PersonDaoTest
{
  @Inject
  private TransactionHelper transactionHelper;

  @ObjectUnderTest
  private PersonDao objectUnderTest;

  @Test
  public void testMocking()
  {
    final EntityManager entityManager = (EntityManager) ReflectionUtil.getFieldValue(objectUnderTest, "entityManager");

    assertThat(mockingDetails(objectUnderTest).isMock()).isFalse();
    assertThat(mockingDetails(entityManager).isMock()).isFalse();
  }

  @Test
  public void testFindByStreet()
  {
    final Person person1 = transactionHelper.saveObject(new Person("Heinz", new Address("Bülowstr. 66", "10783 Berlin")));
    final Person person2 = transactionHelper.saveObject(new Person("Markus", new Address("Bülowstr. 66", "14163 Berlin")));
    final Person person3 = transactionHelper.saveObject(new Person("René", new Address("Kurfürstendamm 66", "10707 Berlin")));

    assertThat(objectUnderTest.findByStreet("")).isEmpty();
    assertThat(objectUnderTest.findByStreet("Bülowstr. 66")).containsExactlyInAnyOrder(person1, person2);
    assertThat(objectUnderTest.findByStreet("Kurfürstendamm 66")).containsExactly(person3);
  }

  @Test
  public void testConstraint()
  {
    transactionHelper.saveObject(new Person("Heinz", new Address("Bülowstr. 66", "10783 Berlin")));

    assertThatThrownBy(() -> transactionHelper.saveObject(new Person("Markus", new Address("", "10783 Berlin"))))
        .as("Constraint violation, because zip column is unique").isInstanceOf(PersistenceException.class)
        .hasCauseInstanceOf(ConstraintViolationException.class);
  }
}
