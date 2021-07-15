TRUNCATE users, transfers, accounts CASCADE;

INSERT INTO users (user_id, username, password_hash)
VALUES ('1001', 'testOne', '$2a$10$66Z1y0IHKHPZWbAYjzzK/OVXooVM2H8Kyp/CFd/HPkCtqpiPfwVsy');
INSERT INTO users (user_id, username, password_hash)
VALUES ('1002', 'testTwo', '$2a$10$66Z1y0IHKHPZWbAYjzzK/OVXooVM2H8Kyp/CFd/HPkCtqpiPfwVsy');
INSERT INTO users (user_id, username, password_hash)
VALUES ('1003', 'testThree', '$2a$10$66Z1y0IHKHPZWbAYjzzK/OVXooVM2H8Kyp/CFd/HPkCtqpiPfwVsy');
INSERT INTO users (user_id, username, password_hash)
VALUES ('1004', 'testFour', '$2a$10$66Z1y0IHKHPZWbAYjzzK/OVXooVM2H8Kyp/CFd/HPkCtqpiPfwVsy');


INSERT INTO accounts (account_id, user_id, balance)
VALUES ('2001', '1001', '1000.00');
INSERT INTO accounts (account_id, user_id, balance)
VALUES ('2002', '1002', '1000.00');
INSERT INTO accounts (account_id, user_id, balance)
VALUES ('2003', '1003', '1000.00');
INSERT INTO accounts (account_id, user_id, balance)
VALUES ('2004', '1004', '1000.00');


INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES ('3001', '2', '2', '2001', '2002', '100.00');
INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES ('3002', '2', '2', '2003', '2004', '100.00');
INSERT INTO transfers (transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount)
VALUES('3003', '2', '2', '2001', '2003', '100.00');