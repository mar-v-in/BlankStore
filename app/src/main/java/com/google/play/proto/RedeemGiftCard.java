// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Browse.proto
package com.google.play.proto;

import com.squareup.wire.Message;

public final class RedeemGiftCard extends Message {

  public RedeemGiftCard() {
  }

  private RedeemGiftCard(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof RedeemGiftCard;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<RedeemGiftCard> {

    public Builder() {
    }

    public Builder(RedeemGiftCard message) {
      super(message);
    }

    @Override
    public RedeemGiftCard build() {
      return new RedeemGiftCard(this);
    }
  }
}
