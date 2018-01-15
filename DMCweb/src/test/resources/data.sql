-- user: admin
-- password: adminpass
--INSERT INTO ACCOUNT(password, user_name, email, first_Name, last_Name, role, created, dtype) 
--			VALUES ('cat42pass', 'idcat', 'idcat@mail.org', 'Alex', 'Dem', 'USER', CURRENT_TIMESTAMP(), 'Account');
			
INSERT INTO ACCOUNT(password, user_name, email, first_Name, last_Name, role, created, dtype) 
			VALUES ('cat42pass', 'id23cat', 'id22cat@mail.org', 'Alex', 'Dem', 'USER', CURRENT_TIMESTAMP(), 'Account');

INSERT INTO PROJECT(name, project_type, account_id, created)
			VALUES ('test0', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='id23cat'), CURRENT_TIMESTAMP());
INSERT INTO PROJECT(name, project_type, account_id, created)
			VALUES ('test1', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='id23cat'), CURRENT_TIMESTAMP());
INSERT INTO PROJECT(name, project_type, account_id, created)
			VALUES ('test2', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='id23cat'), CURRENT_TIMESTAMP());