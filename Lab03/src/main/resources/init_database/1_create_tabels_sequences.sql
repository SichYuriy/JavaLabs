DROP SEQUENCE entity_id_seq;
DROP SEQUENCE entity_type_id_seq;
DROP SEQUENCE attribute_id_seq;
DROP SEQUENCE attr_type_id_seq;
-------------------------------

DROP TABLE value;
DROP TABLE refs;
DROP TABLE attr_binds;
DROP TABLE attribute;
DROP TABLE attr_type;
DROP TABLE entity;
DROP TABLE entity_type;


CREATE TABLE entity_type(
  entity_type_id  NUMBER(20, 0) NOT NULL,
  parent_id       NUMBER(20, 0) NULL,
  name            VARCHAR2(100) NULL UNIQUE,
  description     VARCHAR2(1000) NULL
);

ALTER TABLE entity_type
ADD (PRIMARY KEY(entity_type_id));
  
CREATE TABLE entity(
  entity_id       NUMBER(20, 0) NOT NULL,
  entity_type_id  NUMBER(20, 0) NOT NULL
);

ALTER TABLE entity
ADD (PRIMARY KEY(entity_id));

CREATE TABLE attr_type(
  attr_type_id    NUMBER(20, 0) NOT NULL,
  name            varchar(50) NULL UNIQUE
);

ALTER TABLE attr_type
ADD (PRIMARY KEY(attr_type_id));

CREATE TABLE attribute(
  attribute_id  NUMBER(20, 0) NOT NULL,
  attr_type_id  NUMBER(20, 0) NOT NULL,
  name          VARCHAR2(100) NULL UNIQUE,
  ismultiple    NUMBER(1, 0)
);

ALTER TABLE attribute
ADD (PRIMARY KEY(attribute_id));

CREATE TABLE attr_binds(
  attribute_id    NUMBER(20, 0) NOT NULL,
  entity_type_id  NUMBER(20, 0) NOT NULL
);

CREATE INDEX attr_binds_et_index 
ON attr_binds(entity_type_id ASC);

ALTER TABLE attr_binds
ADD (PRIMARY KEY(attribute_id, entity_type_id));

CREATE TABLE value(
  entity_id     NUMBER(20, 0) NOT NULL,
  attribute_id  NUMBER(20, 0) NOT NULL,
  text_value    VARCHAR2(1000) NULL,
  number_value  NUMBER(20, 0) NULL,
  date_value    DATE NULL
);

CREATE INDEX value_entity_index
ON value(entity_id ASC);

ALTER TABLE value
ADD (PRIMARY KEY(entity_id, attribute_id));

CREATE TABLE refs(
  owner_id    NUMBER(20, 0) NOT NULL,
  entity_id   NUMBER(20, 0) NOT NULL,
  attribute_id     NUMBER(20, 0) NOT NULL
);

ALTER TABLE refs
ADD (PRIMARY KEY(owner_id, entity_id, attribute_id));

ALTER TABLE entity_type
ADD (
      FOREIGN KEY(parent_id)
      REFERENCES entity_type
);

ALTER TABLE entity
ADD (
      FOREIGN KEY(entity_type_id)
      REFERENCES entity_type
);

ALTER TABLE attribute
ADD (
      FOREIGN KEY(attr_type_id)
      REFERENCES attr_type
);

ALTER TABLE attr_binds
ADD (
      FOREIGN KEY(entity_type_id)
      REFERENCES entity_type
);

ALTER TABLE attr_binds
ADD (
      FOREIGN KEY(attribute_id)
      REFERENCES attribute
);

ALTER TABLE value
ADD (
      FOREIGN KEY(entity_id)
      REFERENCES entity
);

ALTER TABLE value
ADD (
      FOREIGN KEY(attribute_id)
      REFERENCES attribute
);

ALTER TABLE refs
ADD (
      FOREIGN KEY(attribute_id)
      REFERENCES attribute
);

ALTER TABLE refs
ADD (
      FOREIGN KEY(owner_id)
      REFERENCES entity
);

ALTER TABLE refs
ADD (
      FOREIGN KEY(entity_id)
      REFERENCES entity
);

CREATE SEQUENCE entity_id_seq START WITH 1 NOCACHE ORDER;
CREATE SEQUENCE entity_type_id_seq START WITH 1 NOCACHE ORDER;
CREATE SEQUENCE attribute_id_seq START WITH 1 NOCACHE ORDER;
CREATE SEQUENCE attr_type_id_seq START WITH 1 NOCACHE ORDER;