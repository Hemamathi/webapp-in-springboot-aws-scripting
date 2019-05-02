package com.csye6225.noteapp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "note")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = { "createdOn", "lastUpdatedOn" }, allowGetters = true)
public class NoteEntity {
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private String note_id;

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	@Column(nullable = false, updatable = false, name = "created_on")
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdOn;

	@Column(nullable = false, name = "last_updated_on")
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date lastUpdatedOn;

	private Long user_id;

	public Long getUserId() {
		return user_id;
	}

	public void setUserId(Long user_id) {
		this.user_id = user_id;
	}

	public String getId() {
		return note_id;
	}

	public void setId(String id) {
		this.note_id = id;
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

	public Date getCreatedAt() {
		return createdOn;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdOn = createdAt;
	}

	public Date getUpdatedAt() {
		return lastUpdatedOn;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.lastUpdatedOn = updatedAt;
	}

}
