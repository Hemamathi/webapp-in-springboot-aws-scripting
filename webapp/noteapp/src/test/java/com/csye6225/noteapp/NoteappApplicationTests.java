package com.csye6225.noteapp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.csye6225.noteapp.controller.UserController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteappApplicationTests {
//test assert
	@Test
	public void contextLoads() {

	UserController pwcheck=new UserController();
	assertEquals("0",pwcheck.validateEmail("mhema2795@gmail.com"),true);
	assertEquals("1",pwcheck.validateEmail("mhema2795.com"),false);
	}

}

