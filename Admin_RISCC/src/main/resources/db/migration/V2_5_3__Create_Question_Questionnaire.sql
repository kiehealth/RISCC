CREATE TABLE `tbl_question_questionnaire`
(
    `id`               BIGINT   NOT NULL AUTO_INCREMENT,
    `created_by`       BIGINT   NULL,
    `created_date`     DATETIME NOT NULL,
    `modified_by`      BIGINT   NULL,
    `modified_date`    DATETIME NOT NULL,
    `display_order`    INT      NULL,
    `question_id`      BIGINT   NOT NULL,
    `questionnaire_id` BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_question_questionnaire_question_id` (`question_id`),
    CONSTRAINT `FK_question_questionnaire_question_id` FOREIGN KEY (`question_id`) REFERENCES tbl_question (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    KEY `FK_question_questionnaire_questionnaire_id` (`questionnaire_id`),
    CONSTRAINT `FK_question_questionnaire_questionnaire_id` FOREIGN KEY (`questionnaire_id`) REFERENCES tbl_questionnaire (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;