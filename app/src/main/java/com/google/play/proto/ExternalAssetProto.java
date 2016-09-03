// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Asset.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;
import java.util.Collections;
import java.util.List;

import static com.squareup.wire.Message.Datatype.BOOL;
import static com.squareup.wire.Message.Datatype.INT32;
import static com.squareup.wire.Message.Datatype.INT64;
import static com.squareup.wire.Message.Datatype.STRING;
import static com.squareup.wire.Message.Label.REPEATED;

public final class ExternalAssetProto extends Message {

  public static final String DEFAULT_ID = "";
  public static final String DEFAULT_TITLE = "";
  public static final Integer DEFAULT_ASSETTYPE = 0;
  public static final String DEFAULT_OWNER = "";
  public static final String DEFAULT_VERSION = "";
  public static final String DEFAULT_PRICE = "";
  public static final String DEFAULT_AVERAGERATING = "";
  public static final Long DEFAULT_NUMRATINGS = 0L;
  public static final String DEFAULT_OWNERID = "";
  public static final String DEFAULT_PACKAGENAME = "";
  public static final Integer DEFAULT_VERSIONCODE = 0;
  public static final Boolean DEFAULT_BUNDLEDASSET = false;
  public static final String DEFAULT_PRICECURRENCY = "";
  public static final Long DEFAULT_PRICEMICROS = 0L;
  public static final String DEFAULT_FILTERREASON = "";
  public static final String DEFAULT_ACTUALSELLERPRICE = "";
  public static final List<ExternalBadgeProto> DEFAULT_APPBADGE = Collections.emptyList();
  public static final List<ExternalBadgeProto> DEFAULT_OWNERBADGE = Collections.emptyList();

  @ProtoField(tag = 1, type = STRING)
  public final String id;

  @ProtoField(tag = 2, type = STRING)
  public final String title;

  @ProtoField(tag = 3, type = INT32)
  public final Integer assetType;

  @ProtoField(tag = 4, type = STRING)
  public final String owner;

  @ProtoField(tag = 5, type = STRING)
  public final String version;

  @ProtoField(tag = 6, type = STRING)
  public final String price;

  @ProtoField(tag = 7, type = STRING)
  public final String averageRating;

  @ProtoField(tag = 8, type = INT64)
  public final Long numRatings;

  @ProtoField(tag = 9)
  public final PurchaseInformationProto purchaseInformation;

  @ProtoField(tag = 12)
  public final ExtendedInfoProto extendedInfo;

  @ProtoField(tag = 22, type = STRING)
  public final String ownerId;

  @ProtoField(tag = 24, type = STRING)
  public final String packageName;

  @ProtoField(tag = 25, type = INT32)
  public final Integer versionCode;

  @ProtoField(tag = 29, type = BOOL)
  public final Boolean bundledAsset;

  @ProtoField(tag = 32, type = STRING)
  public final String priceCurrency;

  @ProtoField(tag = 33, type = INT64)
  public final Long priceMicros;

  @ProtoField(tag = 35, type = STRING)
  public final String filterReason;

  @ProtoField(tag = 40, type = STRING)
  public final String actualSellerPrice;

  @ProtoField(tag = 47, label = REPEATED)
  public final List<ExternalBadgeProto> appBadge;

  @ProtoField(tag = 48, label = REPEATED)
  public final List<ExternalBadgeProto> ownerBadge;

  public ExternalAssetProto(String id, String title, Integer assetType, String owner, String version, String price, String averageRating, Long numRatings, PurchaseInformationProto purchaseInformation, ExtendedInfoProto extendedInfo, String ownerId, String packageName, Integer versionCode, Boolean bundledAsset, String priceCurrency, Long priceMicros, String filterReason, String actualSellerPrice, List<ExternalBadgeProto> appBadge, List<ExternalBadgeProto> ownerBadge) {
    this.id = id;
    this.title = title;
    this.assetType = assetType;
    this.owner = owner;
    this.version = version;
    this.price = price;
    this.averageRating = averageRating;
    this.numRatings = numRatings;
    this.purchaseInformation = purchaseInformation;
    this.extendedInfo = extendedInfo;
    this.ownerId = ownerId;
    this.packageName = packageName;
    this.versionCode = versionCode;
    this.bundledAsset = bundledAsset;
    this.priceCurrency = priceCurrency;
    this.priceMicros = priceMicros;
    this.filterReason = filterReason;
    this.actualSellerPrice = actualSellerPrice;
    this.appBadge = immutableCopyOf(appBadge);
    this.ownerBadge = immutableCopyOf(ownerBadge);
  }

