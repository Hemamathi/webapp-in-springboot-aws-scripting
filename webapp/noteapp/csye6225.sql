CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(100) NOT NULL,
  `password` varchar(150) NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `userid_UNIQUE` (`email`)
) 

CREATE TABLE `note` (
  `note_id` varchar(100) NOT NULL,
  `content` varchar(999) NOT NULL,
  `title` varchar(200) DEFAULT NULL,
  `created_on` varchar(100) DEFAULT NULL,
  `last_updated_on` varchar(100) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`note_id`),
  KEY `fk_note_1_idx` (`user_id`),
  CONSTRAINT `fk_note_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
)

CREATE TABLE `attachment` (
  `attachment_id` varchar(100) NOT NULL,
  `file_name` varchar(100) DEFAULT NULL,
  `file_type` varchar(100) DEFAULT NULL,
  `note_id` varchar(45) DEFAULT NULL,
  `file_path` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`attachment_id`),
  KEY `fk_attachment_1_idx` (`note_id`),
  CONSTRAINT `fk_attachment_1` FOREIGN KEY (`note_id`) REFERENCES `note` (`note_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
)
