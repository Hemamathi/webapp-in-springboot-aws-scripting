package com.csye6225.noteapp.Dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.csye6225.noteapp.model.NoteEntity;
import com.csye6225.noteapp.repository.NoteRepository;

@Service
public class NoteDaoImpl implements NoteDaoInterface {

	@Autowired
	NoteRepository noteRepository;

	@Override
	public List<NoteEntity> findAll(Long userId) {
		// TODO Auto-generated method stub
		return noteRepository.findAll(userId);
	}

	@Override
	public NoteEntity createNote(NoteEntity note) {
		// TODO Auto-generated method stub
		return noteRepository.save(note);
	}

	@Override
	public NoteEntity updateNote(NoteEntity noteDetails) {
		// TODO Auto-generated method stub
		return noteRepository.save(noteDetails);
	}

	@Override
	public void deleteNote(NoteEntity note) {
		// TODO Auto-generated method stub
		noteRepository.delete(note);
	}

	@Override
	public NoteEntity getNoteById(String noteId) {
		return noteRepository.findById(noteId).orElse(null);
	}

}
