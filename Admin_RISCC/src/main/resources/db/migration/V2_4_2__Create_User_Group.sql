CREATE TABLE `tbl_user_group`
(
    `user_id`  BIGINT NOT NULL,
    `group_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `group_id`),
    KEY `FK_user_group_user_id` (`user_id`),
    KEY `FK_user_group_group_id` (`group_id`),
    UNIQUE `UK_user_group_user_id_group_id` (`user_id`, `group_id`),
    CONSTRAINT `FK_user_group_user_id` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT `FK_user_group_group_id` FOREIGN KEY (`group_id`) REFERENCES `tbl_group` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;