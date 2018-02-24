START TRANSACTION;
CREATE TABLE search (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  project_id bigint(20) NOT NULL,
  type varchar(255) NOT NULL,
  srcval TEXT NOT NULL,
  name varchar(255) NOT NULL,
  linked_id bigint(20) NOT NULL,
  PRIMARY KEY (id)
) engine=InnoDB;
ALTER TABLE search ADD FULLTEXT INDEX search_index (srcval ASC);
COMMIT;