package com.org.business.repository;

import com.org.business.model.DbTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbTableRepository extends JpaRepository<DbTable, String> {

    boolean existsBySchemaNameAndTableName(String schemaName, String tableName);

}
