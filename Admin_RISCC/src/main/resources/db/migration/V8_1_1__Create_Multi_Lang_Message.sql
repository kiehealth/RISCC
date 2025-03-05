CREATE TABLE `tbl_multi_lang_message`
(
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `created_by`    BIGINT       NULL,
    `created_date`  DATETIME     NOT NULL,
    `modified_by`   BIGINT       NULL,
    `modified_date` DATETIME     NOT NULL,
    `code`          VARCHAR(10)  NOT NULL,
    `english`       VARCHAR(256) NOT NULL,
    `swedish`       VARCHAR(256) NULL,
    `spanish`       VARCHAR(256) NULL,
    PRIMARY KEY (`id`),
    UNIQUE `UK_multi_lang_message_code` (`code`)
) ENGINE = INNODB;

-- Insert authorities of Multi Lang Message
INSERT INTO tbl_authority (created_date, modified_date, title)
VALUES (NOW(), NOW(), 'MULTI_LANG_MESSAGE'),
       (NOW(), NOW(), 'MULTI_LANG_MESSAGE_C'),
       (NOW(), NOW(), 'MULTI_LANG_MESSAGE_U'),
       (NOW(), NOW(), 'MULTI_LANG_MESSAGE_D');

-- Insert Multi Lang Message List authorities to provide access to Super Admin
INSERT INTO tbl_role_authority(role_id, authority_id)
VALUES ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'MULTI_LANG_MESSAGE')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'MULTI_LANG_MESSAGE_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'MULTI_LANG_MESSAGE_U')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'MULTI_LANG_MESSAGE_D'));