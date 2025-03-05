CREATE TABLE `tbl_notification`
(
    `id`                BIGINT                                   NOT NULL AUTO_INCREMENT,
    `created_by`        BIGINT                                   NULL,
    `created_date`      DATETIME                                 NOT NULL,
    `modified_by`       BIGINT                                   NULL,
    `modified_date`     DATETIME                                 NOT NULL,
    `title`             VARCHAR(256)                             NOT NULL,
    `description`       TEXT                                     NULL,
    `notification_type` ENUM ('BROADCAST', 'ROLE', 'INDIVIDUAL') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;