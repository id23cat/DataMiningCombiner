-- user: admin
-- password: adminpass
INSERT INTO ACCOUNT(password, user_name, email, first_Name, last_Name, role, created, dtype) 
			VALUES ('devel', 'devel', 'devel@mail.org', 'Devel', 'Oper', 'USER', CURRENT_TIMESTAMP(), 'Account');

INSERT INTO PROJECT(name, project_type, account_id, created)
			VALUES ('proj0', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='devel'), CURRENT_TIMESTAMP());
INSERT INTO PROJECT(name, project_type, account_id, created)
			VALUES ('proj1', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='devel'), CURRENT_TIMESTAMP());
INSERT INTO PROJECT(name, project_type, account_id, created)
			VALUES ('proj2', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='devel'), CURRENT_TIMESTAMP());
			
INSERT INTO ALGORITHM(name,	PARENT_PROJECT_ID, DESCRIPTION, entity_type)
			VALUES('alg1', (SELECT id FROM PROJECT WHERE name='proj0'), 'devel Algorithm', 'subalg');
INSERT INTO PROJECT_ALGORITHM (project_id, algorithm_id)
			VALUES((SELECT id FROM PROJECT WHERE name='proj0'), (SELECT id FROM ALGORITHM WHERE name='alg1'));