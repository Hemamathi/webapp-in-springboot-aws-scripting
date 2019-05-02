package com.csye6225.noteapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.csye6225.noteapp.model.AttachmentFile;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentFile, String> {

	@Query(value = "SELECT * FROM attachment a WHERE a.note_id = ?1", nativeQuery = true)
	List<AttachmentFile> findAll(String note_id);

	@Query(value = "SELECT * FROM attachment a WHERE a.attachment_id = ?1", nativeQuery = true)
	AttachmentFile getAttachmentById(String attachmentId);
}