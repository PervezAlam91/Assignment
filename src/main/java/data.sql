--Inserting some employees on start up
DELETE FROM EMPLOYEES;
INSERT INTO EMPLOYEES (id,first_Name,last_Name,email_address) VALUES
  (1000,'alam', 'alam', 'abc@gmail.com');
INSERT INTO EMPLOYEES (id,first_Name,last_Name,email_address) VALUES
  (1001,'palam', 'palam', 'abcd@gmail.com');
INSERT INTO EMPLOYEES (id,first_Name,last_Name,email_address) VALUES
  (1002,'pervez', 'palam', 'delete@gmail.com');