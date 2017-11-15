-- user: admin
-- password: adminpass
INSERT INTO ACCOUNT(id, password, user_name, email, first_Name, last_Name, role, created, dtype) 
			VALUES (1, 'adminpass', 'admin', 'admin@mail.org', 'Admin', 'Admin', 'ROLE_ADMIN', CURRENT_TIMESTAMP(), 'Account');