// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Paypal.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

public final class PaypalPreapprovalCredentialsRequestProto extends Message {

  public static final String DEFAULT_GAIAAUTHTOKEN = "";
  public static final String DEFAULT_BILLINGINSTRUMENTID = "";

  @ProtoField(tag = 1, type = STRING)
  public final String gaiaAuthToken;

  @ProtoField(tag = 2, type = STRING)
  public final String billingInstrumentId;

  public PaypalPreapprovalCredentialsRequestProto(String gaiaAuthToken, String billingInstrumentId) {
    this.gaiaAuthToken = gaiaAuthToken;
    this.billingInstrumentId = billingInstrumentId;
  }

  private PaypalPreapprovalCredentialsRequestProto(Builder builder) {
    this(builder.gaiaAuthToken, builder.billingInstrumentId);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PaypalPreapprovalCredentialsRequestProto)) return false;
    PaypalPreapprovalCredentialsRequestProto o = (PaypalPreapprovalCredentialsRequestProto) other;
    return equals(gaiaAuthToken, o.gaiaAuthToken)
        && equals(billingInstrumentId, o.billingInstrumentId);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = gaiaAuthToken != null ? gaiaAuthToken.hashCode() : 0;
      result = result * 37 + (billingInstrumentId != null ? billingInstrumentId.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<PaypalPreapprovalCredentialsRequestProto> {

    public String gaiaAuthToken;
    public String billingInstrumentId;

    public Builder() {
    }

    public Builder(PaypalPreapprovalCredentialsRequestProto message) {
      super(message);
      if (message == null) return;
      this.gaiaAuthToken = message.gaiaAuthToken;
      this.billingInstrumentId = message.billingInstrumentId;
    }

    public Builder gaiaAuthToken(String gaiaAuthToken) {
      this.gaiaAuthToken = gaiaAuthToken;
      return this;
    }

    public Builder billingInstrumentId(String billingInstrumentId) {
      this.billingInstrumentId = billingInstrumentId;
      return this;
    }

    @Override
    public PaypalPreapprovalCredentialsRequestProto build() {
      return new PaypalPreapprovalCredentialsRequestProto(this);
    }
  }
}