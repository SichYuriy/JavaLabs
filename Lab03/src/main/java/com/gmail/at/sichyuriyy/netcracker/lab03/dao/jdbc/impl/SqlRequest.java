package com.gmail.at.sichyuriyy.netcracker.lab03.dao.jdbc.impl;

/**
 * Created by Yuriy on 3/19/2017.
 */
public interface SqlRequest {

    String INSERT_ENTITY = "INSERT INTO entity(entity_type_id) VALUES(?)";

    String INSERT_TEXT_VALUE = "INSERT INTO value(entity_id, attribute_id, text_value) VALUES(?, ?, ?)";
    String INSERT_NUMBER_VALUE = "INSERT INTO value(entity_id, attribute_id, number_value) VALUES(?, ?, ?)";
    String INSERT_DATE_VALUE = "INSERT INTO value(entity_id, attribute_id, date_value) VALUES(?, ?, ?)";

    String UPDATE_TEXT_VALUE = "UPDATE value SET text_value=? WHERE entity_id=? AND attribute_id=?";
    String UPDATE_NUMBER_VALUE = "UPDATE value SET number_value=? WHERE entity_id=? AND attribute_id=?";
    String UPDATE_DATE_VALUE = "UPDATE value SET date_value=? WHERE entity_id=? AND attribute_id=?";

    String INSERT_REF = "INSERT INTO refs(owner_id, attribute_id, entity_id) VALUES(?, ?, ?)";
    String DELETE_REF = "DELETE FROM refs WHERE owner_id=? AND attribute_id=? AND entity_id=?";

    String UPDATE_REF = "UPDATE refs SET entity_id=? WHERE owner_id=? AND attribute_id=?";

    String DELETE_ALL_REFS_BY_OWNER = "DELETE FROM refs WHERE owner_id=?";
    String DELETE_ALL_REFS_BY_OWNER_ATTRIBUTE = "DELETE FROM refs WHERE owner_id=? AND attribute_id=?";
    String DELETE_ALL_VALUES_BY_ENTITY_ID = "DELETE FROM value WHERE entity_id=?";

    String DELETE_ENTITY = "DELETE FROM entity WHERE entity_id=?";

    String SELECT_BEGIN =
            "SELECT " +
                "entity.entity_id id, " +
                "attribute.name attr_name, " +
                "value.text_value text_value, " +
                "value.number_value as number_value, " +
                "value.date_value as date_value, " +
                "refs.entity_id as ref_value ";

    String SELECT_END =
            "INNER JOIN attr_binds ON (entity_type.entity_type_id=attr_binds.entity_type_id) " +
            "INNER JOIN attribute ON (attribute.attribute_id=attr_binds.attribute_id AND attribute.ismultiple=0) " +
            "LEFT JOIN value ON (value.attribute_id=attribute.attribute_id AND value.entity_id=entity.entity_id) " +
            "LEFT JOIN refs ON (refs.owner_id=entity.entity_id AND attribute.attribute_id=refs.attribute_id)";


    String SELECT_BY_ID = SELECT_BEGIN +
            "FROM entity " +
            "INNER JOIN entity_type " +
                "ON (entity.entity_id=? AND entity_type.entity_type_id=? AND entity.entity_type_id=entity_type.entity_type_id) " +
            SELECT_END;

    String SELECT_ALL = SELECT_BEGIN +
            "FROM entity " +
            "INNER JOIN entity_type " +
                "ON (entity_type.entity_type_id=? AND entity.entity_type_id=entity_type.entity_type_id) " +
            SELECT_END;

    String SELECT_BY_REF_ID = SELECT_BEGIN +
            "FROM refs filter_refs " +
            "INNER JOIN entity ref_entity ON (filter_refs.entity_id=ref_entity.entity_id AND ref_entity.entity_type_id=?) " +
            "INNER JOIN entity ON (filter_refs.ENTITY_ID=? AND filter_refs.OWNER_ID=entity.entity_id AND filter_refs.attribute_id=?) " +
            "INNER JOIN entity_type ON (entity_type.entity_type_id=? AND entity_type.entity_type_id=entity.entity_type_id) " +
            SELECT_END;

    String SELECT_BY_OWNER_REF_ID = SELECT_BEGIN +
            "FROM refs filter_refs " +
            "INNER JOIN entity ref_entity ON (filter_refs.owner_id=ref_entity.entity_id AND ref_entity.entity_type_id=?) " +
            "INNER JOIN entity ON (filter_refs.owner_id=? AND filter_refs.entity_id=entity.entity_id AND filter_refs.attribute_id=?) " +
            "INNER JOIN entity_type ON (entity_type.entity_type_id=? AND entity_type.entity_type_id=entity.entity_type_id) " +
            SELECT_END;
}
