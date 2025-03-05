-- tbl_app_analytics_key
CREATE TABLE `tbl_app_analytics_key`
(
    `id`                          BIGINT                              NOT NULL AUTO_INCREMENT,
    `created_by`                  BIGINT                              NULL,
    `created_date`                DATETIME                            NOT NULL,
    `modified_by`                 BIGINT                              NULL,
    `modified_date`               DATETIME                            NOT NULL,
    `key_title`                   VARCHAR(256)                        NOT NULL,
    `description`                 TEXT                                NULL,
    `app_analytics_key_data_type` ENUM ('DATETIME', 'NUMBER', 'TEXT') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB;

-- tbl_app_analytics
CREATE TABLE `tbl_app_analytics`
(
    `id`                   BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`           BIGINT       NULL,
    `created_date`         DATETIME     NOT NULL,
    `modified_by`          BIGINT       NULL,
    `modified_date`        DATETIME     NOT NULL,
    `description`          TEXT         NULL,
    `key_value_int`        INT          NULL,
    `key_value_date_time`  DATETIME     NULL,
    `key_value_text`       VARCHAR(256) NULL,
    `app_analytics_key_id` BIGINT       NOT NULL,
    `user_id`              BIGINT       NULL,
    PRIMARY KEY (`id`),
    KEY `FK_app_analytics_app_analytics_key_id` (`app_analytics_key_id`),
    KEY `FK_app_analytics_user_id` (`user_id`),
    CONSTRAINT `FK_app_analytics_app_analytics_type_id` FOREIGN KEY (`app_analytics_key_id`) REFERENCES `tbl_app_analytics_key` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_app_analytics_user_id` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;

-- tbl_app_analytics_key_pair
CREATE TABLE `tbl_app_analytics_key_pair`
(
    `id`            BIGINT   NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT   NULL,
    `created_date`  DATETIME NOT NULL,
    `modified_by`   BIGINT   NULL,
    `modified_date` DATETIME NOT NULL,
    `key_one`       BIGINT   NOT NULL,
    `key_two`       BIGINT   NULL,
    PRIMARY KEY (`id`),
    UNIQUE `UK_tbl_app_analytics_key_pair_one_two` (`key_one`, `key_two`),
    KEY `FK_tbl_app_analytics_key_pair_key_one_id` (`key_one`),
    KEY `FK_tbl_app_analytics_key_pair_key_two_id` (`key_two`),
    CONSTRAINT `FK_tbl_app_analytics_key_pair_key_one_id` FOREIGN KEY (`key_one`) REFERENCES `tbl_app_analytics_key` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_tbl_app_analytics_key_pair_key_two_id` FOREIGN KEY (`key_two`) REFERENCES `tbl_app_analytics_key` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;