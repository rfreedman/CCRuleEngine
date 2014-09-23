package com.carecaminnovations.mobile.model;

public class MeasureDefinition {

private String displayName;
private Range range;


public String getDisplayName() {
	return displayName;
}


public void setDisplayName(String displayName) {
	this.displayName = displayName;
}


public Range getRange() {
	return range;
}


public void setRange(Range range) {
	this.range = range;
}


private class Range{
	
	private String name;
	private String color;
	private int startValue;
	private int endValue;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getStartValue() {
		return startValue;
	}
	public void setStartValue(int startValue) {
		this.startValue = startValue;
	}
	public int getEndValue() {
		return endValue;
	}
	public void setEndValue(int endValue) {
		this.endValue = endValue;
	}
	
	
}




}
