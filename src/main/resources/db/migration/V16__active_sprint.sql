START TRANSACTION;

ALTER TABLE user
ADD report BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE project
ADD active_sprint_id bigint(20) NOT NULL DEFAULT -1;

ALTER TABLE issue
ADD overload BOOLEAN NOT NULL DEFAULT FALSE;

COMMIT;