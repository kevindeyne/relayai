START TRANSACTION;
ALTER TABLE user
ADD create_date DATETIME;

ALTER TABLE user
ADD create_user varchar(255) NOT NULL;

ALTER TABLE user
ADD update_date DATETIME;

ALTER TABLE user
ADD update_user varchar(255) NOT NULL;

/* ---- */

ALTER TABLE issue
ADD create_date DATETIME;

ALTER TABLE issue
ADD create_user varchar(255) NOT NULL;

ALTER TABLE issue
ADD update_date DATETIME;

ALTER TABLE issue
ADD update_user varchar(255) NOT NULL;
COMMIT;