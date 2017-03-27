ALTER SESSION SET PLSCOPE_SETTINGS = 'IDENTIFIERS:NONE';

CREATE OR REPLACE TRIGGER entity_trg
BEFORE INSERT ON entity 
FOR EACH ROW
BEGIN
  SELECT entity_id_seq.NEXTVAL
  INTO   :new.entity_id
  FROM   dual;
END;
/

CREATE OR REPLACE TRIGGER entity_type_trg
BEFORE INSERT ON entity_type 
FOR EACH ROW
BEGIN
  SELECT entity_type_id_seq.NEXTVAL
  INTO   :new.entity_type_id
  FROM   dual;
END;
/

CREATE OR REPLACE TRIGGER attribute_type_trg
BEFORE INSERT ON attr_type 
FOR EACH ROW
BEGIN
  SELECT attr_type_id_seq.NEXTVAL
  INTO   :new.attr_type_id
  FROM   dual;
END;
/

CREATE OR REPLACE TRIGGER attribute_trg
BEFORE INSERT ON attribute 
FOR EACH ROW
BEGIN
  SELECT attribute_id_seq.NEXTVAL
  INTO   :new.attribute_id
  FROM   dual;
END;
/

commit;