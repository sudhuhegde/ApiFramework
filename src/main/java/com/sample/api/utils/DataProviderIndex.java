package com.sample.api.restservice.utils;

public class DataProviderIndex {
	public int index;

	public DataProviderIndex(int index){
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String toString() {
		return "DataSetId: " + index;
	}

}
