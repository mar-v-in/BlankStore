// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Empty.proto
package com.google.play.proto;

import com.squareup.wire.Message;

public final class Empty extends Message {

  public Empty() {
  }

  private Empty(Builder builder) {
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Empty;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public static final class Builder extends Message.Builder<Empty> {

    public Builder() {
    }

    public Builder(Empty message) {
      super(message);
    }

    @Override
    public Empty build() {
      return new Empty(this);
    }
  }
}