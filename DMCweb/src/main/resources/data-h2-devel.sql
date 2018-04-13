-- user: admin
-- password: adminpass
--INSERT INTO ACCOUNT(password, user_name, email, first_Name, last_Name, role, created, dtype) 
--			VALUES ('devel', 'devel', 'devel@mail.org', 'Devel', 'Oper', 'USER', CURRENT_TIMESTAMP(), 'Account');
--
--INSERT INTO PROJECT(name, project_type, account_id, created)
--			VALUES ('proj0', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='devel'), CURRENT_TIMESTAMP());
--INSERT INTO PROJECT(name, project_type, account_id, created)
--			VALUES ('proj1', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='devel'), CURRENT_TIMESTAMP());
--INSERT INTO PROJECT(name, project_type, account_id, created)
--			VALUES ('proj2', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='devel'), CURRENT_TIMESTAMP());
--			
--INSERT INTO ALGORITHM(name,	PARENT_PROJECT_ID, DESCRIPTION, entity_type)
--			VALUES('alg1', (SELECT id FROM PROJECT WHERE name='proj0'), 'devel Algorithm', 'subalg');
--INSERT INTO PROJECT_ALGORITHM (project_id, algorithm_id)
--	
-- account:
INSERT INTO ACCOUNT(DTYPE, CREATED, EMAIL, FIRST_NAME, LAST_NAME, PASSWORD, ROLE, USER_NAME) VALUES
('Account', CURRENT_TIMESTAMP(), 'devel@mail.org', 'Devel', 'Oper', 'devel', 'USER', 'devel');

-- project:
INSERT INTO PROJECT(CREATED, NAME, PROJECT_TYPE, ACCOUNT_ID) VALUES
(CURRENT_TIMESTAMP(), 'proj0', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='devel')),
(CURRENT_TIMESTAMP(), 'proj1', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='devel')),
(CURRENT_TIMESTAMP(), 'proj2', 'SIMPLEST_PROJECT', (SELECT id FROM ACCOUNT WHERE user_name='devel'));   

-- algorithm:
--INSERT INTO ALGORITHM(ENTITY_TYPE, DATA_DESTINATION, DATA_SOURCE, DESCRIPTION, NAME, SHARED, FUNCTION_MODEL, PARENT_PROJECT_ID) VALUES
--('subalg', NULL, NULL, 'devel Algorithm', 'alg1', FALSE, NULL, (SELECT id FROM PROJECT WHERE name='proj0'));
INSERT INTO ALGORITHM(NAME, PROJECT_ID) VALUES
('alg0', (SELECT id FROM PROJECT WHERE name='proj0'));

--INSERT INTO PROJECT_ALGORITHM(PROJECT_ID, ALGORITHM_ID) VALUES
--((SELECT id FROM PROJECT WHERE name='proj0'), (SELECT id FROM ALGORITHM WHERE name='alg1'));  

-- metadata:
INSERT INTO DATA_STORAGE(ID, DELIMITER, HAS_HEADER, STORAGE_TYPE, relative_path) VALUES
(1, STRINGDECODE(',;\t|'), TRUE, 'LOCAL_FS', 'devel/proj0/telecom_churn.csv'),
(2, STRINGDECODE(',;\t|'), TRUE, 'LOCAL_FS', 'devel/proj0/iris.csv')
;      

INSERT INTO METADATA(ID, DESCRIPTION, NAME, PROJECT_ID, STORAGE_ID) VALUES
(1, '', 'telecom', (SELECT id FROM PROJECT WHERE name='proj0'), 1),
(2, '', 'iris', (SELECT id FROM PROJECT WHERE name='proj0'), 2)
;   

