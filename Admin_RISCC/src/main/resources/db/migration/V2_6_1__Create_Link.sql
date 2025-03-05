CREATE TABLE `tbl_link`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`     BIGINT       NULL,
    `created_date`   DATETIME     NOT NULL,
    `modified_by`    BIGINT       NULL,
    `modified_date`  DATETIME     NOT NULL,
    `title`          VARCHAR(256) NOT NULL,
    `description`    TEXT         NULL,
    `url`            VARCHAR(256) NULL,
    `email_address`  VARCHAR(256) NULL,
    `contact_number` VARCHAR(256) NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;