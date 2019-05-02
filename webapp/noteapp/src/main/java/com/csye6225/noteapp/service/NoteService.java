package com.csye6225.noteapp.service;

import java.util.List;
import java.util.UUID;

import com.csye6225.noteapp.model.NoteEntity;
import com.csye6225.noteapp.model.UploadFileResponse;

public interface NoteService {

	List<NoteEntity> findAll(Long userId);

	NoteEntity createNote(NoteEntity note);

	public NoteEntity getNoteById(String noteId);

	NoteEntity updateNote(NoteEntity noteDetails);

	public void deleteNote(NoteEntity noteDetails);

	public List<UploadFileResponse> getListAttachments(String noteId);

}
