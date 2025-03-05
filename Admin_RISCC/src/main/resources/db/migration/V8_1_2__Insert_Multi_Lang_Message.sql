INSERT INTO `tbl_multi_lang_message` (id, created_date, modified_date, code, english, swedish)
VALUES (1, NOW(), NOW(), 'ERR001', 'Something went wrong', 'Något gick fel'),
       (2, NOW(), NOW(), 'ERR002', 'Username and/or password is wrong.', 'Fel användarnamn och / eller lösenord'),
       (3, NOW(), NOW(), 'ERR003', 'You are not Authorized.', 'Du är inte auktoriserad'),
       (4, NOW(), NOW(), 'ERR004', 'Invalid token.', 'Ogiltigt token.'),
       (5, NOW(), NOW(), 'ERR005', 'Super Admin cannot be deleted.', 'Superadmin kan inte raderas.'),
       (6, NOW(), NOW(), 'ERR006', 'No token found.', 'Ingen token hittades.'),
       (7, NOW(), NOW(), 'ERR007', 'Check for Duplicate data', 'Sök efter duplicerade data'),

       -- Role
       (8, NOW(), NOW(), 'ROL001', 'Role does not exist.', 'Roll finns inte.'),
       (9, NOW(), NOW(), 'ROL002', 'Role already exists.', 'Roll finns redan.'),
       (10, NOW(), NOW(), 'ROL003', 'Restricted Role.', 'Begränsad roll.'),
       (11, NOW(), NOW(), 'ROL004', 'Role Id not provided.', 'Roll-id anges inte.'),

       -- Verification
       (12, NOW(), NOW(), 'VER001', 'Verification details are missing.', 'Verifieringsinformation saknas.'),
       (13, NOW(), NOW(), 'VER002', 'Verification code is wrong or expired.',
        'Verifieringskoden är felaktig eller är inte längre giltig.'),
       (14, NOW(), NOW(), 'VER003', 'Email address did not match.', 'E-postadressen matchade inte.'),
       (15, NOW(), NOW(), 'VER004', 'Mobile number did not match.', 'Mobilnumret matchade inte.'),
       (16, NOW(), NOW(), 'VER005', 'Email Address or Mobile Number is missing.',
        'E-postadress eller mobilnummer saknas.'),
       (17, NOW(), NOW(), 'VER006',
        'Your Email Address has not been added to the allowed registration list. Please contact RISCC Support Team.',
        'Din e-postadress har inte lagts till i deltagarlistan. Kontakta RISCC Support Team'),
       (18, NOW(), NOW(), 'VER007', 'Identity is not provided.', 'Identitet inte tillhandahålls'),
       (19, NOW(), NOW(), 'VER008', 'Password is empty.', 'Lösenordet är tomt'),

       -- User
       (20, NOW(), NOW(), 'USR001', 'User does not exist.', 'Användare finns inte.'),
       (21, NOW(), NOW(), 'USR002', 'User already exists.', 'Användare finns redan.'),
       (22, NOW(), NOW(), 'USR003', 'You don\'t have permission to change the password.',
        'Du har inte behörighet att ändra lösenordet.'),
       (23, NOW(), NOW(), 'USR004', 'Email Address is missing.', 'E-postadress saknas.'),
       (24, NOW(), NOW(), 'USR005', 'Email Address already exists.', 'Emailadressen finns redan.'),
       (25, NOW(), NOW(), 'USR006', 'Mobile Number already exists.', 'Mobilnummer finns redan.'),
       (26, NOW(), NOW(), 'USR007', 'User with this email address already exists.',
        'Användare med den här e-postadressen finns redan.'),
       (27, NOW(), NOW(), 'USR008', 'User with this mobile number already exists.',
        'Användare med det här mobilnumret finns redan.'),
       (28, NOW(), NOW(), 'USR009', 'Allowed registration does not exist.', 'Tillåten registrering finns inte.'),
       (29, NOW(), NOW(), 'USR010', 'Device does not exist.', 'Enheten finns inte.'),

       -- Country
       (30, NOW(), NOW(), 'COU001', 'Country does not exist.', 'Land finns inte.'),

       -- Authority
       (31, NOW(), NOW(), 'AUT001', 'Authority already exists.', 'Auktoritet finns redan.'),
       (32, NOW(), NOW(), 'AUT002', 'Authority does not exist.', 'Auktoritet finns inte.'),

       -- About Us
       (33, NOW(), NOW(), 'ABT001', 'About Us does not exist.', 'Om användning finns inte.'),

       -- App Analytics
       (34, NOW(), NOW(), 'APA001', 'App Analytics Type does not exist.', 'App Analytics-typ finns inte.'),
       (35, NOW(), NOW(), 'APA002', 'App Analytics Key has already been paired.',
        'App Analytics-nyckeln har redan parats ihop.'),
       (36, NOW(), NOW(), 'APA003', 'App Analytics Type pair does not exist.', 'Appparatyp finns inte.'),

       -- Group
       (37, NOW(), NOW(), 'GRP001', 'Group already exists.', 'Gruppen finns redan.'),
       (38, NOW(), NOW(), 'GRP002', 'Group does not exist.', 'Gruppen finns inte.'),
       (39, NOW(), NOW(), 'GRP003', 'Group questionnaire does not exist.', 'Grupp enkät finns inte.'),
       (40, NOW(), NOW(), 'GRP004', 'User has finished answer for this group questionnaire.',
        'Användaren har slutfört svaret på detta gruppenkät'),
       (41, NOW(), NOW(), 'GRP005', 'Group Ids not provided.', 'SökGrupp-id anges inte'),

       -- Questionnaire
       (42, NOW(), NOW(), 'QUE001', 'Questionnaire does not exist.', 'Enkät finns inte.'),
       (43, NOW(), NOW(), 'QUE002', 'Question does not exist.', 'Frågan finns inte.'),
       (44, NOW(), NOW(), 'QUE003', 'Already answered.', 'Har redan svarat'),
       (45, NOW(), NOW(), 'QUE004', 'Answer data is missing.', 'Svarsdata saknas'),
       (46, NOW(), NOW(), 'QUE005', 'Question option does not exist.', 'Frågan finns inte'),
       (47, NOW(), NOW(), 'QUE006', 'Questionnaire already exists.', 'Questionnaire already exists.'),
       (48, NOW(), NOW(), 'QUE007', 'Question Questionnaire does not exist.', 'Enkät finns inte'),
       (49, NOW(), NOW(), 'QUE008', 'Please delete associated questions before deleting questionnaire.',
        'Ta bort associerade frågor innan du tar bort enkäten.'),
       (50, NOW(), NOW(), 'QUE009', 'Question already exists.', 'Frågan finns redan.'),
       (51, NOW(), NOW(), 'QUE010', 'Question Type does not exist.', 'Frågetyp finns inte.'),
       (52, NOW(), NOW(), 'QUE011', 'Please delete associated answers first.', 'Ta bort associerade svar först.'),
       (53, NOW(), NOW(), 'QUE012', 'Question Title required.', 'Frågetitel krävs.'),
       (54, NOW(), NOW(), 'QUE013', 'Question body required.', 'Frågeställning krävs.'),

       -- SMS
       (55, NOW(), NOW(), 'SMS001', 'Failed to send SMS.', 'Det gick inte att skicka SMS'),

       -- App Version
       (56, NOW(), NOW(), 'APV001', 'App Version does not exist.', 'Appversion finns inte.'),

       -- Link
       (57, NOW(), NOW(), 'LNK001', 'Link already exists.', 'Länken finns redan.'),
       (58, NOW(), NOW(), 'LNK002', 'Link does not exist.', 'Länk finns inte.'),

       -- Note
       (59, NOW(), NOW(), 'NOT001', 'Note does not exist.', 'Not finns inte.'),

       -- Notification
       (60, NOW(), NOW(), 'NTI001', 'Notification does not exist.', 'Meddelande finns inte.'),

       -- Resource
       (61, NOW(), NOW(), 'RES001', 'Only PDF file is allowed.', 'Endast PDF-filer är tillåtna.'),
       (62, NOW(), NOW(), 'RES002', 'Resource file does not exist.', 'Resursfilen finns inte.'),

       -- Setting
       (63, NOW(), NOW(), 'SET001', 'Setting does not exist.', 'Inställningen finns inte.'),
       (64, NOW(), NOW(), 'SET002', 'Welcome does not exist.', 'Välkomsttexten finns inte.')
;