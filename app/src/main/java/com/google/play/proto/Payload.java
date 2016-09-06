// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: proto/Requests.proto
package com.google.play.proto;

import com.squareup.wire.Message;
import com.squareup.wire.ProtoField;

public final class Payload extends Message {

  @ProtoField(tag = 1)
  public final ListResponse listResponse;

  @ProtoField(tag = 2)
  public final DetailsResponse detailsResponse;

  @ProtoField(tag = 3)
  public final ReviewResponse reviewResponse;

  @ProtoField(tag = 4)
  public final BuyResponse buyResponse;

  @ProtoField(tag = 5)
  public final SearchResponse searchResponse;

  @ProtoField(tag = 6)
  public final TocResponse tocResponse;

  @ProtoField(tag = 7)
  public final BrowseResponse browseResponse;

  @ProtoField(tag = 8)
  public final PurchaseStatusResponse purchaseStatusResponse;

  @ProtoField(tag = 9)
  public final UpdateInstrumentResponse updateInstrumentResponse;

  @ProtoField(tag = 10)
  public final LogResponse logResponse;

  @ProtoField(tag = 11)
  public final CheckInstrumentResponse checkInstrumentResponse;

  @ProtoField(tag = 12)
  public final PlusOneResponse plusOneResponse;

  @ProtoField(tag = 13)
  public final FlagContentResponse flagContentResponse;

  @ProtoField(tag = 14)
  public final AckNotificationResponse ackNotificationResponse;

  @ProtoField(tag = 15)
  public final InitiateAssociationResponse initiateAssociationResponse;

  @ProtoField(tag = 16)
  public final VerifyAssociationResponse verifyAssociationResponse;

  @ProtoField(tag = 17)
  public final LibraryReplicationResponse libraryReplicationResponse;

  @ProtoField(tag = 18)
  public final RevokeResponse revokeResponse;

  @ProtoField(tag = 19)
  public final BulkDetailsResponse bulkDetailsResponse;

  @ProtoField(tag = 20)
  public final ResolveLinkResponse resolveLinkResponse;

  @ProtoField(tag = 21)
  public final DeliveryResponse deliveryResponse;

  @ProtoField(tag = 22)
  public final AcceptTosResponse acceptTosResponse;

  @ProtoField(tag = 23)
  public final RateSuggestedContentResponse rateSuggestedContentResponse;

  @ProtoField(tag = 24)
  public final CheckPromoOfferResponse checkPromoOfferResponse;

  @ProtoField(tag = 28)
  public final UploadDeviceConfigResponse uploadDeviceConfigResponse;

  public Payload(ListResponse listResponse, DetailsResponse detailsResponse, ReviewResponse reviewResponse, BuyResponse buyResponse, SearchResponse searchResponse, TocResponse tocResponse, BrowseResponse browseResponse, PurchaseStatusResponse purchaseStatusResponse, UpdateInstrumentResponse updateInstrumentResponse, LogResponse logResponse, CheckInstrumentResponse checkInstrumentResponse, PlusOneResponse plusOneResponse, FlagContentResponse flagContentResponse, AckNotificationResponse ackNotificationResponse, InitiateAssociationResponse initiateAssociationResponse, VerifyAssociationResponse verifyAssociationResponse, LibraryReplicationResponse libraryReplicationResponse, RevokeResponse revokeResponse, BulkDetailsResponse bulkDetailsResponse, ResolveLinkResponse resolveLinkResponse, DeliveryResponse deliveryResponse, AcceptTosResponse acceptTosResponse, RateSuggestedContentResponse rateSuggestedContentResponse, CheckPromoOfferResponse checkPromoOfferResponse, UploadDeviceConfigResponse uploadDeviceConfigResponse) {
    this.listResponse = listResponse;
    this.detailsResponse = detailsResponse;
    this.reviewResponse = reviewResponse;
    this.buyResponse = buyResponse;
    this.searchResponse = searchResponse;
    this.tocResponse = tocResponse;
    this.browseResponse = browseResponse;
    this.purchaseStatusResponse = purchaseStatusResponse;
    this.updateInstrumentResponse = updateInstrumentResponse;
    this.logResponse = logResponse;
    this.checkInstrumentResponse = checkInstrumentResponse;
    this.plusOneResponse = plusOneResponse;
    this.flagContentResponse = flagContentResponse;
    this.ackNotificationResponse = ackNotificationResponse;
    this.initiateAssociationResponse = initiateAssociationResponse;
    this.verifyAssociationResponse = verifyAssociationResponse;
    this.libraryReplicationResponse = libraryReplicationResponse;
    this.revokeResponse = revokeResponse;
    this.bulkDetailsResponse = bulkDetailsResponse;
    this.resolveLinkResponse = resolveLinkResponse;
    this.deliveryResponse = deliveryResponse;
    this.acceptTosResponse = acceptTosResponse;
    this.rateSuggestedContentResponse = rateSuggestedContentResponse;
    this.checkPromoOfferResponse = checkPromoOfferResponse;
    this.uploadDeviceConfigResponse = uploadDeviceConfigResponse;
  }

