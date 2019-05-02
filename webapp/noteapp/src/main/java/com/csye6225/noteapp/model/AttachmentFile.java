package com.csye6225.noteapp.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "attachment")
public class AttachmentFile {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "attachment_id")
	private String attachmentId;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_type")
	private String fileType;

	@Column(name = "note_id")
	private String noteId;
	
	@Column(name = "file_path")
	private String filePath;

	public AttachmentFile() {

	}

	public AttachmentFile(String fileName, String fileType, String noteId, String filePath) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.noteId = noteId;
		this.filePath = filePath;
	}

	public String getId() {
		return attachmentId;
	}

	public void setId(String id) {
		this.attachmentId = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getNoteId() {
		return noteId;
	}

	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}