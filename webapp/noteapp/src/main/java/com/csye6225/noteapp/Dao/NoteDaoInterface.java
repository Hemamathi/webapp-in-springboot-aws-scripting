package com.csye6225.noteapp.Dao;

import java.util.List;

import com.csye6225.noteapp.model.NoteEntity;

public interface NoteDaoInterface {

	List<NoteEntity> findAll(Long userId);

	NoteEntity createNote(NoteEntity note);

	NoteEntity updateNote(NoteEntity noteDetails);

	public void deleteNote(NoteEntity note);
	
	public NoteEntity getNoteById(String noteId);

}
