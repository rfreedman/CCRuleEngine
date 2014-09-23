package com.carecaminnovations.mobile.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Activity {
	private static final int STATUS_COMPLETED = 1;
	
	private int activityId;
	private int ownerId;
	private int personProtocolId;
	private int entityId;
	private int activityType;
	private int status;
	private Date startDt;
	private Date endDt;
	private String startBefore;
	private String startAfter;
	private String name;
	private String description;
	private String activityDefinition;
	private Date createdDt;
	private int lastUpdatedUserId;
	private Date lastUpdatedDt;
	private List<HelpItem> helpItems;
    private List<Step> steps;
    private List<Notification> notification;
    private int section;
	private int type;
	private String iconUrl;
	private int parentActivityId;
	private Date lateDt;
	private Date missedDt;
	private int notificationIndicator;
	private List<VLearningItem> vLearningItems;
	private String[] vLearningIds;
	public String[] getvLearningIds() {
		return vLearningIds;
	}
	public void setvLearningIds(String[] vLearningIds) {
		this.vLearningIds = vLearningIds;
	}
	public List<VLearningItem> getVLearningItems() {
		return vLearningItems;
	}
	public void setVLearningItems(List<VLearningItem> vLearningItems) {
		vLearningItems = vLearningItems;
	}
	public int getNotificationIndicator() {
		return notificationIndicator;
	}
	public void setNotificationIndicator(int notificationIndicator) {
		this.notificationIndicator = notificationIndicator;
	}
	public Date getMissedDt() {
		return missedDt;
	}
	public void setMissedDt(Date missedDt) {
		this.missedDt = missedDt;
	}
	public Date getLateDt() {
		return lateDt;
	}
	public void setLateDt(Date lateDt) {
		this.lateDt = lateDt;
	}
	public int getParentActivityId() {
		return parentActivityId;
	}
	public void setParentActivityId(int parentActivityId) {
		this.parentActivityId = parentActivityId;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<HelpItem> getHelpItems() {
		return helpItems;
	}
	public void setHelpItems(List<HelpItem> helpItems) {
		this.helpItems = helpItems;
	}
	public List<Step> getSteps() {
		return steps;
	}
	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}
	public List<Notification> getNotification() {
		return notification;
	}
	public void setNotifications(List<Notification> notification) {
		this.notification = notification;
	}
	public int getActivityId() {
		return activityId;
	}
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public int getPersonProtocolId() {
		return personProtocolId;
	}
	public void setPersonProtocolId(int personProtocolId) {
		this.personProtocolId = personProtocolId;
	}
	public int getEntityId() {
		return entityId;
	}
	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}
	public int getActivityType() {
		return activityType;
	}
	public void setActivityType(int activityType) {
		this.activityType = activityType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getStartDt() {
		return startDt;
	}
	public void setStartDt(Date startDt) {
		this.startDt = startDt;
	}
	public Date getEndDt() {
		return endDt;
	}
	public void setEndDt(Date endDt) {
		this.endDt = endDt;
	}
	public String getStartBefore() {
		return startBefore;
	}
	public void setStartBefore() {
		try{
		//todo clean up look at calender object
			SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss");
		String sstartDate = sdf.format(getStartDt());
		Date startDate = sdf.parse(sstartDate);
		String strDateFormat = "hh:mm a";
	    SimpleDateFormat sdfTime = new SimpleDateFormat(strDateFormat);
	    if(startDt != null){
	    	this.startBefore = sdfTime.format(startDate);
	    }
		}
		catch(Exception ex){
			
		}
	}
	public String getStartAfter() {
		return startAfter;
	}
	public void setStartAfter(String startAfter) {
		this.startAfter = startAfter;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getActivityDefinition() {
		return activityDefinition;
	}
	public void setActivityDefinition(String activityDefinition) {
		this.activityDefinition = activityDefinition;
	}
	public Date getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}
	public int getLastUpdatedUserId() {
		return lastUpdatedUserId;
	}
	public void setLastUpdatedUserId(int lastUpdatedUserId) {
		this.lastUpdatedUserId = lastUpdatedUserId;
	}
	public Date getLastUpdatedDt() {
		return lastUpdatedDt;
	}
	public void setLastUpdatedDt(Date lastUpdatedDt) {
		this.lastUpdatedDt = lastUpdatedDt;
	}
	public int getSection() {
		return section;
	}
	public void setSection(int section) {
		this.section = section;
	}
	public String getSectionDisplayName() {
		switch (section) {
		case 0:
			return "Notification";
		case 1:
			return "Morning";
		case 2:
			return "Afternoon";
		case 3:
			return "Evening";
		default:
			return "Unknown";
		}
	}
	public void completeActivity() {
		status = STATUS_COMPLETED;
	}
	public boolean isCompleted() {
		return status == STATUS_COMPLETED;
	}
}
