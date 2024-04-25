package com.org.business.model;

import com.littlecode.setup.SetupMetaObject;
import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "pg_catalog", name = "pg_tables")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SetupMetaObject(metaDataIgnore = true)
public class DbTable {
    @Id
    @Column(name = "schemaname")
    private String schemaName;
    @Column(name = "tablename")
    private String tableName;
}
