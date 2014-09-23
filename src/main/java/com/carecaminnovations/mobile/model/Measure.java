package com.carecaminnovations.mobile.model;

import java.util.List;

public class Measure {
	private String name;
	private String measureId;
	private List<Data> data;
	private List<Range> ranges;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMeasureId() {
		return measureId;
	}
	public void setMeasureId(String measureId) {
		this.measureId = measureId;
	}

	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	public List<Range> getRanges() {
		return ranges;
	}
	public void setRanges(List<Range> ranges) {
		this.ranges = ranges;
	}

}
