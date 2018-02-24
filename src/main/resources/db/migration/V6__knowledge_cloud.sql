START TRANSACTION;
CREATE TABLE `tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) engine=InnoDB;

ALTER TABLE tag ADD INDEX (tag(25));

CREATE TABLE `tagcloud` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `issue_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) engine=InnoDB;

ALTER TABLE tagcloud
ADD FOREIGN KEY (issue_id) REFERENCES issue(id);

ALTER TABLE tagcloud
ADD FOREIGN KEY (tag_id) REFERENCES tag(id);

CREATE TABLE `knowledge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) engine=InnoDB;

ALTER TABLE knowledge
ADD FOREIGN KEY (user_id) REFERENCES user(id);

ALTER TABLE knowledge
ADD FOREIGN KEY (tag_id) REFERENCES tag(id);
COMMIT;