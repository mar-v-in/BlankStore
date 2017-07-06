// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Billing.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.STRING;

public final class CarrierBillingConfig extends Message {

  public static final String DEFAULT_ID = "";
  public static final String DEFAULT_NAME = "";
  public static final Integer DEFAULT_APIVERSION = 0;
  public static final String DEFAULT_PROVISIONINGURL = "";
  public static final String DEFAULT_CREDENTIALSURL = "";
  public static final Boolean DEFAULT_TOSREQUIRED = false;
  public static final Boolean DEFAULT_PERTRANSACTIONCREDENTIALSREQUIRED = false;
  public static final Boolean DEFAULT_SENDSUBSCRIBERIDWITHCARRIERBILLINGREQUESTS = false;

  @ProtoField(tag = 1, type = STRING)
  public final String id;

  @ProtoField(tag = 2, type = STRING)
  public final String name;

  @ProtoField(tag = 3, type = INT32)
  public final Integer apiVersion;

  @ProtoField(tag = 4, type = STRING)
  public final String provisioningUrl;

  @ProtoField(tag = 5, type = STRING)
  public final String credentialsUrl;

  @ProtoField(tag = 6, type = BOOL)
  public final Boolean tosRequired;

  @ProtoField(tag = 7, type = BOOL)
  public final Boolean perTransactionCredentialsRequired;

  @ProtoField(tag = 8, type = BOOL)
  public final Boolean sendSubscriberIdWithCarrierBillingRequests;

  public CarrierBillingConfig(String id, String name, Integer apiVersion, String provisioningUrl, String credentialsUrl, Boolean tosRequired, Boolean perTransactionCredentialsRequired, Boolean sendSubscriberIdWithCarrierBillingRequests) {
    this.id = id;
    this.name = name;
    this.apiVersion = apiVersion;
    this.provisioningUrl = provisioningUrl;
    this.credentialsUrl = credentialsUrl;
    this.tosRequired = tosRequired;
    this.perTransactionCredentialsRequired = perTransactionCredentialsRequired;
    this.sendSubscriberIdWithCarrierBillingRequests = sendSubscriberIdWithCarrierBillingRequests;
  }

  private CarrierBillingConfig(Builder builder) {
    this(builder.id, builder.name, builder.apiVersion, builder.provisioningUrl, builder.credentialsUrl, builder.tosRequired, builder.perTransactionCredentialsRequired, builder.sendSubscriberIdWithCarrierBillingRequests);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof CarrierBillingConfig)) return false;
    CarrierBillingConfig o = (CarrierBillingConfig) other;
    return equals(id, o.id)
        && equals(name, o.name)
        && equals(apiVersion, o.apiVersion)
        && equals(provisioningUrl, o.provisioningUrl)
        && equals(credentialsUrl, o.credentialsUrl)
        && equals(tosRequired, o.tosRequired)
        && equals(perTransactionCredentialsRequired, o.perTransactionCredentialsRequired)
        && equals(sendSubscriberIdWithCarrierBillingRequests, o.sendSubscriberIdWithCarrierBillingRequests);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = id != null ? id.hashCode() : 0;
      result = result * 37 + (name != null ? name.hashCode() : 0);
      result = result * 37 + (apiVersion != null ? apiVersion.hashCode() : 0);
      result = result * 37 + (provisioningUrl != null ? provisioningUrl.hashCode() : 0);
      result = result * 37 + (credentialsUrl != null ? credentialsUrl.hashCode() : 0);
      result = result * 37 + (tosRequired != null ? tosRequired.hashCode() : 0);
      result = result * 37 + (perTransactionCredentialsRequired != null ? perTransactionCredentialsRequired.hashCode() : 0);
      result = result * 37 + (sendSubscriberIdWithCarrierBillingRequests != null ? sendSubscriberIdWithCarrierBillingRequests.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<CarrierBillingConfig> {

    public String id;
    public String name;
    public Integer apiVersion;
    public String provisioningUrl;
    public String credentialsUrl;
    public Boolean tosRequired;
    public Boolean perTransactionCredentialsRequired;
    public Boolean sendSubscriberIdWithCarrierBillingRequests;

    public Builder() {
    }

    public Builder(CarrierBillingConfig message) {
      super(message);
      if (message == null) return;
      this.id = message.id;
      this.name = message.name;
      this.apiVersion = message.apiVersion;
      this.provisioningUrl = message.provisioningUrl;
      this.credentialsUrl = message.credentialsUrl;
      this.tosRequired = message.tosRequired;
      this.perTransactionCredentialsRequired = message.perTransactionCredentialsRequired;
      this.sendSubscriberIdWithCarrierBillingRequests = message.sendSubscriberIdWithCarrierBillingRequests;
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder apiVersion(Integer apiVersion) {
      this.apiVersion = apiVersion;
      return this;
    }

    public Builder provisioningUrl(String provisioningUrl) {
      this.provisioningUrl = provisioningUrl;
      return this;
    }

    public Builder credentialsUrl(String credentialsUrl) {
      this.credentialsUrl = credentialsUrl;
      return this;
    }

    public Builder tosRequired(Boolean tosRequired) {
      this.tosRequired = tosRequired;
      return this;
    }

    public Builder perTransactionCredentialsRequired(Boolean perTransactionCredentialsRequired) {
      this.perTransactionCredentialsRequired = perTransactionCredentialsRequired;
      return this;
    }

    public Builder sendSubscriberIdWithCarrierBillingRequests(Boolean sendSubscriberIdWithCarrierBillingRequests) {
      this.sendSubscriberIdWithCarrierBillingRequests = sendSubscriberIdWithCarrierBillingRequests;
      return this;
    }

    @Override
    public CarrierBillingConfig build() {
      return new CarrierBillingConfig(this);
    }
  }
}
