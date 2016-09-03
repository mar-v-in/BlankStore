// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Offers.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.FIXED64;
import static com.squareup.wire.Message.Datatype.INT32;

public final class Install extends Message {

  public static final Long DEFAULT_ANDROIDID = 0L;
  public static final Integer DEFAULT_VERSION = 0;
  public static final Boolean DEFAULT_BUNDLED = false;

  @ProtoField(tag = 1, type = FIXED64)
  public final Long androidId;

  @ProtoField(tag = 2, type = INT32)
  public final Integer version;

  @ProtoField(tag = 3, type = BOOL)
  public final Boolean bundled;

  public Install(Long androidId, Integer version, Boolean bundled) {
    this.androidId = androidId;
    this.version = version;
    this.bundled = bundled;
  }

  private Install(Builder builder) {
    this(builder.androidId, builder.version, builder.bundled);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Install)) return false;
    Install o = (Install) other;
    return equals(androidId, o.androidId)
        && equals(version, o.version)
        && equals(bundled, o.bundled);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = androidId != null ? androidId.hashCode() : 0;
      result = result * 37 + (version != null ? version.hashCode() : 0);
      result = result * 37 + (bundled != null ? bundled.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<Install> {

    public Long androidId;
    public Integer version;
    public Boolean bundled;

    public Builder() {
    }

    public Builder(Install message) {
      super(message);
      if (message == null) return;
      this.androidId = message.androidId;
      this.version = message.version;
      this.bundled = message.bundled;
    }

    public Builder androidId(Long androidId) {
      this.androidId = androidId;
      return this;
    }

    public Builder version(Integer version) {
      this.version = version;
      return this;
    }

    public Builder bundled(Boolean bundled) {
      this.bundled = bundled;
      return this;
    }

    @Override
    public Install build() {
      return new Install(this);
    }
  }
}
