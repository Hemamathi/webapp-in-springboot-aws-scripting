package com.csye6225.noteapp;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.csye6225.noteapp.util.FileStorageProperties;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties({ FileStorageProperties.class })
public class NoteappApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(NoteappApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println(args.toString());
	}

}
