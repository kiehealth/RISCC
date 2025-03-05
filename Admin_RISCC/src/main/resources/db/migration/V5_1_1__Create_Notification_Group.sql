CREATE TABLE `tbl_notification_group`
(
    `notification_id` BIGINT NOT NULL,
    `group_id`        BIGINT NOT NULL,
    PRIMARY KEY (`notification_id`, `group_id`),
    KEY `FK_notification_group_notification_id` (`notification_id`),
    KEY `FK_notification_group_group_id` (`group_id`),
    CONSTRAINT `FK_notification_group_notification_id` FOREIGN KEY (`notification_id`) REFERENCES `tbl_notification` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_notification_group_group_id` FOREIGN KEY (`group_id`) REFERENCES `tbl_group` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;