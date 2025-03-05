CREATE TABLE `tbl_answer_option`
(
    `id`                 BIGINT   NOT NULL AUTO_INCREMENT,
    `created_by`         BIGINT   NULL,
    `created_date`       DATETIME NOT NULL,
    `modified_by`        BIGINT   NULL,
    `modified_date`      DATETIME NOT NULL,
    `value`              TEXT     NULL,
    `question_option_id` BIGINT   NOT NULL,
    `answer_id`          BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_answer_option_question_option_id` (`question_option_id`),
    KEY `FK_answer_option_answer_id` (`answer_id`),
    CONSTRAINT `FK_answer_option_question_option_id` FOREIGN KEY (`question_option_id`) REFERENCES `tbl_question_option` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_answer_option_answer_id` FOREIGN KEY (`answer_id`) REFERENCES `tbl_answer` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;