INSERT INTO DATA_ATTRIBUTES(METADATA_ID, CHECKED, MULTIPLIER, NAME, TYPE, MAP_KEY) VALUES
(1, TRUE, 1.0, 'Total intl minutes', 'NUMERIC', 'Total intl minutes'),
(1, TRUE, 1.0, 'Total day charge', 'NUMERIC', 'Total day charge'),
(1, TRUE, 1.0, 'Total day calls', 'NUMERIC', 'Total day calls'),
(1, TRUE, 1.0, 'Number vmail messages', 'NUMERIC', 'Number vmail messages'),
(1, TRUE, 1.0, 'Churn', 'NOMINAL', 'Churn'),
(1, TRUE, 1.0, 'Total night charge', 'NUMERIC', 'Total night charge'),
(1, TRUE, 1.0, 'Total night minutes', 'NUMERIC', 'Total night minutes'),
(1, TRUE, 1.0, 'Total eve calls', 'NUMERIC', 'Total eve calls'),
(1, TRUE, 1.0, 'Customer service calls', 'NUMERIC', 'Customer service calls'),
(1, TRUE, 1.0, 'Total day minutes', 'NUMERIC', 'Total day minutes'),
(1, TRUE, 1.0, 'Total intl charge', 'NUMERIC', 'Total intl charge'),
(1, TRUE, 1.0, 'Total night calls', 'NUMERIC', 'Total night calls'),
(1, TRUE, 1.0, 'Area code', 'NUMERIC', 'Area code'),
(1, TRUE, 1.0, 'International plan', 'NOMINAL', 'International plan'),
(1, TRUE, 1.0, 'State', 'NOMINAL', 'State'),
(1, TRUE, 1.0, 'Total intl calls', 'NUMERIC', 'Total intl calls'),
(1, TRUE, 1.0, 'Total eve minutes', 'NUMERIC', 'Total eve minutes'),
(1, TRUE, 1.0, 'Account length', 'NUMERIC', 'Account length'),
(1, TRUE, 1.0, 'Total eve charge', 'NUMERIC', 'Total eve charge'),
(1, TRUE, 1.0, 'Voice mail plan', 'NOMINAL', 'Voice mail plan'),
(2, TRUE, 1.0, '1', 'NUMERIC', '1'),
(2, TRUE, 1.0, '2', 'NUMERIC', '2'),
(2, TRUE, 1.0, '3', 'NUMERIC', '3'),
(2, TRUE, 1.0, '4', 'NUMERIC', '4'),
(2, TRUE, 1.0, '5', 'NOMINAL', '5')
;

INSERT INTO DATA_PREVIEW(DELIMITER, META_DATA_ID, HEADER) VALUES
(',', 1, 'State,Account length,Area code,International plan,Voice mail plan,Number vmail messages,Total day minutes,Total day calls,Total day charge,Total eve minutes,Total eve calls,Total eve charge,Total night minutes,Total night calls,Total night charge,Total intl minutes,Total intl calls,Total intl charge,Customer service calls,Churn'),
(',', 2, '1,2,3,4,5')
;

UPDATE METADATA SET PREVIEW_ID=LAST_INSERT_ID() WHERE NAME='telecom';
UPDATE METADATA SET PREVIEW_ID=LAST_INSERT_ID() WHERE NAME='iris';

INSERT INTO DATA_PREVIEW_LIST(DATA_PREVIEW_ID, DATA) VALUES
--((SELECT id FROM DATA_PREVIEW WHERE META_DATA_ID=(SELECT id FROM METADATA WHERE name='telecom')), 'KS,128,415,No,Yes,25,265.1,110,45.07,197.4,99,16.78,244.7,91,11.01,10.0,3,2.7,1,False'),
(1, 'KS,128,415,No,Yes,25,265.1,110,45.07,197.4,99,16.78,244.7,91,11.01,10.0,3,2.7,1,False'),
(1, 'OH,107,415,No,Yes,26,161.6,123,27.47,195.5,103,16.62,254.4,103,11.45,13.7,3,3.7,1,False'),
(1, 'NJ,137,415,No,No,0,243.4,114,41.38,121.2,110,10.3,162.6,104,7.32,12.2,5,3.29,0,False'),
(1, 'OH,84,408,Yes,No,0,299.4,71,50.9,61.9,88,5.26,196.9,89,8.86,6.6,7,1.78,2,False'),
(1, 'OK,75,415,Yes,No,0,166.7,113,28.34,148.3,122,12.61,186.9,121,8.41,10.1,3,2.73,3,False'),
(1, 'AL,118,510,Yes,No,0,223.4,98,37.98,220.6,101,18.75,203.9,118,9.18,6.3,6,1.7,0,False'),
(1, 'MA,121,510,No,Yes,24,218.2,88,37.09,348.5,108,29.62,212.6,118,9.57,7.5,7,2.03,3,False'),
(2, '5.1,3.5,1.4,0.2,Iris-setosa'),
(2, '4.9,3.0,1.4,0.2,Iris-setosa'),
(2, '4.7,3.2,1.3,0.2,Iris-setosa'),
(2, '4.6,3.1,1.5,0.2,Iris-setosa'),
(2, '5.0,3.6,1.4,0.2,Iris-setosa'),
(2, '5.4,3.9,1.7,0.4,Iris-setosa'),
(2, '4.6,3.4,1.4,0.3,Iris-setosa')
;








