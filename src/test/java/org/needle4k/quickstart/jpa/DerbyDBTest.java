package org.needle4k.quickstart.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.needle4k.configuration.ConfigurationLoaderKt.DB_OPERATION_KEY;
import static org.needle4k.configuration.ConfigurationLoaderKt.PERSISTENCE_UNIT_NAME_KEY;

import java.util.Map;

import org.apache.derby.impl.jdbc.EmbedConnection;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.needle4k.db.TransactionHelper;
import org.needle4k.db.operation.NoOperation;
import org.needle4k.junit5.JPANeedleExtension;
import org.needle4k.quickstart.user.Address;
import org.needle4k.quickstart.user.Person;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SuppressWarnings("CdiInjectionPointsInspection")
public class DerbyDBTest
{
  @RegisterExtension
  private static final JPANeedleExtension needleExtension = new JPANeedleExtension()
  {
    protected void configure()
    {
      final Map<String, String> properties = getNeedleConfiguration().getConfigurationProperties();

      properties.put(PERSISTENCE_UNIT_NAME_KEY, "DerbyModel");
      properties.put(DB_OPERATION_KEY, NoOperation.class.getName());
    }
  };

  @Inject
  private TransactionHelper transactionHelper;

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  public void testSaveAndLoad()
  {
    final Person person = transactionHelper.saveObject(new Person("demo", new Address("Bülowstr. 66", "10783 Berlin")));
    final Person personFromDB = transactionHelper.loadObject(Person.class, person.getId());

    assertThat(personFromDB).isEqualTo(person);
    assertThat(transactionHelper.loadAllObjects(Person.class)).hasSize(1);

    final Session session = entityManager.unwrap(Session.class);
    session.doWork(connection -> assertThat(connection.getClass()).isEqualTo(EmbedConnection.class));
  }
}
