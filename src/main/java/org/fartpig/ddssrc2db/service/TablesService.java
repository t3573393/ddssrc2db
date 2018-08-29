package org.fartpig.ddssrc2db.service;

import org.fartpig.ddssrc2db.consts.ResolverConst;
import org.fartpig.ddssrc2db.model.Tables;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFFile;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFJoin;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFRecordFormat;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFFile;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFRecordFormat;
import org.fartpig.ddssrc2db.repository.TablesRepository;
import org.fartpig.ddssrc2db.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TablesService {

	@Autowired
	private TablesRepository tablesRepository;

	public Page<Tables> findByTableNameAndTableType(String tableName, String tableType, Pageable pageRequest) {
		return tablesRepository.findByTableNameStartingWithAndTableType(tableName, tableType, pageRequest);
	}

	@Cacheable("tables")
	public Tables findFirstByTableNameAndTableType(String tableName, String tableType) {
		return tablesRepository.findFirstByTableNameAndTableType(tableName, tableType);
	}

	public Page<Tables> findAll(Pageable pageRequest) {
		return tablesRepository.findAll(pageRequest);
	}

	@Cacheable("tables")
	public Tables findOne(long id) {
		return tablesRepository.findOne(id);
	}

	public long insertOrUpdateTable(PFFile pfFile, PFRecordFormat recordFormat) {
		String tableName = pfFile.getName();

		Tables dbTables = tablesRepository.findFirstByTableNameAndTableType(tableName, ResolverConst.PF_FILE_TYPE);
		if (dbTables == null) {
			dbTables = Tables.getBuilder(recordFormat.getName(), tableName, ResolverConst.PF_FILE_TYPE).build();
			dbTables.setVersion(1);
			copyToTables(recordFormat, dbTables);
		} else {
			copyToTables(recordFormat, dbTables);
		}
		tablesRepository.save(dbTables);
		return 1;
	}

	public long insertOrUpdateTable(LFFile lfFile, LFRecordFormat recordFormat) {
		String tableName = lfFile.getName();

		Tables dbTables = tablesRepository.findFirstByTableNameAndTableType(tableName, ResolverConst.LF_FILE_TYPE);
		if (dbTables == null) {
			dbTables = Tables.getBuilder(recordFormat.getName(), tableName, ResolverConst.LF_FILE_TYPE).build();
			dbTables.setVersion(1);
			copyToTables(lfFile, recordFormat, dbTables);
		} else {
			copyToTables(lfFile, recordFormat, dbTables);
		}
		tablesRepository.save(dbTables);
		return 1;
	}

	public void deleteTables(PFFile pfFile, PFRecordFormat recordFormat) {
		String tableName = pfFile.getName();
		Tables dbTables = tablesRepository.findFirstByTableNameAndTableType(tableName, ResolverConst.PF_FILE_TYPE);
		if (dbTables != null) {
			tablesRepository.delete(dbTables);
		}
	}

	public void deleteTables(LFFile lfFile, LFRecordFormat recordFormat) {
		String tableName = lfFile.getName();
		Tables dbTables = tablesRepository.findFirstByTableNameAndTableType(tableName, ResolverConst.LF_FILE_TYPE);
		if (dbTables != null) {
			tablesRepository.delete(dbTables);
		}
	}

	private void copyToTables(PFRecordFormat recordFormat, Tables dbTables) {
		dbTables.setTableComment(recordFormat.getText());

		if (StringUtils.isNullOrEmpty(dbTables.getTableComment())) {
			dbTables.setTableComment(recordFormat.getComment());
		} else {
			if (recordFormat.getComment() != null) {
				dbTables.setTableComment(
						dbTables.getTableComment() + ResolverConst.LINE_BREAK + recordFormat.getComment());
			}
		}
	}

	private void copyToTables(LFFile pfFile, LFRecordFormat recordFormat, Tables dbTables) {
		dbTables.setTableComment(recordFormat.getText());

		if (StringUtils.isNullOrEmpty(dbTables.getTableComment())) {
			dbTables.setTableComment(recordFormat.getComment());
		} else {
			if (recordFormat.getComment() != null) {
				dbTables.setTableComment(
						dbTables.getTableComment() + ResolverConst.LINE_BREAK + recordFormat.getComment());
			}
		}
		String joinFields = null;
		String refTables = null;

		if (!StringUtils.isNullOrEmpty(recordFormat.getpFile())) {
			refTables = recordFormat.getpFile();
		} else {
			if (!StringUtils.isNullOrEmpty(recordFormat.getjFile())) {
				refTables = recordFormat.getjFile();

				joinFields = "";
				for (LFJoin aJoin : recordFormat.getJoins()) {
					if (!StringUtils.isNullOrEmpty(aJoin.getjFld())) {
						joinFields += aJoin.getjFld() + "|";
					}
				}
			}
		}

		dbTables.setJoinFields(joinFields);
		dbTables.setRefTables(refTables);
	}

}
