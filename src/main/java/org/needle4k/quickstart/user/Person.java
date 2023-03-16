package org.needle4k.quickstart.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = Person.TABLE_NAME)
@NamedQuery(name = Person.FIND_BY_STREET, query = "SELECT p FROM Person p INNER JOIN p.addresses a WHERE a.street = :street")
public class Person {
  public static final String TABLE_NAME = "TEST_PERSON";
  public static final String FIND_BY_STREET = "FIND_BY_STREET";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(nullable = false)
  private String name;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
  private List<Address> addresses = new ArrayList<>();

  // JPA
  protected Person() {}

  public Person(final String name, final Address... addresses) {
    assert name != null : "assert name != null";
    this.name = name;

    Arrays.stream(addresses).forEach(this::addAddress);
  }

  public void addAddress(final Address address) {
    assert address != null : "assert address != null";

    address.setPerson(this);
    addresses.add(address);
  }

  public String getName() {
    return name;
  }

  public long getId() {
    return id;
  }

  public List<Address> getAddresses()
  {
    return new ArrayList<>(addresses);
  }

  @Override
  public boolean equals(final Object o)
  {
    if (this == o)
    {return true;}
    if (o == null || getClass() != o.getClass())
    {return false;}
    final Person person = (Person) o;
    return getId() == person.getId() && getName().equals(person.getName());
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(getId(), getName());
  }
}
