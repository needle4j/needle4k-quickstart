package org.needle4k.quickstart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.needle4k.injection.InjectionProviders.providerForQualifiedInstance;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.needle4k.annotation.ObjectUnderTest;
import org.needle4k.junit5.NeedleExtension;
import org.needle4k.quickstart.annotations.CurrentUser;
import org.needle4k.quickstart.user.User;
import org.needle4k.quickstart.user.dao.PersonService;
import org.needle4k.reflection.ReflectionUtil;

import jakarta.inject.Inject;

/**
 * Example for custom injection provider for "qualified" CDI component injection
 */
public class QualifierTest
{
  private static final User USER = new User("heinz");

  @RegisterExtension
  private static final NeedleExtension needleExtension = new NeedleExtension(
      providerForQualifiedInstance(CurrentUser.class, USER));

  @Inject
  private User someUser;

  @Inject
  private ReflectionUtil reflectionUtil;

  @ObjectUnderTest
  private PersonService objectUnderTest;

  @Test
  public void testQualifier()
  {
    final User currentUser = (User) reflectionUtil.getFieldValue(objectUnderTest, "currentUser");

    assertThat(someUser).isNotSameAs(currentUser);
    assertThat(currentUser).isSameAs(USER);
  }
}
