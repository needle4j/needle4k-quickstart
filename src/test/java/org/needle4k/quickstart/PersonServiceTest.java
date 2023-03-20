package org.needle4k.quickstart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.needle4k.annotation.InjectInto;
import org.needle4k.annotation.InjectIntoMany;
import org.needle4k.annotation.Mock;
import org.needle4k.annotation.ObjectUnderTest;
import org.needle4k.db.TransactionHelper;
import org.needle4k.junit5.JPANeedleExtension;
import org.needle4k.quickstart.user.SystemType;
import org.needle4k.quickstart.user.User;
import org.needle4k.quickstart.user.dao.PersonDao;
import org.needle4k.quickstart.user.dao.PersonService;
import org.needle4k.reflection.ReflectionHelper;

import jakarta.ejb.EJBAccessException;
import jakarta.inject.Inject;

/**
 * Shows how to create mocks as well as real instance and use provided internal utilities.
 * <p>
 * Example for testing DAOs.
 */
@ExtendWith(JPANeedleExtension.class)
public class PersonServiceTest
{
  @Inject
  private TransactionHelper transactionHelper;

  @InjectIntoMany
  @Mock
  private User user;

  // Normally you would just use @InjectIntoMany, since we want the same instance everywhere. Usually...
  @InjectInto(targetComponentId = "service")
  private final PersonDao dao = Mockito.mock(PersonDao.class);

  @InjectInto(targetComponentId = "service")
  private final SystemType systemType = SystemType.PROD;

  @ObjectUnderTest(id = "service")
  private PersonService objectUnderTest;

  @Test
  public void testMocking()
  {
    final SystemType systemType = (SystemType) ReflectionHelper.getFieldValue(objectUnderTest, "systemType");
    final PersonDao dao = (PersonDao) ReflectionHelper.getFieldValue(objectUnderTest, "personDao");

    assertThat(mockingDetails(objectUnderTest).isMock()).isFalse();
    assertThat(mockingDetails(dao).isMock()).isTrue();
    assertThat(systemType).isSameAs(SystemType.PROD);
    assertThat(dao).isSameAs(this.dao);
  }

  @Test
  public void testSecurityCheckWorks()
  {
    when(dao.findByStreet(anyString())).thenReturn(new ArrayList<>());
    when(user.isAllowed(anyString())).thenReturn(true);

    assertThat(objectUnderTest.findByStreet("Kurfürstendamm 66")).isEmpty();
  }

  @Test
  public void testSecurityCheckFails()
  {
    when(dao.findByStreet(anyString())).thenReturn(new ArrayList<>());
    when(user.isAllowed(anyString())).thenReturn(false);
    when(user.getUsername()).thenReturn("jens");

    assertThatThrownBy(() -> objectUnderTest.findByStreet("Kurfürstendamm 66")).isInstanceOf(EJBAccessException.class);
  }
}