  private Payload(Builder builder) {
    this(builder.listResponse, builder.detailsResponse, builder.reviewResponse, builder.buyResponse, builder.searchResponse, builder.tocResponse, builder.browseResponse, builder.purchaseStatusResponse, builder.updateInstrumentResponse, builder.logResponse, builder.checkInstrumentResponse, builder.plusOneResponse, builder.flagContentResponse, builder.ackNotificationResponse, builder.initiateAssociationResponse, builder.verifyAssociationResponse, builder.libraryReplicationResponse, builder.revokeResponse, builder.bulkDetailsResponse, builder.resolveLinkResponse, builder.deliveryResponse, builder.acceptTosResponse, builder.rateSuggestedContentResponse, builder.checkPromoOfferResponse, builder.uploadDeviceConfigResponse);
    setBuilder(builder);
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof Payload)) return false;
    Payload o = (Payload) other;
    return equals(listResponse, o.listResponse)
        && equals(detailsResponse, o.detailsResponse)
        && equals(reviewResponse, o.reviewResponse)
        && equals(buyResponse, o.buyResponse)
        && equals(searchResponse, o.searchResponse)
        && equals(tocResponse, o.tocResponse)
        && equals(browseResponse, o.browseResponse)
        && equals(purchaseStatusResponse, o.purchaseStatusResponse)
        && equals(updateInstrumentResponse, o.updateInstrumentResponse)
        && equals(logResponse, o.logResponse)
        && equals(checkInstrumentResponse, o.checkInstrumentResponse)
        && equals(plusOneResponse, o.plusOneResponse)
        && equals(flagContentResponse, o.flagContentResponse)
        && equals(ackNotificationResponse, o.ackNotificationResponse)
        && equals(initiateAssociationResponse, o.initiateAssociationResponse)
        && equals(verifyAssociationResponse, o.verifyAssociationResponse)
        && equals(libraryReplicationResponse, o.libraryReplicationResponse)
        && equals(revokeResponse, o.revokeResponse)
        && equals(bulkDetailsResponse, o.bulkDetailsResponse)
        && equals(resolveLinkResponse, o.resolveLinkResponse)
        && equals(deliveryResponse, o.deliveryResponse)
        && equals(acceptTosResponse, o.acceptTosResponse)
        && equals(rateSuggestedContentResponse, o.rateSuggestedContentResponse)
        && equals(checkPromoOfferResponse, o.checkPromoOfferResponse)
        && equals(uploadDeviceConfigResponse, o.uploadDeviceConfigResponse);
  }

  @Override
  public int hashCode() {
    int result = hashCode;
    if (result == 0) {
      result = listResponse != null ? listResponse.hashCode() : 0;
      result = result * 37 + (detailsResponse != null ? detailsResponse.hashCode() : 0);
      result = result * 37 + (reviewResponse != null ? reviewResponse.hashCode() : 0);
      result = result * 37 + (buyResponse != null ? buyResponse.hashCode() : 0);
      result = result * 37 + (searchResponse != null ? searchResponse.hashCode() : 0);
      result = result * 37 + (tocResponse != null ? tocResponse.hashCode() : 0);
      result = result * 37 + (browseResponse != null ? browseResponse.hashCode() : 0);
      result = result * 37 + (purchaseStatusResponse != null ? purchaseStatusResponse.hashCode() : 0);
      result = result * 37 + (updateInstrumentResponse != null ? updateInstrumentResponse.hashCode() : 0);
      result = result * 37 + (logResponse != null ? logResponse.hashCode() : 0);
      result = result * 37 + (checkInstrumentResponse != null ? checkInstrumentResponse.hashCode() : 0);
      result = result * 37 + (plusOneResponse != null ? plusOneResponse.hashCode() : 0);
      result = result * 37 + (flagContentResponse != null ? flagContentResponse.hashCode() : 0);
      result = result * 37 + (ackNotificationResponse != null ? ackNotificationResponse.hashCode() : 0);
      result = result * 37 + (initiateAssociationResponse != null ? initiateAssociationResponse.hashCode() : 0);
      result = result * 37 + (verifyAssociationResponse != null ? verifyAssociationResponse.hashCode() : 0);
      result = result * 37 + (libraryReplicationResponse != null ? libraryReplicationResponse.hashCode() : 0);
      result = result * 37 + (revokeResponse != null ? revokeResponse.hashCode() : 0);
      result = result * 37 + (bulkDetailsResponse != null ? bulkDetailsResponse.hashCode() : 0);
      result = result * 37 + (resolveLinkResponse != null ? resolveLinkResponse.hashCode() : 0);
      result = result * 37 + (deliveryResponse != null ? deliveryResponse.hashCode() : 0);
      result = result * 37 + (acceptTosResponse != null ? acceptTosResponse.hashCode() : 0);
      result = result * 37 + (rateSuggestedContentResponse != null ? rateSuggestedContentResponse.hashCode() : 0);
      result = result * 37 + (checkPromoOfferResponse != null ? checkPromoOfferResponse.hashCode() : 0);
      result = result * 37 + (uploadDeviceConfigResponse != null ? uploadDeviceConfigResponse.hashCode() : 0);
      hashCode = result;
    }
    return result;
  }

  public static final class Builder extends Message.Builder<Payload> {

    public ListResponse listResponse;
    public DetailsResponse detailsResponse;
    public ReviewResponse reviewResponse;
    public BuyResponse buyResponse;
    public SearchResponse searchResponse;
    public TocResponse tocResponse;
    public BrowseResponse browseResponse;
    public PurchaseStatusResponse purchaseStatusResponse;
    public UpdateInstrumentResponse updateInstrumentResponse;
    public LogResponse logResponse;
    public CheckInstrumentResponse checkInstrumentResponse;
    public PlusOneResponse plusOneResponse;
    public FlagContentResponse flagContentResponse;
    public AckNotificationResponse ackNotificationResponse;
    public InitiateAssociationResponse initiateAssociationResponse;
    public VerifyAssociationResponse verifyAssociationResponse;
    public LibraryReplicationResponse libraryReplicationResponse;
    public RevokeResponse revokeResponse;
    public BulkDetailsResponse bulkDetailsResponse;
    public ResolveLinkResponse resolveLinkResponse;
    public DeliveryResponse deliveryResponse;
    public AcceptTosResponse acceptTosResponse;
    public RateSuggestedContentResponse rateSuggestedContentResponse;
    public CheckPromoOfferResponse checkPromoOfferResponse;
    public UploadDeviceConfigResponse uploadDeviceConfigResponse;

    public Builder() {
    }

    public Builder(Payload message) {
      super(message);
      if (message == null) return;
      this.listResponse = message.listResponse;
      this.detailsResponse = message.detailsResponse;
      this.reviewResponse = message.reviewResponse;
      this.buyResponse = message.buyResponse;
      this.searchResponse = message.searchResponse;
      this.tocResponse = message.tocResponse;
      this.browseResponse = message.browseResponse;
      this.purchaseStatusResponse = message.purchaseStatusResponse;
      this.updateInstrumentResponse = message.updateInstrumentResponse;
      this.logResponse = message.logResponse;
      this.checkInstrumentResponse = message.checkInstrumentResponse;
      this.plusOneResponse = message.plusOneResponse;
      this.flagContentResponse = message.flagContentResponse;
      this.ackNotificationResponse = message.ackNotificationResponse;
      this.initiateAssociationResponse = message.initiateAssociationResponse;
      this.verifyAssociationResponse = message.verifyAssociationResponse;
      this.libraryReplicationResponse = message.libraryReplicationResponse;
      this.revokeResponse = message.revokeResponse;
      this.bulkDetailsResponse = message.bulkDetailsResponse;
      this.resolveLinkResponse = message.resolveLinkResponse;
      this.deliveryResponse = message.deliveryResponse;
      this.acceptTosResponse = message.acceptTosResponse;
      this.rateSuggestedContentResponse = message.rateSuggestedContentResponse;
      this.checkPromoOfferResponse = message.checkPromoOfferResponse;
      this.uploadDeviceConfigResponse = message.uploadDeviceConfigResponse;
    }

    public Builder listResponse(ListResponse listResponse) {
      this.listResponse = listResponse;
      return this;
    }

    public Builder detailsResponse(DetailsResponse detailsResponse) {
      this.detailsResponse = detailsResponse;
      return this;
    }

    public Builder reviewResponse(ReviewResponse reviewResponse) {
      this.reviewResponse = reviewResponse;
      return this;
    }

    public Builder buyResponse(BuyResponse buyResponse) {
      this.buyResponse = buyResponse;
      return this;
    }

    public Builder searchResponse(SearchResponse searchResponse) {
      this.searchResponse = searchResponse;
      return this;
    }

    public Builder tocResponse(TocResponse tocResponse) {
      this.tocResponse = tocResponse;
      return this;
    }

    public Builder browseResponse(BrowseResponse browseResponse) {
      this.browseResponse = browseResponse;
      return this;
    }

    public Builder purchaseStatusResponse(PurchaseStatusResponse purchaseStatusResponse) {
      this.purchaseStatusResponse = purchaseStatusResponse;
      return this;
    }

    public Builder updateInstrumentResponse(UpdateInstrumentResponse updateInstrumentResponse) {
      this.updateInstrumentResponse = updateInstrumentResponse;
      return this;
    }

    public Builder logResponse(LogResponse logResponse) {
      this.logResponse = logResponse;
      return this;
    }

    public Builder checkInstrumentResponse(CheckInstrumentResponse checkInstrumentResponse) {
      this.checkInstrumentResponse = checkInstrumentResponse;
      return this;
    }

    public Builder plusOneResponse(PlusOneResponse plusOneResponse) {
      this.plusOneResponse = plusOneResponse;
      return this;
    }

    public Builder flagContentResponse(FlagContentResponse flagContentResponse) {
      this.flagContentResponse = flagContentResponse;
      return this;
    }

    public Builder ackNotificationResponse(AckNotificationResponse ackNotificationResponse) {
      this.ackNotificationResponse = ackNotificationResponse;
      return this;
    }

    public Builder initiateAssociationResponse(InitiateAssociationResponse initiateAssociationResponse) {
      this.initiateAssociationResponse = initiateAssociationResponse;
      return this;
    }

    public Builder verifyAssociationResponse(VerifyAssociationResponse verifyAssociationResponse) {
      this.verifyAssociationResponse = verifyAssociationResponse;
      return this;
    }

    public Builder libraryReplicationResponse(LibraryReplicationResponse libraryReplicationResponse) {
      this.libraryReplicationResponse = libraryReplicationResponse;
      return this;
    }

    public Builder revokeResponse(RevokeResponse revokeResponse) {
      this.revokeResponse = revokeResponse;
      return this;
    }

    public Builder bulkDetailsResponse(BulkDetailsResponse bulkDetailsResponse) {
      this.bulkDetailsResponse = bulkDetailsResponse;
      return this;
    }

    public Builder resolveLinkResponse(ResolveLinkResponse resolveLinkResponse) {
      this.resolveLinkResponse = resolveLinkResponse;
      return this;
    }

    public Builder deliveryResponse(DeliveryResponse deliveryResponse) {
      this.deliveryResponse = deliveryResponse;
      return this;
    }

    public Builder acceptTosResponse(AcceptTosResponse acceptTosResponse) {
      this.acceptTosResponse = acceptTosResponse;
      return this;
    }

    public Builder rateSuggestedContentResponse(RateSuggestedContentResponse rateSuggestedContentResponse) {
      this.rateSuggestedContentResponse = rateSuggestedContentResponse;
      return this;
    }

    public Builder checkPromoOfferResponse(CheckPromoOfferResponse checkPromoOfferResponse) {
      this.checkPromoOfferResponse = checkPromoOfferResponse;
      return this;
    }

    public Builder uploadDeviceConfigResponse(UploadDeviceConfigResponse uploadDeviceConfigResponse) {
      this.uploadDeviceConfigResponse = uploadDeviceConfigResponse;
      return this;
    }

    @Override
    public Payload build() {
      return new Payload(this);
    }
  }
}