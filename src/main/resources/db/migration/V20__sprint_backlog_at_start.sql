START TRANSACTION;

ALTER TABLE `sprint`
ADD `backlog_at_start` INT NOT NULL DEFAULT 0;

COMMIT;