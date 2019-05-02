package com.csye6225.noteapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.csye6225.noteapp.model.UserEntity;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	
	@Query(value= "SELECT * FROM user u WHERE LOWER(u.email) = LOWER(?1)", nativeQuery = true)
     UserEntity findUser(String email);

}