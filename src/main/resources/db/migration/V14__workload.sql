START TRANSACTION;

ALTER TABLE issue
ADD workload INT NOT NULL DEFAULT -1;

COMMIT;