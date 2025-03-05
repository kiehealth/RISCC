-- Create tbl_group
CREATE TABLE `tbl_group`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `title`         VARCHAR(256) NOT NULL,
    `description`   TEXT         NULL,
    PRIMARY KEY (`id`),
    UNIQUE `UK_group_title` (`title`)
) ENGINE = INNODB;

-- Insert authorities of Group
INSERT INTO tbl_authority (created_date, modified_date, title)
VALUES (NOW(), NOW(), 'GROUP'),
       (NOW(), NOW(), 'GROUP_RA'),
       (NOW(), NOW(), 'GROUP_C'),
       (NOW(), NOW(), 'GROUP_U'),
       (NOW(), NOW(), 'GROUP_D');

-- Insert Group authorities to provide access to Super Admin
INSERT INTO tbl_role_authority(role_id, authority_id)
VALUES ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'GROUP')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'GROUP_RA')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'GROUP_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'GROUP_U')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'GROUP_D'));