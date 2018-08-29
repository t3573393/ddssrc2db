package org.fartpig.ddssrc2db.service;

import java.util.ArrayList;
import java.util.List;

import org.fartpig.ddssrc2db.consts.ResolverConst;
import org.fartpig.ddssrc2db.model.ViewCondition;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFCondition;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFConditionTree;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFFile;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFRecordFormat;
import org.fartpig.ddssrc2db.repository.ViewConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ViewConditionService {

	@Autowired
	private ViewConditionRepository viewConditionRepository;

	@Cacheable("viewCondition")
	public List<ViewCondition> findByTableNameAndTableType(String tableName, String tableType) {
		return viewConditionRepository.findByTableNameAndTableTypeOrderByIdAsc(tableName, tableType);
	}

	public long insertViewCondition(LFFile lfFile, LFRecordFormat recordFormat) {
		LFConditionTree rootConditionTree = recordFormat.getConditionTree();
		if (rootConditionTree == null) {
			return -1;
		}

		String tableName = lfFile.getName();

		List<ViewCondition> dbViewConditions = viewConditionRepository.findByTableNameAndTableType(tableName,
				ResolverConst.LF_FILE_TYPE);
		if (dbViewConditions != null && dbViewConditions.size() > 0) {
			return -1;
		} else {
			dbViewConditions = new ArrayList<ViewCondition>();
			rootConditionTree = recordFormat.getConditionTree();
			ViewCondition rootCondition = ViewCondition.getBuilder(tableName, ResolverConst.LF_FILE_TYPE).build();
			copyToViewCondition(rootConditionTree, rootCondition);
			viewConditionRepository.save(rootCondition);

			copyToViewConditions(rootConditionTree, rootCondition, dbViewConditions, tableName,
					ResolverConst.LF_FILE_TYPE);
			viewConditionRepository.save(dbViewConditions);
		}
		return 1;
	}

	public void deleteViewConditions(LFFile lfFile, LFRecordFormat recordFormat) {
		String tableName = lfFile.getName();

		List<ViewCondition> dbViewConditions = viewConditionRepository.findByTableNameAndTableType(tableName,
				ResolverConst.LF_FILE_TYPE);
		viewConditionRepository.delete(dbViewConditions);
	}

	private void copyToViewCondition(LFCondition condition, ViewCondition leafCondition) {
		leafCondition.setKeyName(condition.getName());
		leafCondition.setExpr(condition.getExpr());
		leafCondition.setConditionComment(condition.getComment());
	}

	private void copyToViewCondition(LFConditionTree condition, ViewCondition leafCondition) {
		leafCondition.setConditionType(condition.getConditionType());
		leafCondition.setOperationType(condition.getOperationType());
		leafCondition.setConditionComment(condition.getComment());
	}

	private void copyToViewConditions(LFConditionTree parentConditionTree, ViewCondition parentCondition,
			List<ViewCondition> dbViewConditions, String tableName, String tableType) {

		if (parentConditionTree.getConditionChilds().size() == 0) {
			for (LFCondition condition : parentConditionTree.getLeafs()) {
				ViewCondition leafCondition = ViewCondition.getBuilder(tableName, tableType).build();
				copyToViewCondition(condition, leafCondition);
				leafCondition.setParentId(parentCondition.getId());

				viewConditionRepository.save(leafCondition);
				dbViewConditions.add(leafCondition);
			}
		} else {
			for (LFConditionTree conditionTree : parentConditionTree.getConditionChilds()) {
				ViewCondition condition = ViewCondition.getBuilder(tableName, tableType).build();
				copyToViewCondition(conditionTree, condition);
				condition.setParentId(parentCondition.getId());

				viewConditionRepository.save(condition);
				dbViewConditions.add(condition);

				copyToViewConditions(conditionTree, condition, dbViewConditions, tableName, tableType);
			}
		}
	}

}
