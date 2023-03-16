package org.needle4k.quickstart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.needle4k.quickstart.user.Person.FIND_BY_STREET;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.needle4k.annotation.InjectIntoMany;
import org.needle4k.annotation.Mock;
import org.needle4k.annotation.ObjectUnderTest;
import org.needle4k.db.TransactionHelper;
import org.needle4k.junit5.JPANeedleExtension;
import org.needle4k.quickstart.user.Address;
import org.needle4k.quickstart.user.Person;
import org.needle4k.quickstart.user.User;
import org.needle4k.quickstart.user.dao.PersonDao;
import org.needle4k.reflection.ReflectionUtil;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

@ExtendWith(JPANeedleExtension.class)
public class PersonDaoTest
{
  @Inject
  private TransactionHelper transactionHelper;

  @InjectIntoMany
  @Mock
  private User user;

  @Inject
  private ReflectionUtil reflectionUtil;

  @ObjectUnderTest
  private PersonDao objectUnderTest;

  @Test
  public void testFindByStreet() throws Exception
  {
    final EntityManager entityManager = (EntityManager) reflectionUtil.getFieldValue(objectUnderTest, "entityManager");

    assertThat(Mockito.mockingDetails(user).isMock()).isTrue();
    assertThat(Mockito.mockingDetails(objectUnderTest).isMock()).isFalse();
    assertThat(Mockito.mockingDetails(entityManager).isMock()).isFalse();

    final Person person1 = transactionHelper.saveObject(new Person("Heinz", new Address("Bülowstr. 66", "10783 Berlin")));

    assertThatThrownBy(() -> transactionHelper.saveObject(new Person("Markus", new Address("", "10783 Berlin"))))
        .isInstanceOf(PersistenceException.class);
    final Person person2 = transactionHelper.saveObject(new Person("Markus", new Address("Bülowstr. 66", "14163 Berlin")));
    final Person person3 = transactionHelper.saveObject(new Person("René", new Address("Kurfürstendamm 66", "10707 Berlin")));

    when(user.isAllowed(anyString())).thenReturn(true);
    when(user.getUsername()).thenReturn("markus");

    assertThat(objectUnderTest.findByStreet("")).isEmpty();
    assertThat(objectUnderTest.findByStreet("Bülowstr. 66")).containsExactlyInAnyOrder(person1, person2);
    assertThat(objectUnderTest.findByStreet("Kurfürstendamm 66")).containsExactly(person3);
  }
}
