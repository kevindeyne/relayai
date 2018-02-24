START TRANSACTION;
CREATE TABLE timesheet (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  start_date DATETIME NOT NULL,
  end_date DATETIME NULL,
  avg_workday INT NOT NULL DEFAULT 8,
  issue_id bigint(20) NOT NULL,
  user_id bigint(20) NOT NULL,
  PRIMARY KEY (id)
) engine=InnoDB;
ALTER TABLE timesheet
ADD FOREIGN KEY (issue_id) REFERENCES issue(id);

ALTER TABLE timesheet
ADD FOREIGN KEY (user_id) REFERENCES user(id);

CREATE INDEX timesheet_date ON timesheet (start_date);
COMMIT;