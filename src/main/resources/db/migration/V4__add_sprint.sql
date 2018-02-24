START TRANSACTION;
CREATE TABLE project (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  title varchar(255) NOT NULL,
  key varchar(255) NOT NULL,
  PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE project_users (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  project_id bigint(20) NOT NULL,
  user_id bigint(20) NOT NULL,
  PRIMARY KEY (id)
) engine=InnoDB;

CREATE TABLE sprint (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  start_date DATETIME,
  end_date DATETIME,
  project_id bigint(20) NOT NULL,
  PRIMARY KEY (id)
) engine=InnoDB;

/* ---- */

ALTER TABLE issue
ADD sprint_id bigint(20) NOT NULL;

ALTER TABLE issue
ADD project_id bigint(20) NOT NULL;

/* ---- */

ALTER TABLE issue
ADD FOREIGN KEY (sprint_id) REFERENCES sprint(id);

ALTER TABLE issue
ADD FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE project_users
ADD FOREIGN KEY (project_id) REFERENCES project(id);

ALTER TABLE project_users
ADD FOREIGN KEY (user_id) REFERENCES user(id);

ALTER TABLE sprint
ADD FOREIGN KEY (project_id) REFERENCES project(id);
COMMIT;