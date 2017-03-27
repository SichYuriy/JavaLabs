DELETE FROM refs;
DELETE FROM value;
DELETE FROM entity;

DROP SEQUENCE entity_id_seq;
DROP SEQUENCE entity_type_id_seq;
DROP SEQUENCE attribute_id_seq;
DROP SEQUENCE attr_type_id_seq;

CREATE SEQUENCE entity_id_seq START WITH 1 NOCACHE ORDER;
CREATE SEQUENCE entity_type_id_seq START WITH 1 NOCACHE ORDER;
CREATE SEQUENCE attribute_id_seq START WITH 1 NOCACHE ORDER;
CREATE SEQUENCE attr_type_id_seq START WITH 1 NOCACHE ORDER;

INSERT INTO entity(entity_type_id) VALUES(2);
INSERT INTO entity(entity_type_id) VALUES(2);
INSERT INTO entity(entity_type_id) VALUES(2);
INSERT INTO entity(entity_type_id) VALUES(2);

INSERT INTO value(entity_id, attribute_id, text_value) VALUES(1, 8, 'ADMINISTRATOR');
INSERT INTO value(entity_id, attribute_id, text_value) VALUES(2, 8, 'PROJECT_MANAGER');
INSERT INTO value(entity_id, attribute_id, text_value) VALUES(3, 8, 'EMPLOYEE');
INSERT INTO value(entity_id, attribute_id, text_value) VALUES(4, 8, 'CUSTOMER');
