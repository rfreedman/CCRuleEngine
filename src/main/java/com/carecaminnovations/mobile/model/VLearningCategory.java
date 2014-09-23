package com.carecaminnovations.mobile.model;

import java.util.List;

public class VLearningCategory {
	private String categoryName;
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	private List<VLearningItem> vLearningItems;
	public List<VLearningItem> getvLearningItems() {
		return vLearningItems;
	}
	public void setvLearningItems(List<VLearningItem> vLearningItems) {
		this.vLearningItems = vLearningItems;
	}
}
