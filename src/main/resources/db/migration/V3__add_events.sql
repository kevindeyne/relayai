START TRANSACTION;
CREATE TABLE `event` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL,
  `payload` TEXT NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `timestamp` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) engine=InnoDB;
COMMIT;