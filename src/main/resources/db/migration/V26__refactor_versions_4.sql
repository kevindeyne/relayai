START TRANSACTION;

ALTER TABLE versions ADD `version` varchar(100) NOT NULL DEFAULT '';
ALTER TABLE versions DROP major_version;
ALTER TABLE versions DROP minor_version;
ALTER TABLE versions DROP patch_version;

COMMIT;