package com.csye6225.noteapp.Dao;

import com.csye6225.noteapp.model.UserEntity;

public interface UserDaoInterface {

	public UserEntity findUser(String email);

	public UserEntity save(UserEntity users);
}	