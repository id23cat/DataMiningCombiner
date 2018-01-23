-- user: admin
-- password: adminpass
INSERT INTO ACCOUNT(password, user_name, email, first_Name, last_Name, role, created, dtype) 
			VALUES ('cat42pass', 'idcat', 'idcat@mail.org', 'Alex', 'Dem', 'USER', CURRENT_TIMESTAMP(), 'Account');

INSERT INTO PROJECT(name, project_type, account_id, created)
			VALUES ('test0', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='idcat'), CURRENT_TIMESTAMP());
INSERT INTO PROJECT(name, project_type, account_id, created)
			VALUES ('test1', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='idcat'), CURRENT_TIMESTAMP());
INSERT INTO PROJECT(name, project_type, account_id, created)
			VALUES ('test2', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='idcat'), CURRENT_TIMESTAMP());
			
INSERT INTO ALGORITHM(name,	PARENT_PROJECT_ID)
			VALUES('alg1', (SELECT id FROM PROJECT WHERE name='test0'));
INSERT INTO PROJECT_ALGORITHM (project_id, algorithm_id)
			VALUES((SELECT id FROM PROJECT WHERE name='test0'), (SELECT id FROM ALGORITHM WHERE name='alg1'));