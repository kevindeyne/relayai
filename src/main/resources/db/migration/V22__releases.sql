START TRANSACTION;

CREATE TABLE `releases` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` varchar(10) NOT NULL,
  `release_date` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) engine=InnoDB;

CREATE TABLE `releases_changelog` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `release_id` bigint(20) NOT NULL,
  `issue_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) engine=InnoDB;

ALTER TABLE releases_changelog
ADD FOREIGN KEY (issue_id) REFERENCES issue(id);

ALTER TABLE releases_changelog
ADD FOREIGN KEY (release_id) REFERENCES releases(id);

COMMIT;