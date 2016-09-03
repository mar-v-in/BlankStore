package com.google.play;

import com.google.play.proto.Notification;
import com.google.play.proto.PreFetch;
import com.google.play.proto.ResponseWrapper;
import com.google.play.proto.ServerCommands;

import java.util.Collections;
import java.util.List;

public class DfeResponse<ResponseType> {
	private ResponseType response;
	private ResponseWrapper wrapper;
	private int statusCode;
	private String statusString;
	private Throwable throwable;

	public DfeResponse(Throwable throwable) {
		this.throwable = throwable;
	}

	public DfeResponse(int statusCode, String statusString, Throwable throwable) {
		this.statusCode = statusCode;
		this.statusString = statusString;
		this.throwable = throwable;
	}

	public DfeResponse(ResponseType response, ResponseWrapper wrapper, int statusCode, String statusString) {
		this.response = response;
		this.wrapper = wrapper;
		this.statusCode = statusCode;
		this.statusString = statusString;
	}

	public DfeResponse(int statusCode, String statusString) {
		this.statusCode = statusCode;
		this.statusString = statusString;
	}

	public DfeResponse(ResponseWrapper wrapper, ResponseType response) {
		this.wrapper = wrapper;
		this.response = response;
	}

	public DfeResponse(ResponseWrapper wrapper, int statusCode, String statusString) {
		this.wrapper = wrapper;
		this.statusCode = statusCode;
		this.statusString = statusString;
	}

	public boolean hasError() {
		return throwable != null || statusCode > 200;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusString() {
		return statusString;
	}

	public ResponseType getResponse() {
		return response;
	}

	void setResponse(ResponseType response) {
		this.response = response;
	}

	public ServerCommands getCommands() {
		return wrapper != null ? wrapper.commands : null;
	}

	public List<Notification> getNotifications() {
		return wrapper != null ? wrapper.notification : Collections.<Notification>emptyList();
	}

	public List<PreFetch> getPreFetches() {
		return wrapper != null ? wrapper.preFetch : Collections.<PreFetch>emptyList();
	}

	ResponseWrapper getWrapper() {
		return wrapper;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	boolean hasWrapperPayload() {
		return wrapper != null && wrapper.payload != null;
	}

    @Override
    public String toString() {
        return "DfeResponse{" +
                "response=" + response +
                ", wrapper=" + wrapper +
                ", statusCode=" + statusCode +
                ", statusString='" + statusString + '\'' +
                ", throwable=" + throwable +
                '}';
    }
}
