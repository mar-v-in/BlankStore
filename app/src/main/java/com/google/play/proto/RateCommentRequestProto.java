// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Comment.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.STRING;

public final class RateCommentRequestProto extends Message {

  public static final String DEFAULT_ASSETID = "";
  public static final String DEFAULT_CREATORID = "";
  public static final Integer DEFAULT_COMMENTRATING = 0;

  @ProtoField(tag = 1, type = STRING)
  public final String assetId;

  @ProtoField(tag = 2, type = STRING)
  public final String creatorId;

  @ProtoField(tag = 3, type = INT32)
  public final Integer commentRating;

  public RateCommentRequestProto(String assetId, String creatorId, Integer commentRating) {
    this.assetId = assetId;
    this.creatorId = creatorId;
    this.commentRating = commentRating;
  }

  private RateCommentRequestProto(Builder builder) {
    this(builder.assetId, builder.creatorId, builder.commentRating);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof RateCommentRequestProto)) return false;
    RateCommentRequestProto o = (RateCommentRequestProto) other;
    return equals(assetId, o.assetId)
        && equals(creatorId, o.creatorId)
        && equals(commentRating, o.commentRating);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = assetId != null ? assetId.hashCode() : 0;
      result = result * 37 + (creatorId != null ? creatorId.hashCode() : 0);
      result = result * 37 + (commentRating != null ? commentRating.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<RateCommentRequestProto> {

    public String assetId;
    public String creatorId;
    public Integer commentRating;

    public Builder() {
    }

    public Builder(RateCommentRequestProto message) {
      super(message);
      if (message == null) return;
      this.assetId = message.assetId;
      this.creatorId = message.creatorId;
      this.commentRating = message.commentRating;
    }

    public Builder assetId(String assetId) {
      this.assetId = assetId;
      return this;
    }

    public Builder creatorId(String creatorId) {
      this.creatorId = creatorId;
      return this;
    }

    public Builder commentRating(Integer commentRating) {
      this.commentRating = commentRating;
      return this;
    }

    @Override
    public RateCommentRequestProto build() {
      return new RateCommentRequestProto(this);
    }
  }
}
