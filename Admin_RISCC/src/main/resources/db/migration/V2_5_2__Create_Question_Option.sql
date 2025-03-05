CREATE TABLE `tbl_question_option`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`     BIGINT       NULL,
    `created_date`   DATETIME     NOT NULL,
    `modified_by`    BIGINT       NULL,
    `modified_date`  DATETIME     NOT NULL,
    `title`          VARCHAR(256) NOT NULL,
    `value`          VARCHAR(256) NULL,
    `research_id`    VARCHAR(256) NULL,
    `option_message` VARCHAR(256) NULL,
    `notify_user`    BOOLEAN      NULL,
    `other_email`    VARCHAR(256) NULL,
    `notify_other`   BOOLEAN      NULL,
    `question_id`    BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_question_option_question_id` (`question_id`),
    CONSTRAINT `FK_question_option_question_id` FOREIGN KEY (`question_id`) REFERENCES tbl_question (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;