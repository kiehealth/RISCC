CREATE TABLE `tbl_notification_role`
(
    `notification_id` BIGINT NOT NULL,
    `role_id`         BIGINT NOT NULL,
    PRIMARY KEY (`notification_id`, `role_id`),
    KEY `FK_notification_role_notification_id` (`notification_id`),
    KEY `FK_notification_role_role_id` (`role_id`),
    CONSTRAINT `FK_notification_role_notification_id` FOREIGN KEY (`notification_id`) REFERENCES `tbl_notification` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_notification_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `tbl_role` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;