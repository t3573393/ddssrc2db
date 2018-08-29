package org.fartpig.ddssrc2db.repository;

import java.util.List;

import org.fartpig.ddssrc2db.model.TableIndex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableIndexRepository extends JpaRepository<TableIndex, Long> {

	List<TableIndex> findByTableNameAndTableTypeAndKeyName(String tableName, String tableType, String keyName);

	List<TableIndex> findByTableNameAndTableType(String tableName, String tableType);

	List<TableIndex> findByTableNameAndTableTypeOrderByIdAsc(String tableName, String tableType);

	TableIndex findOneByTableNameAndTableTypeAndKeyNameAndColumnName(String tableName, String tableType, String keyName,
			String columnName);

}
