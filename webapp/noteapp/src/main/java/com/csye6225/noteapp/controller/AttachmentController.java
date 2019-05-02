package com.csye6225.noteapp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.csye6225.noteapp.model.AttachmentFile;
import com.csye6225.noteapp.model.NoteEntity;
import com.csye6225.noteapp.model.UploadFileResponse;
import com.csye6225.noteapp.service.AttachmentStorageService;
import com.csye6225.noteapp.service.NoteService;
import com.csye6225.noteapp.service.UserService;
import com.csye6225.noteapp.util.MyFileNotFoundException;
import com.csye6225.noteapp.util.MyUnauthorizedException;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

@RestController
public class AttachmentController {

	@Autowired
	private AttachmentStorageService attachmentService;

	@Autowired
	UserService userService;

	@Autowired
	NoteService noteService;

	@Value("${env}")
	private String env;

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private static final StatsDClient statsd = new NonBlockingStatsDClient("csye6225.webapp", "localhost", 8125);

	@GetMapping("/note/{id}/attachments")
	public List<UploadFileResponse> getAllAttachmentByNoteId(@PathVariable(value = "id") String noteId) {
		log.info("Inside get /note/id/attachments mapping");
		statsd.incrementCounter("get /note/id/attachments");
		// HashMap<String, Object> entities = new HashMap<String, Object>();
		NoteEntity note = noteService.getNoteById(noteId);
		if (null == note) {
			// entities.put("message", "Note does not exists");
			throw new MyFileNotFoundException("Note does not exists " + HttpStatus.NOT_FOUND);
		} else if (Long.compare(userService.currentUserId().getId(), note.getUserId()) == 0) {
			List<AttachmentFile> attachmentFiles = attachmentService.getAllAttachments(noteId);
			List<UploadFileResponse> retunList = new ArrayList<>();
			for (AttachmentFile a : attachmentFiles) {
				UploadFileResponse ret = new UploadFileResponse(a.getId(), a.getFilePath());
				retunList.add(ret);
			}
			// entities.put("attachments", retunList);
			return retunList;

		} else {
			// entities.put("message", "Not authorized to read this note's attachment");
			throw new MyUnauthorizedException(
					"Note Authorized to view add attachments to this note " + HttpStatus.UNAUTHORIZED);
		}

	}

	@PostMapping("/note/{id}/attachments")
	public UploadFileResponse uploadFile(@PathVariable(value = "id") String noteId,
			@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String authori) {
		log.info("Inside post /note/id/attachments mapping");

		statsd.incrementCounter("post /note/id/attachments");
		NoteEntity note = noteService.getNoteById(noteId);
		if (null == note) {
			// entities.put("message", "Note does not exists");
			throw new MyFileNotFoundException("Note does not exists" + HttpStatus.NOT_FOUND);
		} else if (Long.compare(userService.currentUserId().getId(), note.getUserId()) != 0) {
			log.warn("Header " + authori + "uSER iD" + userService.currentUserId().getId() + "nOTE ID"
					+ note.getUserId());
			throw new MyUnauthorizedException(
					"Not Authorized to view add attachments to this note " + HttpStatus.UNAUTHORIZED);
		} else {
			String fileName = new Date().getTime() + StringUtils.cleanPath(file.getOriginalFilename());

			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
					.path(fileName).toUriString();

			AttachmentFile attachment = new AttachmentFile(fileName, file.getContentType(), noteId, fileDownloadUri);
			AttachmentFile uploadedFile = attachmentService.storeFile(file, attachment);
			return new UploadFileResponse(uploadedFile.getId(), uploadedFile.getFilePath());
		}
	}

	@PutMapping("/note/{id}/attachments/{attachmentsid}")
	public ResponseEntity<Object> updateNote(@PathVariable(value = "id") String noteId,
			@PathVariable(value = "attachmentsid") String attachmentid, @RequestParam("file") MultipartFile file) {
		log.info("Inside put /note/id/attachments/id mapping");
		statsd.incrementCounter("put /note/id/attachments/id");
		// Check if user authenticated and authorized
		NoteEntity note = noteService.getNoteById(noteId);
		HashMap<String, Object> entities = new HashMap<String, Object>();
		if (null == note) {
			entities.put("message", "Note does not exists");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		} else if (Long.compare(userService.currentUserId().getId(), note.getUserId()) == 0) {

			AttachmentFile attachment = attachmentService.getAttachmentByid(attachmentid);

			if (attachment == null) {
				throw new MyFileNotFoundException("Attachment does not exists " + HttpStatus.NOT_FOUND);
			} else if (!noteId.equalsIgnoreCase(attachment.getNoteId())) {
				throw new MyFileNotFoundException("Attachment does not exists in this note" + HttpStatus.NOT_FOUND);
			} else {
				AttachmentFile updatedAttachment = new AttachmentFile();
				String fileName = StringUtils.cleanPath(file.getOriginalFilename());

				String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/downloadFile/")
						.path(fileName).toUriString();
				// AttachmentFile updatedAttachment = new AttachmentFile(fileName,
				// file.getContentType(), noteId, fileDownloadUri);
				updatedAttachment.setId(attachmentid);
				updatedAttachment.setFileName(fileName);
				updatedAttachment.setFileType(file.getContentType());
				updatedAttachment.setFilePath(fileDownloadUri);
				updatedAttachment.setNoteId(noteId);
				deleteNote(noteId, attachmentid);
				attachmentService.storeFile(file, updatedAttachment);
				// entities.put("message", "Note does not exists");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

		} else {
			entities.put("message", "Not authorized to update this note");
			throw new MyUnauthorizedException(
					"Not Authorized to view add attachments to this note " + HttpStatus.UNAUTHORIZED);
		}

	}

	@DeleteMapping("/note/{id}/attachments/{attachmentsid}")
	public ResponseEntity<?> deleteNote(@PathVariable(value = "id") String noteId,
			@PathVariable(value = "attachmentsid") String attachmentid) {
		log.info("Inside delete /note/id/attachments/id mapping");
		statsd.incrementCounter("delete /note/id/attachments/id");
		NoteEntity note = noteService.getNoteById(noteId);

		HashMap<String, Object> entities = new HashMap<String, Object>();
		if (null == note) {
			entities.put("message", "Note does not exists");
			return new ResponseEntity<>(entities, HttpStatus.BAD_REQUEST);
		} else if (Long.compare(userService.currentUserId().getId(), note.getUserId()) == 0) {

			AttachmentFile attachment = attachmentService.getAttachmentByid(attachmentid);

			if (attachment == null) {
				throw new MyFileNotFoundException("Attachment does not exists " + HttpStatus.NOT_FOUND);
			} else if (!noteId.equalsIgnoreCase(attachment.getNoteId())) {
				throw new MyFileNotFoundException("Attachment does not exists in this note" + HttpStatus.NOT_FOUND);
			} else {
				attachmentService.deleteNoteByid(attachmentid);
				// entities.put("message", "Note does not exists");
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			}

		} else {
			entities.put("message", "Not authorized to Delete this note");
			return new ResponseEntity<>(entities, HttpStatus.UNAUTHORIZED);
		}

	}

	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		Resource resource = attachmentService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			log.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}