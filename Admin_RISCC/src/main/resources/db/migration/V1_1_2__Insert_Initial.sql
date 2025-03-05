-- tbl_authority
INSERT INTO `tbl_authority` (id, created_date, modified_date, title)
VALUES (1, NOW(), NOW(), 'COUNTRY_C'),
       (2, NOW(), NOW(), 'COUNTRY_U'),
       (3, NOW(), NOW(), 'COUNTRY_D'),

       (4, NOW(), NOW(), 'STATE_C'),
       (5, NOW(), NOW(), 'STATE_U'),
       (6, NOW(), NOW(), 'STATE_D'),

       (7, NOW(), NOW(), 'CITY_C'),
       (8, NOW(), NOW(), 'CITY_U'),
       (9, NOW(), NOW(), 'CITY_D'),

       (10, NOW(), NOW(), 'AUTHORITY'),
       (11, NOW(), NOW(), 'AUTHORITY_C'),
       (12, NOW(), NOW(), 'AUTHORITY_U'),
       (13, NOW(), NOW(), 'AUTHORITY_D'),

       (14, NOW(), NOW(), 'ROLE'),
       (15, NOW(), NOW(), 'ROLE_RA'),
       (16, NOW(), NOW(), 'ROLE_C'),
       (17, NOW(), NOW(), 'ROLE_U'),
       (18, NOW(), NOW(), 'ROLE_D'),

       (19, NOW(), NOW(), 'USER'),
       (20, NOW(), NOW(), 'USER_RA'),
       (21, NOW(), NOW(), 'USER_C'),
       (22, NOW(), NOW(), 'USER_U'),
       (23, NOW(), NOW(), 'USER_D'),
       (24, NOW(), NOW(), 'CHANGE_PASSWORD');

-- tbl_role
INSERT INTO `tbl_role` (id, created_date, modified_date, title)
VALUES (1, NOW(), NOW(), 'Super Admin');


-- tbl_role_authority
INSERT INTO `tbl_role_authority` (role_id, authority_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15),
       (1, 16),
       (1, 17),
       (1, 18),
       (1, 19),
       (1, 20),
       (1, 21),
       (1, 22),
       (1, 23),
       (1, 24)
;


-- tbl_user
INSERT INTO `tbl_user` (id, created_date, modified_date, mobile_number, email_address, password, first_name, last_name,
                        gender, email_address_verified, last_password_reset_date, role_id)
VALUES (1, NOW(), NOW(), 0123456789, 'sa@yopmail.com', '$2a$04$ND04Es6dx5AtjOuNzmbvPOkrwMCrs2oF9Zj/focP5TG9KeFaZ8okq',
        'Super', 'Admin', 'MALE', true, NOW(), 1);
