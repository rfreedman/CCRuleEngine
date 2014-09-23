package com.carecaminnovations.mobile.model;

public class Transaction {
private String activityName;
private String stepNumber;
private int stepId;
public int getStepId() {
	return stepId;
}
public void setStepId(int stepId) {
	this.stepId = stepId;
}
private String stepText;
private String completedDate;


public String getActivityName() {
	return activityName;
}
public void setActivityName(String activityName) {
	this.activityName = activityName;
}
public String getStepNumber() {
	return stepNumber;
}
public void setStepNumber(String stepNumber) {
	this.stepNumber = stepNumber;
}
public String getStepText() {
	return stepText;
}
public void setStepText(String stepText) {
	this.stepText = stepText;
}
public String getCompletedDate() {
	return completedDate;
}
public void setCompletedDate(String completedDate) {
	this.completedDate = completedDate;
}

}
