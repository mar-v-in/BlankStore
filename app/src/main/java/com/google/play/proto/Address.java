// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/BillingAddress.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.STRING;

public final class Address extends Message {

  public static final String DEFAULT_NAME = "";
  public static final String DEFAULT_ADDRESSLINE1 = "";
  public static final String DEFAULT_ADDRESSLINE2 = "";
  public static final String DEFAULT_CITY = "";
  public static final String DEFAULT_STATE = "";
  public static final String DEFAULT_POSTALCODE = "";
  public static final String DEFAULT_POSTALCOUNTRY = "";
  public static final String DEFAULT_DEPENDENTLOCALITY = "";
  public static final String DEFAULT_SORTINGCODE = "";
  public static final String DEFAULT_LANGUAGECODE = "";
  public static final String DEFAULT_PHONENUMBER = "";
  public static final Boolean DEFAULT_ISREDUCED = false;
  public static final String DEFAULT_FIRSTNAME = "";
  public static final String DEFAULT_LASTNAME = "";
  public static final String DEFAULT_EMAIL = "";

  @ProtoField(tag = 1, type = STRING)
  public final String name;

  @ProtoField(tag = 2, type = STRING)
  public final String addressLine1;

  @ProtoField(tag = 3, type = STRING)
  public final String addressLine2;

  @ProtoField(tag = 4, type = STRING)
  public final String city;

  @ProtoField(tag = 5, type = STRING)
  public final String state;

  @ProtoField(tag = 6, type = STRING)
  public final String postalCode;

  @ProtoField(tag = 7, type = STRING)
  public final String postalCountry;

  @ProtoField(tag = 8, type = STRING)
  public final String dependentLocality;

  @ProtoField(tag = 9, type = STRING)
  public final String sortingCode;

  @ProtoField(tag = 10, type = STRING)
  public final String languageCode;

  @ProtoField(tag = 11, type = STRING)
  public final String phoneNumber;

  @ProtoField(tag = 12, type = BOOL)
  public final Boolean isReduced;

  @ProtoField(tag = 13, type = STRING)
  public final String firstName;

  @ProtoField(tag = 14, type = STRING)
  public final String lastName;

  @ProtoField(tag = 15, type = STRING)
  public final String email;

  public Address(String name, String addressLine1, String addressLine2, String city, String state, String postalCode, String postalCountry, String dependentLocality, String sortingCode, String languageCode, String phoneNumber, Boolean isReduced, String firstName, String lastName, String email) {
    this.name = name;
    this.addressLine1 = addressLine1;
    this.addressLine2 = addressLine2;
    this.city = city;
    this.state = state;
    this.postalCode = postalCode;
    this.postalCountry = postalCountry;
    this.dependentLocality = dependentLocality;
    this.sortingCode = sortingCode;
    this.languageCode = languageCode;
    this.phoneNumber = phoneNumber;
    this.isReduced = isReduced;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  private Address(Builder builder) {
    this(builder.name, builder.addressLine1, builder.addressLine2, builder.city, builder.state, builder.postalCode, builder.postalCountry, builder.dependentLocality, builder.sortingCode, builder.languageCode, builder.phoneNumber, builder.isReduced, builder.firstName, builder.lastName, builder.email);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Address)) return false;
    Address o = (Address) other;
    return equals(name, o.name)
        && equals(addressLine1, o.addressLine1)
        && equals(addressLine2, o.addressLine2)
        && equals(city, o.city)
        && equals(state, o.state)
        && equals(postalCode, o.postalCode)
        && equals(postalCountry, o.postalCountry)
        && equals(dependentLocality, o.dependentLocality)
        && equals(sortingCode, o.sortingCode)
        && equals(languageCode, o.languageCode)
        && equals(phoneNumber, o.phoneNumber)
        && equals(isReduced, o.isReduced)
        && equals(firstName, o.firstName)
        && equals(lastName, o.lastName)
        && equals(email, o.email);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = name != null ? name.hashCode() : 0;
      result = result * 37 + (addressLine1 != null ? addressLine1.hashCode() : 0);
      result = result * 37 + (addressLine2 != null ? addressLine2.hashCode() : 0);
      result = result * 37 + (city != null ? city.hashCode() : 0);
      result = result * 37 + (state != null ? state.hashCode() : 0);
      result = result * 37 + (postalCode != null ? postalCode.hashCode() : 0);
      result = result * 37 + (postalCountry != null ? postalCountry.hashCode() : 0);
      result = result * 37 + (dependentLocality != null ? dependentLocality.hashCode() : 0);
      result = result * 37 + (sortingCode != null ? sortingCode.hashCode() : 0);
      result = result * 37 + (languageCode != null ? languageCode.hashCode() : 0);
      result = result * 37 + (phoneNumber != null ? phoneNumber.hashCode() : 0);
      result = result * 37 + (isReduced != null ? isReduced.hashCode() : 0);
      result = result * 37 + (firstName != null ? firstName.hashCode() : 0);
      result = result * 37 + (lastName != null ? lastName.hashCode() : 0);
      result = result * 37 + (email != null ? email.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<Address> {

    public String name;
    public String addressLine1;
    public String addressLine2;
    public String city;
    public String state;
    public String postalCode;
    public String postalCountry;
    public String dependentLocality;
    public String sortingCode;
    public String languageCode;
    public String phoneNumber;
    public Boolean isReduced;
    public String firstName;
    public String lastName;
    public String email;

    public Builder() {
    }

    public Builder(Address message) {
      super(message);
      if (message == null) return;
      this.name = message.name;
      this.addressLine1 = message.addressLine1;
      this.addressLine2 = message.addressLine2;
      this.city = message.city;
      this.state = message.state;
      this.postalCode = message.postalCode;
      this.postalCountry = message.postalCountry;
      this.dependentLocality = message.dependentLocality;
      this.sortingCode = message.sortingCode;
      this.languageCode = message.languageCode;
      this.phoneNumber = message.phoneNumber;
      this.isReduced = message.isReduced;
      this.firstName = message.firstName;
      this.lastName = message.lastName;
      this.email = message.email;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder addressLine1(String addressLine1) {
      this.addressLine1 = addressLine1;
      return this;
    }

    public Builder addressLine2(String addressLine2) {
      this.addressLine2 = addressLine2;
      return this;
    }

    public Builder city(String city) {
      this.city = city;
      return this;
    }

    public Builder state(String state) {
      this.state = state;
      return this;
    }

    public Builder postalCode(String postalCode) {
      this.postalCode = postalCode;
      return this;
    }

    public Builder postalCountry(String postalCountry) {
      this.postalCountry = postalCountry;
      return this;
    }

    public Builder dependentLocality(String dependentLocality) {
      this.dependentLocality = dependentLocality;
      return this;
    }

    public Builder sortingCode(String sortingCode) {
      this.sortingCode = sortingCode;
      return this;
    }

    public Builder languageCode(String languageCode) {
      this.languageCode = languageCode;
      return this;
    }

    public Builder phoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
      return this;
    }

    public Builder isReduced(Boolean isReduced) {
      this.isReduced = isReduced;
      return this;
    }

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    @Override
    public Address build() {
      return new Address(this);
    }
  }
}
