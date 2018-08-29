package org.fartpig.ddssrc2db.repository;

import org.fartpig.ddssrc2db.model.Tables;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TablesRepository extends JpaRepository<Tables, Long> {

	Tables findFirstByTableNameAndTableType(String tableName, String tableType);

	Page<Tables> findByTableNameStartingWithAndTableType(String tableName, String tableType, Pageable pageable);

}
