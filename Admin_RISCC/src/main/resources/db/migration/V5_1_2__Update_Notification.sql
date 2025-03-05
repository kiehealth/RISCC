ALTER TABLE `tbl_notification`
    MODIFY COLUMN `notification_type` ENUM ('BROADCAST', 'ROLE', 'GROUP', 'INDIVIDUAL');