  private ExternalAssetProto(Builder builder) {
    this(builder.id, builder.title, builder.assetType, builder.owner, builder.version, builder.price, builder.averageRating, builder.numRatings, builder.purchaseInformation, builder.extendedInfo, builder.ownerId, builder.packageName, builder.versionCode, builder.bundledAsset, builder.priceCurrency, builder.priceMicros, builder.filterReason, builder.actualSellerPrice, builder.appBadge, builder.ownerBadge);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof ExternalAssetProto)) return false;
    ExternalAssetProto o = (ExternalAssetProto) other;
    return equals(id, o.id)
        && equals(title, o.title)
        && equals(assetType, o.assetType)
        && equals(owner, o.owner)
        && equals(version, o.version)
        && equals(price, o.price)
        && equals(averageRating, o.averageRating)
        && equals(numRatings, o.numRatings)
        && equals(purchaseInformation, o.purchaseInformation)
        && equals(extendedInfo, o.extendedInfo)
        && equals(ownerId, o.ownerId)
        && equals(packageName, o.packageName)
        && equals(versionCode, o.versionCode)
        && equals(bundledAsset, o.bundledAsset)
        && equals(priceCurrency, o.priceCurrency)
        && equals(priceMicros, o.priceMicros)
        && equals(filterReason, o.filterReason)
        && equals(actualSellerPrice, o.actualSellerPrice)
        && equals(appBadge, o.appBadge)
        && equals(ownerBadge, o.ownerBadge);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = id != null ? id.hashCode() : 0;
      result = result * 37 + (title != null ? title.hashCode() : 0);
      result = result * 37 + (assetType != null ? assetType.hashCode() : 0);
      result = result * 37 + (owner != null ? owner.hashCode() : 0);
      result = result * 37 + (version != null ? version.hashCode() : 0);
      result = result * 37 + (price != null ? price.hashCode() : 0);
      result = result * 37 + (averageRating != null ? averageRating.hashCode() : 0);
      result = result * 37 + (numRatings != null ? numRatings.hashCode() : 0);
      result = result * 37 + (purchaseInformation != null ? purchaseInformation.hashCode() : 0);
      result = result * 37 + (extendedInfo != null ? extendedInfo.hashCode() : 0);
      result = result * 37 + (ownerId != null ? ownerId.hashCode() : 0);
      result = result * 37 + (packageName != null ? packageName.hashCode() : 0);
      result = result * 37 + (versionCode != null ? versionCode.hashCode() : 0);
      result = result * 37 + (bundledAsset != null ? bundledAsset.hashCode() : 0);
      result = result * 37 + (priceCurrency != null ? priceCurrency.hashCode() : 0);
      result = result * 37 + (priceMicros != null ? priceMicros.hashCode() : 0);
      result = result * 37 + (filterReason != null ? filterReason.hashCode() : 0);
      result = result * 37 + (actualSellerPrice != null ? actualSellerPrice.hashCode() : 0);
      result = result * 37 + (appBadge != null ? appBadge.hashCode() : 1);
      result = result * 37 + (ownerBadge != null ? ownerBadge.hashCode() : 1);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<ExternalAssetProto> {

    public String id;
    public String title;
    public Integer assetType;
    public String owner;
    public String version;
    public String price;
    public String averageRating;
    public Long numRatings;
    public PurchaseInformationProto purchaseInformation;
    public ExtendedInfoProto extendedInfo;
    public String ownerId;
    public String packageName;
    public Integer versionCode;
    public Boolean bundledAsset;
    public String priceCurrency;
    public Long priceMicros;
    public String filterReason;
    public String actualSellerPrice;
    public List<ExternalBadgeProto> appBadge;
    public List<ExternalBadgeProto> ownerBadge;

    public Builder() {
    }

    public Builder(ExternalAssetProto message) {
      super(message);
      if (message == null) return;
      this.id = message.id;
      this.title = message.title;
      this.assetType = message.assetType;
      this.owner = message.owner;
      this.version = message.version;
      this.price = message.price;
      this.averageRating = message.averageRating;
      this.numRatings = message.numRatings;
      this.purchaseInformation = message.purchaseInformation;
      this.extendedInfo = message.extendedInfo;
      this.ownerId = message.ownerId;
      this.packageName = message.packageName;
      this.versionCode = message.versionCode;
      this.bundledAsset = message.bundledAsset;
      this.priceCurrency = message.priceCurrency;
      this.priceMicros = message.priceMicros;
      this.filterReason = message.filterReason;
      this.actualSellerPrice = message.actualSellerPrice;
      this.appBadge = copyOf(message.appBadge);
      this.ownerBadge = copyOf(message.ownerBadge);
    }

    public Builder id(String id) {
      this.id = id;
      return this;
    }

    public Builder title(String title) {
      this.title = title;
      return this;
    }

    public Builder assetType(Integer assetType) {
      this.assetType = assetType;
      return this;
    }

    public Builder owner(String owner) {
      this.owner = owner;
      return this;
    }

    public Builder version(String version) {
      this.version = version;
      return this;
    }

    public Builder price(String price) {
      this.price = price;
      return this;
    }

    public Builder averageRating(String averageRating) {
      this.averageRating = averageRating;
      return this;
    }

    public Builder numRatings(Long numRatings) {
      this.numRatings = numRatings;
      return this;
    }

    public Builder purchaseInformation(PurchaseInformationProto purchaseInformation) {
      this.purchaseInformation = purchaseInformation;
      return this;
    }

    public Builder extendedInfo(ExtendedInfoProto extendedInfo) {
      this.extendedInfo = extendedInfo;
      return this;
    }

    public Builder ownerId(String ownerId) {
      this.ownerId = ownerId;
      return this;
    }

    public Builder packageName(String packageName) {
      this.packageName = packageName;
      return this;
    }

    public Builder versionCode(Integer versionCode) {
      this.versionCode = versionCode;
      return this;
    }

    public Builder bundledAsset(Boolean bundledAsset) {
      this.bundledAsset = bundledAsset;
      return this;
    }

    public Builder priceCurrency(String priceCurrency) {
      this.priceCurrency = priceCurrency;
      return this;
    }

    public Builder priceMicros(Long priceMicros) {
      this.priceMicros = priceMicros;
      return this;
    }

    public Builder filterReason(String filterReason) {
      this.filterReason = filterReason;
      return this;
    }

    public Builder actualSellerPrice(String actualSellerPrice) {
      this.actualSellerPrice = actualSellerPrice;
      return this;
    }

    public Builder appBadge(List<ExternalBadgeProto> appBadge) {
      this.appBadge = checkForNulls(appBadge);
      return this;
    }

    public Builder ownerBadge(List<ExternalBadgeProto> ownerBadge) {
      this.ownerBadge = checkForNulls(ownerBadge);
      return this;
    }

    @Override
    public ExternalAssetProto build() {
      return new ExternalAssetProto(this);
    }
  }
}
