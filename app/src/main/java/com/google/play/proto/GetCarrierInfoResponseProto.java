// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Carrier.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import okio.ByteString;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.BYTES;
import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.STRING;

public final class GetCarrierInfoResponseProto extends Message {

  public static final Boolean DEFAULT_CARRIERCHANNELENABLED = false;
  public static final ByteString DEFAULT_CARRIERLOGOICON = ByteString.EMPTY;
  public static final ByteString DEFAULT_CARRIERBANNER = ByteString.EMPTY;
  public static final String DEFAULT_CARRIERSUBTITLE = "";
  public static final String DEFAULT_CARRIERTITLE = "";
  public static final Integer DEFAULT_CARRIERIMAGEDENSITY = 0;

  @ProtoField(tag = 1, type = BOOL)
  public final Boolean carrierChannelEnabled;

  @ProtoField(tag = 2, type = BYTES)
  public final ByteString carrierLogoIcon;

  @ProtoField(tag = 3, type = BYTES)
  public final ByteString carrierBanner;

  @ProtoField(tag = 4, type = STRING)
  public final String carrierSubtitle;

  @ProtoField(tag = 5, type = STRING)
  public final String carrierTitle;

  @ProtoField(tag = 6, type = INT32)
  public final Integer carrierImageDensity;

  public GetCarrierInfoResponseProto(Boolean carrierChannelEnabled, ByteString carrierLogoIcon, ByteString carrierBanner, String carrierSubtitle, String carrierTitle, Integer carrierImageDensity) {
    this.carrierChannelEnabled = carrierChannelEnabled;
    this.carrierLogoIcon = carrierLogoIcon;
    this.carrierBanner = carrierBanner;
    this.carrierSubtitle = carrierSubtitle;
    this.carrierTitle = carrierTitle;
    this.carrierImageDensity = carrierImageDensity;
  }

  private GetCarrierInfoResponseProto(Builder builder) {
    this(builder.carrierChannelEnabled, builder.carrierLogoIcon, builder.carrierBanner, builder.carrierSubtitle, builder.carrierTitle, builder.carrierImageDensity);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof GetCarrierInfoResponseProto)) return false;
    GetCarrierInfoResponseProto o = (GetCarrierInfoResponseProto) other;
    return equals(carrierChannelEnabled, o.carrierChannelEnabled)
        && equals(carrierLogoIcon, o.carrierLogoIcon)
        && equals(carrierBanner, o.carrierBanner)
        && equals(carrierSubtitle, o.carrierSubtitle)
        && equals(carrierTitle, o.carrierTitle)
        && equals(carrierImageDensity, o.carrierImageDensity);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = carrierChannelEnabled != null ? carrierChannelEnabled.hashCode() : 0;
      result = result * 37 + (carrierLogoIcon != null ? carrierLogoIcon.hashCode() : 0);
      result = result * 37 + (carrierBanner != null ? carrierBanner.hashCode() : 0);
      result = result * 37 + (carrierSubtitle != null ? carrierSubtitle.hashCode() : 0);
      result = result * 37 + (carrierTitle != null ? carrierTitle.hashCode() : 0);
      result = result * 37 + (carrierImageDensity != null ? carrierImageDensity.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<GetCarrierInfoResponseProto> {

    public Boolean carrierChannelEnabled;
    public ByteString carrierLogoIcon;
    public ByteString carrierBanner;
    public String carrierSubtitle;
    public String carrierTitle;
    public Integer carrierImageDensity;

    public Builder() {
    }

    public Builder(GetCarrierInfoResponseProto message) {
      super(message);
      if (message == null) return;
      this.carrierChannelEnabled = message.carrierChannelEnabled;
      this.carrierLogoIcon = message.carrierLogoIcon;
      this.carrierBanner = message.carrierBanner;
      this.carrierSubtitle = message.carrierSubtitle;
      this.carrierTitle = message.carrierTitle;
      this.carrierImageDensity = message.carrierImageDensity;
    }

    public Builder carrierChannelEnabled(Boolean carrierChannelEnabled) {
      this.carrierChannelEnabled = carrierChannelEnabled;
      return this;
    }

    public Builder carrierLogoIcon(ByteString carrierLogoIcon) {
      this.carrierLogoIcon = carrierLogoIcon;
      return this;
    }

    public Builder carrierBanner(ByteString carrierBanner) {
      this.carrierBanner = carrierBanner;
      return this;
    }

    public Builder carrierSubtitle(String carrierSubtitle) {
      this.carrierSubtitle = carrierSubtitle;
      return this;
    }

    public Builder carrierTitle(String carrierTitle) {
      this.carrierTitle = carrierTitle;
      return this;
    }

    public Builder carrierImageDensity(Integer carrierImageDensity) {
      this.carrierImageDensity = carrierImageDensity;
      return this;
    }

    @Override
    public GetCarrierInfoResponseProto build() {
      return new GetCarrierInfoResponseProto(this);
    }
  }
}
