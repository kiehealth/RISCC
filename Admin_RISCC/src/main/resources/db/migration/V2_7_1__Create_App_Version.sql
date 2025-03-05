CREATE TABLE `tbl_app_version`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `family`        VARCHAR(256) NOT NULL,
    `version`       VARCHAR(256) NOT NULL,
    `force_update`  BOOLEAN      NOT NULL,
    `url`           VARCHAR(256) NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;

INSERT INTO `tbl_app_version` (created_date, modified_date, family, version, force_update)
VALUES (NOW(), NOW(), 'IOS', '1.0', false),
       (NOW(), NOW(), 'ANDROID', '1.0', false);