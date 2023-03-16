package org.needle4k.quickstart.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = Person.TABLE_NAME)
public class Person {
  public static final String TABLE_NAME = "TEST_PERSON";

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
}
