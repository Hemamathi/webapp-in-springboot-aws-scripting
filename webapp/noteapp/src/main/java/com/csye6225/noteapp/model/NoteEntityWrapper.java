package com.csye6225.noteapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteEntityWrapper {

	private String note_id;

	private String title;

	private String content;

	private Date createdOn;

	private Date lastUpdatedOn;

	private Long user_id;

	private List<UploadFileResponse> attachments;
	
	public NoteEntityWrapper() {
		this.attachments= new ArrayList<>();
	}
	
	public NoteEntityWrapper(NoteEntity note) {
		this.note_id = note.getId();
		this.title = note.getTitle();
		this.content = note.getContent();
		this.createdOn = note.getCreatedAt();
		this.lastUpdatedOn = note.getUpdatedAt();
		this.user_id = note.getUserId();
		this.attachments= new ArrayList<>();
	}
	

	public String getNote_id() {
		return note_id;
	}

	public void setNote_id(String note_id) {
		this.note_id = note_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public List<UploadFileResponse> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<UploadFileResponse> attachments) {
		this.attachments = attachments;
	}
	
	
}
