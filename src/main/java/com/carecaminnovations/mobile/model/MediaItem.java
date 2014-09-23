package com.carecaminnovations.mobile.model;

import java.util.Date;
import java.util.List;

public class MediaItem {
	public String getMediaCategory() {
		return mediaCategory;
	}
	public void setMediaCategory(String mediaCategory) {
		this.mediaCategory = mediaCategory;
	}
	public MediaType getMediaType() {
		return mediaType;
	}
	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}
	public int getMediaId() {
		return mediaId;
	}
	public void setMediaId(int mediaId) {
		this.mediaId = mediaId;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public int getPersonProtocolId() {
		return personProtocolId;
	}
	public void setPersonProtocolId(int personProtocolId) {
		this.personProtocolId = personProtocolId;
	}
	public int getStepId() {
		return stepId;
	}
	public void setStepId(int stepId) {
		this.stepId = stepId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getMediaTypeId() {
		return mediaTypeId;
	}
	public void setMediaTypeId(int mediaTypeId) {
		this.mediaTypeId = mediaTypeId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreatedDt() {
		return createdDt;
	}
	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}
	private String mediaCategory;
	private MediaType mediaType;
	private int mediaId;
	private int personId;
	private int personProtocolId;
	private int stepId;
	private int categoryId;
	private int mediaTypeId;
	private String url;
	private String filename;
	private String mimeType;
	private String title;
	private String description;
	private Date createdDt;
	
	
	
	
	
}
