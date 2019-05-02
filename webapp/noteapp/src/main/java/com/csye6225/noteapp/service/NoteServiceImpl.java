package com.csye6225.noteapp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csye6225.noteapp.Dao.NoteDaoInterface;
import com.csye6225.noteapp.model.AttachmentFile;
import com.csye6225.noteapp.model.NoteEntity;
import com.csye6225.noteapp.model.NoteEntityWrapper;
import com.csye6225.noteapp.model.UploadFileResponse;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	NoteDaoInterface noteDao;

	@Autowired
	private AttachmentStorageService attachmentService;

	@Override
	public List<NoteEntity> findAll(Long userId) {
		List<NoteEntity> noteList = noteDao.findAll(userId);
		return noteList;
	}

	@Override
	public List<UploadFileResponse> getListAttachments(String noteId) {

		List<AttachmentFile> attachmentFiles = attachmentService.getAllAttachments(noteId);
		List<UploadFileResponse> retunList = new ArrayList<>();
		for (AttachmentFile a : attachmentFiles) {
			UploadFileResponse ret = new UploadFileResponse(a.getId(), a.getFilePath());
			retunList.add(ret);
		}
		return retunList;
	}

	@Override
	public NoteEntity createNote(NoteEntity note) {
		// TODO Auto-generated method stub

		note.setCreatedAt(new Date());
		note.setUpdatedAt(new Date());
		UUID uuid = UUID.randomUUID();
		note.setId(uuid.toString());
		return noteDao.createNote(note);
	}

	@Override
	public NoteEntity updateNote(NoteEntity noteDetails) {
		// TODO Auto-generated method stub
		NoteEntity note = noteDao.updateNote(noteDetails);
		return getNoteById(noteDetails.getId());
	}

	@Override
	public void deleteNote(NoteEntity noteDetails) {
		for (UploadFileResponse attachment : getListAttachments(noteDetails.getId())) {
			attachmentService.deleteNoteByid(attachment.getId());
		}
		noteDao.deleteNote(noteDetails);
	}

	@Override
	public NoteEntity getNoteById(String noteId) {
		NoteEntity note = noteDao.getNoteById(noteId);
		//note.setAttachments(getListAttachments(noteId));
		return note;
	}

}
