--INSERT INTO to_do (id, created_date, modified_date, is_done, ready_state, title, writer_id, refering_to_dos_id) values (1, 201803041800, 201803041900, false, false, 'testToDo1', 1, null);
--INSERT INTO to_do (id, created_date, modified_date, is_done, ready_state, title, writer_id, refering_to_dos_id) values (2, 201803051800, 201803051900, false, false, 'testToDo2', 1, 1);
--INSERT INTO to_do (id, created_date, modified_date, is_done, ready_state, title, writer_id, refering_to_dos_id) values (3, 201803061800, 201803061900, false, true, 'testToDo3', 1, 2);

INSERT INTO to_do (id, is_done, ready_state, title, refering_to_dos_id) values (1, false, false, 'testToDo1', null);
INSERT INTO to_do (id, is_done, ready_state, title, refering_to_dos_id) values (2, false, false, 'testToDo2', 1);
INSERT INTO to_do (id, is_done, ready_state, title, refering_to_dos_id) values (3, false, true, 'testToDo3', 2);