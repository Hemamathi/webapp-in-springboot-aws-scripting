package com.csye6225.noteapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.csye6225.noteapp.Dao.AttachmentDaoImpl;
import com.csye6225.noteapp.model.AttachmentFile;
import com.csye6225.noteapp.util.AmazonS3Example;
import com.csye6225.noteapp.util.FileStorageException;
import com.csye6225.noteapp.util.FileStorageProperties;
import com.csye6225.noteapp.util.MyFileNotFoundException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class AttachmentStorageService {

	private final Path fileStorageLocation;

	@Autowired
	AttachmentDaoImpl attachmentDao;

	@Autowired
	AmazonS3Example cloudStorage;

	@Value("${env}")
	private String env;

	@Value("${bucket.name}")
	private String bucketName;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	public AttachmentStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public AttachmentFile storeFile(MultipartFile file, AttachmentFile attachment) {
		// Normalize file name
		AttachmentFile uploadedFile = null;
		try {
			// Check if the file's name contains invalid characters
			if (attachment.getFileName().contains("..")) {
				throw new FileStorageException(
						"Sorry! Filename contains invalid path sequence " + attachment.getFileName());
			}

			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.fileStorageLocation.resolve(attachment.getFileName());
			if (env.equalsIgnoreCase("local")) {
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			} else {
				attachment = cloudStorage.save(file, attachment);
			}
			uploadedFile = attachmentDao.createAttachment(attachment);
			// Files.copy(file.getInputStream(), targetLocation,
			// StandardCopyOption.REPLACE_EXISTING);

			return uploadedFile;
		} catch (IOException ex) {
			log.error(ex.getMessage());
			throw new FileStorageException("Could not store file " + attachment.getFileName() + ". Please try again!",
					ex);
		}
	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			log.error(ex.getMessage());
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}

	public List<AttachmentFile> getAllAttachments(String noteId) {
		// TODO Auto-generated method stub
		return attachmentDao.findAll(noteId);
	}

	public AttachmentFile getAttachmentByid(String attachmentId) {
		return attachmentDao.getAttachmentById(attachmentId);
	}

	public void deleteNoteByid(String attachmentid) {
		// TODO Auto-generated method stub
		if (env.equalsIgnoreCase("local")) {
			attachmentDao.deleteNoteById(attachmentid);
		} else {
			cloudStorage.delete(getAttachmentByid(attachmentid).getFileName());
			attachmentDao.deleteNoteById(attachmentid);
		}
	}
}