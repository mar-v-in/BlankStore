// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Unsorted.proto
package com.google.play.proto;

import com.squareup.wire.Message;

public final class AcceptTosResponse extends Message {

  public AcceptTosResponse() {
  }

  private AcceptTosResponse(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof AcceptTosResponse;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<AcceptTosResponse> {

    public Builder() {
    }

    public Builder(AcceptTosResponse message) {
      super(message);
    }

    @Override
    public AcceptTosResponse build() {
      return new AcceptTosResponse(this);
    }
  }
}
