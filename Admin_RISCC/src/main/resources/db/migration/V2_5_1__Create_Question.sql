-- tbl_question
CREATE TABLE `tbl_question`
(
    `id`               BIGINT   NOT NULL AUTO_INCREMENT,
    `created_by`       BIGINT   NULL,
    `created_date`     DATETIME NOT NULL,
    `modified_by`      BIGINT   NULL,
    `modified_date`    DATETIME NOT NULL,
    `title`            TEXT     NOT NULL,
    `body`             TEXT     NOT NULL,
    `question_type_id` BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_question_question_type_id` (`question_type_id`),
    CONSTRAINT `FK_question_question_type_id` FOREIGN KEY (`question_type_id`) REFERENCES tbl_question_type (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;