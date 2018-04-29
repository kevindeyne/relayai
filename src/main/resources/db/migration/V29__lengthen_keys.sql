START TRANSACTION;

ALTER TABLE invitation MODIFY invitation_key VARCHAR(36);
ALTER TABLE activation_pending MODIFY activation_key VARCHAR(36);

COMMIT;