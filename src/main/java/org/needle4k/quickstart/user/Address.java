package org.needle4k.quickstart.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = Address.TABLE_NAME, uniqueConstraints = { @UniqueConstraint(columnNames = { "STRASSE" }) })
public class Address
{
  public static final String TABLE_NAME = "NEEDLE_TEST_ADDRESS";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "STRASSE", nullable = false)
  private String street = "";

  @Column(name = "PLZ", nullable = false)
  private String zip = "";

  @ManyToOne
  private Person person;

  // JPA
  protected Address()
  {
  }

  public Address(final String street, final String zip)
  {
    assert street != null : "assert street != null";
    assert zip != null : "assert zip != null";

    this.street = street;
    this.zip = zip;
  }

  public long getId()
  {
    return id;
  }

  public String getStreet()
  {
    return street;
  }

  public void setStreet(String street)
  {
    this.street = street;
  }

  public String getZip()
  {
    return zip;
  }

  public void setZip(String zip)
  {
    this.zip = zip;
  }

  public Person getPerson()
  {
    return person;
  }

  void setPerson(final Person person)
  {
    this.person = person;
  }
}
