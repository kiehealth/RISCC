-- tbl_answer
CREATE TABLE `tbl_answer`
(
    `id`                     BIGINT   NOT NULL AUTO_INCREMENT,
    `created_by`             BIGINT   NULL,
    `created_date`           DATETIME NOT NULL,
    `modified_by`            BIGINT   NULL,
    `modified_date`          DATETIME NOT NULL,
    `answer`                 TEXT     NULL,
    `question_id`            BIGINT   NOT NULL,
    `group_questionnaire_id` BIGINT   NOT NULL,
    `user_id`                BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_answer_question_id` (`question_id`),
    KEY `FK_answer_group_questionnaire_id` (`group_questionnaire_id`),
    KEY `FK_answer_user_id` (`user_id`),
    CONSTRAINT `FK_answer_question_id` FOREIGN KEY (`question_id`) REFERENCES `tbl_question` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_answer_group_questionnaire_id` FOREIGN KEY (`group_questionnaire_id`) REFERENCES `tbl_group_questionnaire` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_answer_user_id` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;
