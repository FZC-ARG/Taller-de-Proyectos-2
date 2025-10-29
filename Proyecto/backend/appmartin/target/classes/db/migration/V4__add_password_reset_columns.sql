ALTER TABLE usuarios 
ADD COLUMN password_reset_token VARCHAR(100),
ADD COLUMN password_reset_expiry DATETIME,
ADD COLUMN failed_attempts INT DEFAULT 0;