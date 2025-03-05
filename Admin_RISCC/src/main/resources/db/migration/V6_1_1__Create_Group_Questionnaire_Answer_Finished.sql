CREATE TABLE `tbl_group_questionnaire_answer_finished`
(
    `id`                     BIGINT   NOT NULL AUTO_INCREMENT,
    `created_by`             BIGINT   NULL,
    `created_date`           DATETIME NOT NULL,
    `modified_by`            BIGINT   NULL,
    `modified_date`          DATETIME NOT NULL,
    `finished_date_time`     DATETIME NOT NULL,
    `user_id`                BIGINT   NOT NULL,
    `group_questionnaire_id` BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_group_questionnaire_answer_finished_user_id` (`user_id`),
    KEY `FK_group_questionnaire_answer_finished_group_questionnaire_id` (`group_questionnaire_id`),
    CONSTRAINT `FK_group_questionnaire_answer_finished_user_id` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_group_questionnaire_answer_finished_group_questionnaire_id` FOREIGN KEY (`group_questionnaire_id`) REFERENCES `tbl_group_questionnaire` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;