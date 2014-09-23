package com.carecaminnovations.mobile.model;

import java.util.List;

public class Ruleset {

	private int ruleSetId;
	private String comment;
	private List<Rules> rules;
	
	public int getRuleSetId() {
		return ruleSetId;
	}
	public void setRuleSetId(int ruleSetId) {
		this.ruleSetId = ruleSetId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public List<Rules> getRules() {
		return rules;
	}
	public void setRules(List<Rules> rules) {
		this.rules = rules;
	}

	
	
}
