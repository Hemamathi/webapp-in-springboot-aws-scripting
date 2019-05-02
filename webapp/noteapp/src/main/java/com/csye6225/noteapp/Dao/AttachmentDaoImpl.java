package com.csye6225.noteapp.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csye6225.noteapp.model.AttachmentFile;
import com.csye6225.noteapp.repository.AttachmentRepository;

@Service
public class AttachmentDaoImpl {

	@Autowired
	AttachmentRepository attachmentRepository;

	public AttachmentFile createAttachment(AttachmentFile attachment) {
		// TODO Auto-generated method stub
		return attachmentRepository.save(attachment);
	}

	public List<AttachmentFile> findAll(String noteId) {
		// TODO Auto-generated method stub
		return attachmentRepository.findAll(noteId);
	}

	public AttachmentFile getAttachmentById(String attachmentId) {
		// TODO Auto-generated method stub
		return attachmentRepository.getAttachmentById(attachmentId);
	}

	public void deleteNoteById(String attachmentid) {
		// TODO Auto-generated method stub
		attachmentRepository.deleteById(attachmentid);
	}
}
