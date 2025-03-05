CREATE TABLE `tbl_about_us`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `name`          VARCHAR(256) NULL,
    `phone`         VARCHAR(256) NULL,
    `email`         VARCHAR(256) NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;

INSERT INTO `tbl_about_us` (created_date, modified_date, name, phone, email)
VALUES (NOW(), NOW(), 'Cecilia Moberg', '0737 565 089', 'cecilia.moberg@ki.se');