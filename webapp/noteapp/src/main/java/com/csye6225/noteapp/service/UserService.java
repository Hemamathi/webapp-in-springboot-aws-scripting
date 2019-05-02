package com.csye6225.noteapp.service;

import com.csye6225.noteapp.model.UserEntity;

public interface UserService {
	
	public UserEntity findUser(String email);
	
	public UserEntity save(UserEntity users);
	
	public UserEntity currentUserId();

}
