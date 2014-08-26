package com.google.auth;

import java.util.Set;

public class DataMapReader {

	protected DataMap dataMap;

	public DataMapReader(final DataMap dataMap) {
		if (dataMap == null) {
			throw new RuntimeException("dataMap should not be null!");
		}
		this.dataMap = dataMap;
	}

	public String getData(final DataField field) {
		return dataMap.get(field);
	}

	public Set<DataField> getKeys() {
		return dataMap.keySet();
	}

	@Override
	public String toString() {
		return dataMap.getUrlDataString();
	}

}
