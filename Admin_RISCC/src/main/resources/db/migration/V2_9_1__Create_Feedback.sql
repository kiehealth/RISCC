CREATE TABLE `tbl_feedback`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `title`         VARCHAR(256) NOT NULL,
    `description`   TEXT         NULL,
    `user_id`       BIGINT       NOT NULL,
    PRIMARY KEY (`id`),
    KEY `FK_feedback_user_id` (`user_id`),
    CONSTRAINT `FK_feedback_user_id` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;