package com.carecaminnovations.mobile.model;

import java.util.List;

public class MessageSet {

	private int messageSetId;
	private String comment;
	private Buttons buttons;
	private Messages messages;
	public int getMessageSetId() {
		return messageSetId;
	}
	public void setMessageSetId(int messageSetId) {
		this.messageSetId = messageSetId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public Buttons getButtons() {
		return buttons;
	}
	public void setButtons(Buttons buttons) {
		this.buttons = buttons;
	}
	public Messages getMessages() {
		return messages;
	}
	public void setMessages(Messages messages) {
		this.messages = messages;
	}
	
	
}
