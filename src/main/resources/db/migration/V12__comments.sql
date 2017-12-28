START TRANSACTION;
CREATE TABLE `comments` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `issue_id` bigint(20) NOT NULL,
  `post_date` DATETIME NOT NULL,
  `message` TEXT NOT NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE comments
ADD FOREIGN KEY (user_id) REFERENCES `user`(id);

ALTER TABLE comments
ADD FOREIGN KEY (issue_id) REFERENCES `issue`(id);

COMMIT;