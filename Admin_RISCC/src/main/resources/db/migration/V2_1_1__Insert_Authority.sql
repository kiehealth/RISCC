INSERT INTO tbl_authority (created_date, modified_date, title)
VALUES (NOW(), NOW(), 'QUESTION_TYPE'),
       (NOW(), NOW(), 'QUESTION_TYPE_C'),
       (NOW(), NOW(), 'QUESTION_TYPE_U'),
       (NOW(), NOW(), 'QUESTION_TYPE_D'),

       (NOW(), NOW(), 'QUESTIONNAIRE'),
       (NOW(), NOW(), 'QUESTIONNAIRE_C'),
       (NOW(), NOW(), 'QUESTIONNAIRE_U'),
       (NOW(), NOW(), 'QUESTIONNAIRE_D'),

       (NOW(), NOW(), 'QUESTION'),
       (NOW(), NOW(), 'QUESTION_C'),
       (NOW(), NOW(), 'QUESTION_U'),
       (NOW(), NOW(), 'QUESTION_D'),

       (NOW(), NOW(), 'LINK'),
       (NOW(), NOW(), 'LINK_C'),
       (NOW(), NOW(), 'LINK_U'),
       (NOW(), NOW(), 'LINK_D'),

       (NOW(), NOW(), 'APP_VERSION_U'),

       (NOW(), NOW(), 'NOTIFICATION'),
       (NOW(), NOW(), 'NOTIFICATION_C'),
       (NOW(), NOW(), 'NOTIFICATION_RA'),
       (NOW(), NOW(), 'NOTIFICATION_D'),

       (NOW(), NOW(), 'FEEDBACK'),
       (NOW(), NOW(), 'FEEDBACK_C'),
       (NOW(), NOW(), 'FEEDBACK_RA'),
       (NOW(), NOW(), 'FEEDBACK_D'),

       (NOW(), NOW(), 'ANSWER_RA'),
       (NOW(), NOW(), 'ANSWER_D'),

       (NOW(), NOW(), 'SETTING'),
       (NOW(), NOW(), 'SETTING_U'),

       (NOW(), NOW(), 'APP_ANALYTICS');

INSERT INTO tbl_role_authority(role_id, authority_id)
VALUES ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTION_TYPE')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTION_TYPE_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTION_TYPE_U')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTION_TYPE_D')),

       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTIONNAIRE')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTIONNAIRE_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTIONNAIRE_U')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTIONNAIRE_D')),

       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTION')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTION_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTION_U')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'QUESTION_D')),

       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'LINK')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'LINK_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'LINK_U')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'LINK_D')),

       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'NOTIFICATION')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'NOTIFICATION_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'NOTIFICATION_RA')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'NOTIFICATION_D')),

       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'APP_VERSION_U')),

       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'FEEDBACK')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'FEEDBACK_C')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'FEEDBACK_RA')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'FEEDBACK_D')),

       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'ANSWER_RA')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'ANSWER_D')),

       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'SETTING')),
       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'SETTING_U')),

       ((SELECT id FROM tbl_role WHERE title = 'Super Admin'),
        (SELECT id FROM tbl_authority WHERE title = 'APP_ANALYTICS'));