INSERT INTO user (id, passwd, user_id, user_name) values (1, 'test', 'koo', 'koosangyoon');
INSERT INTO user (id, passwd, user_id, user_name) values (2, 'test', 'jack', 'kwonJack');
INSERT INTO user (id, passwd, user_id, user_name) values (3, 'test', 'pobi', 'parkpobi');

INSERT INTO to_do (id, created_date, modified_date, is_done, ready_state, title, refering_to_dos_id) values (1, 201803041800, 201803041900, false, false, 'testToDo1', null);
INSERT INTO to_do (id, created_date, modified_date, is_done, ready_state, title, refering_to_dos_id) values (2, 201803051800, 201803051900, false, false, 'testToDo2', 1);
INSERT INTO to_do (id, created_date, modified_date, is_done, ready_state, title, refering_to_dos_id) values (3, 201803061800, 201803061900, false, true, 'testToDo3', 2);