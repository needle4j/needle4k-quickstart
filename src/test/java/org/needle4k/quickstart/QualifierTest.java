package org.needle4k.quickstart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.needle4k.injection.InjectionProviders.providerForQualifiedInstance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mockito;
import org.needle4k.annotation.ObjectUnderTest;
import org.needle4k.junit5.NeedleExtension;
import org.needle4k.quickstart.annotations.CurrentUser;
import org.needle4k.quickstart.user.User;
import org.needle4k.quickstart.user.dao.PersonDao;
import org.needle4k.reflection.ReflectionUtil;

import jakarta.ejb.EJBAccessException;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 * Example for custom injection provider for "qualified" CDI component injection
 */
public class QualifierTest
{
  private static final User USER = new User("heinz");

  @RegisterExtension
  private static final NeedleExtension needleExtension = new NeedleExtension(providerForQualifiedInstance(CurrentUser.class, USER));

  @Inject
  private User someUser;

  @Inject
  private ReflectionUtil reflectionUtil;

  @ObjectUnderTest
  private PersonDao objectUnderTest;

  @Test
  public void testQualifier()
  {
    final User currentUser = (User) reflectionUtil.getFieldValue(objectUnderTest, "currentUser");
    final EntityManager entityManager = (EntityManager) reflectionUtil.getFieldValue(objectUnderTest, "entityManager");

    assertThat(Mockito.mockingDetails(objectUnderTest).isMock()).isFalse();
    assertThat(Mockito.mockingDetails(entityManager).isMock()).isTrue();

    assertThatThrownBy(() -> objectUnderTest.findByStreet("")).isInstanceOf(EJBAccessException.class);

    assertThat(someUser).isNotSameAs(currentUser);
    assertThat(currentUser).isSameAs(USER);
  }
}
