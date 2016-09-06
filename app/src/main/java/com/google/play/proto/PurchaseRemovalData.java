// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Purchase.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.BOOL;

public final class PurchaseRemovalData extends Message {

  public static final Boolean DEFAULT_MALICIOUS = false;

  @ProtoField(tag = 1, type = BOOL)
  public final Boolean malicious;

  public PurchaseRemovalData(Boolean malicious) {
    this.malicious = malicious;
  }

  private PurchaseRemovalData(Builder builder) {
    this(builder.malicious);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof PurchaseRemovalData)) return false;
    return equals(malicious, ((PurchaseRemovalData) other).malicious);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    return result != 0 ? result : (hashCode = malicious != null ? malicious.hashCode() : 0);
  }

  public static final class Builder extends Message.Builder<PurchaseRemovalData> {

    public Boolean malicious;

    public Builder() {
    }

    public Builder(PurchaseRemovalData message) {
      super(message);
      if (message == null) return;
      this.malicious = message.malicious;
    }

    public Builder malicious(Boolean malicious) {
      this.malicious = malicious;
      return this;
    }

    @Override
    public PurchaseRemovalData build() {
      return new PurchaseRemovalData(this);
    }
  }
}