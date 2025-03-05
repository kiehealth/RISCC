CREATE TABLE `tbl_notification_user`
(
    `notification_id` BIGINT NOT NULL,
    `user_id`         BIGINT NOT NULL,
    PRIMARY KEY (`notification_id`, `user_id`),
    KEY `FK_notification_user_notification_id` (`notification_id`),
    KEY `FK_notification_user_user_id` (`user_id`),
    CONSTRAINT `FK_notification_user_notification_id` FOREIGN KEY (`notification_id`) REFERENCES `tbl_notification` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_notification_user_user_id` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;