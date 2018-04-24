START TRANSACTION;

CREATE TABLE `activation_pending` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `activation_key` VARCHAR(35) NOT NULL,
  `valid_until` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) engine=InnoDB;

ALTER TABLE activation_pending
ADD FOREIGN KEY (user_id) REFERENCES `user`(id);

COMMIT;