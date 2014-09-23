package com.carecaminnovations.mobile.model;

import java.util.List;

public class TipSet {

	private int tipSetId;
	private String comment;
	private List<Tip> tip;
	public int getTipSetId() {
		return tipSetId;
	}
	public void setTipSetId(int tipSetId) {
		this.tipSetId = tipSetId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<Tip> getTip() {
		return tip;
	}
	public void setTip(List<Tip> tip) {
		this.tip = tip;
	}

	
	
}
