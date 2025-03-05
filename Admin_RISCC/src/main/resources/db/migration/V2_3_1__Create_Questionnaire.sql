CREATE TABLE `tbl_questionnaire`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `title`         VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_questionnaire_title` (`title`)
) ENGINE = INNODB;