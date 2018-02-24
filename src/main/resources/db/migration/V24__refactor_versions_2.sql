START TRANSACTION;

CREATE TABLE `versions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,  
  `project_id` bigint(20) NOT NULL DEFAULT '-1',
  `branch_id` bigint(20) NOT NULL DEFAULT '-1',
  `major_version` int(11) NOT NULL DEFAULT '0',
  `minor_version` int(11) NOT NULL DEFAULT '0',
  `patch_version` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `branch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,  
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE `version_issue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,  
  `versions_id` bigint(20) NOT NULL,
  `issue_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

ALTER TABLE versions
ADD FOREIGN KEY (project_id) REFERENCES `project`(id);

ALTER TABLE versions
ADD FOREIGN KEY (branch_id) REFERENCES `branch`(id);

ALTER TABLE version_issue
ADD FOREIGN KEY (versions_id) REFERENCES `versions`(id);

ALTER TABLE version_issue
ADD FOREIGN KEY (issue_id) REFERENCES `issue`(id);

COMMIT;