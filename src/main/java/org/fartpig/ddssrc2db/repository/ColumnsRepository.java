package org.fartpig.ddssrc2db.repository;

import java.util.List;

import org.fartpig.ddssrc2db.model.Columns;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnsRepository extends JpaRepository<Columns, Long> {

	Columns findFirstByTableNameAndTableTypeAndColumnNameOrderByIdAsc(String tableName, String tableType,
			String columnName);

	List<Columns> findByTableNameAndTableTypeOrderByOrdinalPositionAsc(String tableName, String tableType);

	List<Columns> findByTableNameAndTableType(String tableName, String tableType);

	Page<Columns> findByTableNameAndTableTypeAndColumnNameStartingWith(String tableName, String tableType,
			String columnName, Pageable requestPage);

	Page<Columns> findByColumnNameStartingWith(String columnName, Pageable requestPage);

	Page<Columns> findByTableNameStartingWithAndTableType(String tableName, String tableType, Pageable requestPage);

}
