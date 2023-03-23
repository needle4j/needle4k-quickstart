package org.needle4k.quickstart.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockingDetails;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.Session;
import org.hsqldb.jdbc.JDBCConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.needle4k.db.TransactionHelper;
import org.needle4k.junit5.JPANeedleExtension;
import org.needle4k.quickstart.user.Address;
import org.needle4k.quickstart.user.Person;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Test basic JPA functionality
 */
@SuppressWarnings({ "CdiInjectionPointsInspection", "SqlNoDataSourceInspection" })
@ExtendWith(JPANeedleExtension.class)
public class EntityManagerTest
{
  @Inject
  private TransactionHelper transactionHelper;

  @PersistenceContext
  private EntityManager entityManager;

  @Resource
  private DataSource dataSource;

  private final Person person = new Person("demo", new Address("Bülowstr. 66", "10783 Berlin"));

  @BeforeEach
  public void setup() {
    transactionHelper.saveObject(person);
  }

  @Test
  public void testDataSource() throws SQLException
  {
    assertThat(dataSource).isNotNull();
    assertThat(mockingDetails(dataSource).isMock()).isFalse();

    try(final PreparedStatement statement = dataSource.getConnection()
        .prepareStatement("SELECT p.ID FROM " + Person.TABLE_NAME + " p WHERE p.name = ?")) {
      statement.setString(1, "demo");
      final ResultSet resultSet = statement.executeQuery();

      assertThat(resultSet.next()).isTrue();
      final long id = resultSet.getLong("ID");

      assertThat(id).isGreaterThan(0);
    }
  }

  @Test
  public void testFindByName()
  {
    final Person personFromDB = (Person) entityManager
        .createQuery("SELECT p FROM Person p WHERE p.name = :name")
        .setParameter("name", "demo").getSingleResult();

    assertThat(personFromDB).isEqualTo(person);
    assertThat(personFromDB.getAddresses()).hasSize(1);

    final Session session = entityManager.unwrap(Session.class);
    session.doWork(connection -> assertThat(connection.getClass()).isEqualTo(JDBCConnection.class));
  }
}
