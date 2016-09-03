// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Documents.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

import static com.squareup.wire.Message.Datatype.STRING;

public final class DealOfTheDay extends Message {

  public static final String DEFAULT_FEATUREDHEADER = "";
  public static final String DEFAULT_COLORTHEMEARGB = "";

  @ProtoField(tag = 1, type = STRING)
  public final String featuredHeader;

  @ProtoField(tag = 2, type = STRING)
  public final String colorThemeArgb;

  public DealOfTheDay(String featuredHeader, String colorThemeArgb) {
    this.featuredHeader = featuredHeader;
    this.colorThemeArgb = colorThemeArgb;
  }

  private DealOfTheDay(Builder builder) {
    this(builder.featuredHeader, builder.colorThemeArgb);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof DealOfTheDay)) return false;
    DealOfTheDay o = (DealOfTheDay) other;
    return equals(featuredHeader, o.featuredHeader)
        && equals(colorThemeArgb, o.colorThemeArgb);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = featuredHeader != null ? featuredHeader.hashCode() : 0;
      result = result * 37 + (colorThemeArgb != null ? colorThemeArgb.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<DealOfTheDay> {

    public String featuredHeader;
    public String colorThemeArgb;

    public Builder() {
    }

    public Builder(DealOfTheDay message) {
      super(message);
      if (message == null) return;
      this.featuredHeader = message.featuredHeader;
      this.colorThemeArgb = message.colorThemeArgb;
    }

    public Builder featuredHeader(String featuredHeader) {
      this.featuredHeader = featuredHeader;
      return this;
    }

    public Builder colorThemeArgb(String colorThemeArgb) {
      this.colorThemeArgb = colorThemeArgb;
      return this;
    }

    @Override
    public DealOfTheDay build() {
      return new DealOfTheDay(this);
    }
  }
}
