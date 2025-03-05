CREATE TABLE `tbl_question_type`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `title`         VARCHAR(256) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_question_type_title` (`title`)
) ENGINE = INNODB;

INSERT INTO `tbl_question_type` (created_date, modified_date, title)
VALUES (NOW(), NOW(), 'DATA_INPUT'),
       (NOW(), NOW(), 'SELECT_ONE'),
       (NOW(), NOW(), 'SELECT_MORE_THAN_ONE'),
       (NOW(), NOW(), 'RATING'),
       (NOW(), NOW(), 'NONE');