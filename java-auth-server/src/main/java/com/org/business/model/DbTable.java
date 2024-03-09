package com.org.business.model;

import com.littlecode.setup.SetupMetaObject;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
