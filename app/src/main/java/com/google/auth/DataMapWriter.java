package com.google.auth;

public class DataMapWriter extends DataMapReader {

	public DataMapWriter() {
		this(new DataMap());
		recycle();
	}

	public DataMapWriter(final DataMap dataMap) {
		super(dataMap);
	}

	public String putData(final DataField field, final String data) {
		return dataMap.put(field, data);
	}

	public void putData(final DataMapReader data) {
		for (final DataField field : data.getKeys()) {
			final String value = data.getData(field);
			if (value != null && !value.isEmpty()) {
				putData(field, value);
			}
		}

	}

	public void recycle() {
		dataMap = new DataMap();
	}

	public void setDataMap(final DataMap dataMap) {
		if (dataMap == null) {
			throw new RuntimeException("dataMap should not be null!");
		}
		this.dataMap = dataMap;
	}
}
