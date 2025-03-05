CREATE TABLE `tbl_riscc_range`
(
    `id`                 BIGINT     NOT NULL  AUTO_INCREMENT,
    `created_by`         BIGINT     NULL,
    `created_date`       DATETIME   NOT NULL,
    `modified_by`        BIGINT     NULL,
    `modified_date`      DATETIME   NOT NULL,
    `from_value`         VARCHAR(255)      NOT NULL,
    `to_value`           VARCHAR(255)     NOT NULL,
    `message`            VARCHAR(255)      NOT NULL,
    `questionnaire_id`   BIGINT     NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_riscc_range_questionnaire_id` (`questionnaire_id`),
    CONSTRAINT `FK_riscc_range_questionnaire_id` FOREIGN KEY (`questionnaire_id`) REFERENCES tbl_questionnaire (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;
