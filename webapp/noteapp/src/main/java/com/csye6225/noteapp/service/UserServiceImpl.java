package com.csye6225.noteapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.csye6225.noteapp.Dao.UserDaoInterface;
import com.csye6225.noteapp.model.UserEntity;
import com.csye6225.noteapp.util.PasswordEncoderConfig;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDaoInterface userDao;

	@Autowired
	PasswordEncoderConfig passwordConfig;

	@Override
	public UserEntity findUser(String email) {
		return userDao.findUser(email);
	}

	@Override
	public UserEntity save(UserEntity users) {

		users.setPassword(BCrypt.hashpw(users.getPassword(), BCrypt.gensalt(10)));

		return userDao.save(users);
	}

	@Override
	public UserEntity currentUserId() {
		String currentUserName = "";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			currentUserName = authentication.getName();

		}

		return findUser(currentUserName);

	}

}
