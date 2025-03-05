-- Create tbl_note
CREATE TABLE `tbl_note`
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
    KEY `FK_note_user_id` (`user_id`),
    CONSTRAINT `FK_note_user_id` FOREIGN KEY (`user_id`) REFERENCES `tbl_user` (`id`)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE = INNODB;

-- Insert authorities of Note
INSERT INTO tbl_authority (created_date, modified_date, title)
VALUES (NOW(), NOW(), 'NOTE'),
       (NOW(), NOW(), 'NOTE_C'),
       (NOW(), NOW(), 'NOTE_U'),
       (NOW(), NOW(), 'NOTE_D');

-- Insert Note authorities to provide access to Super Admin
INSERT INTO tbl_role_authority(role_id, authority_id)
VALUES ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'NOTE')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'NOTE_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'NOTE_U')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'NOTE_D'));