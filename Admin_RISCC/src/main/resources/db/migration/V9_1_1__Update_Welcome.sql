ALTER TABLE `tbl_welcome` CHANGE `welcome_text` `welcome_text_swedish` TEXT NULL,
    CHANGE `thank_you_text` `thank_you_text_swedish` TEXT NULL;

ALTER TABLE `tbl_welcome`
    ADD COLUMN `welcome_text` TEXT NULL,
    ADD COLUMN `welcome_text_spanish`  TEXT NULL,
    ADD COLUMN `thank_you_text` TEXT NULL,
    ADD COLUMN `thank_you_text_spanish` TEXT NULL;