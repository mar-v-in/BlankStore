package com.android.vending;

import android.util.Log;

public class Reference<T> {
	private T ref;

	public Reference() {
		Log.d("Reference", hashCode() + ": reference initialized empty");
	}

	public Reference(T init) {
		ref = init;
	}

	public T get() {
		return ref;
	}

	public boolean isNull() {
		return ref == null;
	}

	public void set(T val) {
		ref = val;
	}
}
