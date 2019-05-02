package com.csye6225.noteapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.csye6225.noteapp.model.NoteEntity;

public interface NoteRepository extends JpaRepository<NoteEntity, String> {

	@Query(value = "SELECT * FROM note n WHERE n.user_id = ?1", nativeQuery = true)
	List<NoteEntity> findAll(Long user_id);

	@Query(value = "SELECT * FROM note n WHERE n.note_id = ?1", nativeQuery = true)
	NoteEntity getNoteById(String note_id);

}
