CREATE TABLE `tbl_group_questionnaire`
(
    `id`                     BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`             BIGINT       NULL,
    `created_date`           DATETIME     NOT NULL,
    `modified_by`            BIGINT       NULL,
    `modified_date`          DATETIME     NOT NULL,
    `display_order`          INT          NULL,
    `start_date_time`        DATETIME     NOT NULL,
    `end_date_time`          DATETIME     NOT NULL,
    `reminder_message`       VARCHAR(500) NULL,
    `reminder_time_interval` INT          NULL,
    `group_id`               BIGINT       NOT NULL,
    `questionnaire_id`       BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_group_questionnaire_group_id` (`group_id`),
    KEY `FK_group_questionnaire_questionnaire_id` (`questionnaire_id`),
    UNIQUE `UK_group_questionnaire_group_id_questionnaire_id` (`group_id`, `questionnaire_id`),
    CONSTRAINT `FK_group_questionnaire_questionnaire_id` FOREIGN KEY (`questionnaire_id`) REFERENCES `tbl_questionnaire` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_group_questionnaire_group_id` FOREIGN KEY (`group_id`) REFERENCES `tbl_group` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;