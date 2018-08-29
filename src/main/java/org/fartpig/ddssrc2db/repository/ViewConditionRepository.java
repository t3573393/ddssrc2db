package org.fartpig.ddssrc2db.repository;

import java.util.List;

import org.fartpig.ddssrc2db.model.ViewCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewConditionRepository extends JpaRepository<ViewCondition, Long> {

	List<ViewCondition> findByTableNameAndTableType(String tableName, String tableType);

	List<ViewCondition> findByTableNameAndTableTypeOrderByIdAsc(String tableName, String tableType);

}
