package org.fartpig.ddssrc2db.service;

import java.util.List;

import org.fartpig.ddssrc2db.consts.ResolverConst;
import org.fartpig.ddssrc2db.model.TableIndex;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFFile;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFKey;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFRecordFormat;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFFile;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFKey;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFRecordFormat;
import org.fartpig.ddssrc2db.repository.TableIndexRepository;
import org.fartpig.ddssrc2db.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class TableIndexService {

	@Autowired
	private TableIndexRepository tableIndexRepository;

	@Cacheable("tableIndex")
	public List<TableIndex> findByTableNameAndTableType(String tableName, String tableType) {
		return tableIndexRepository.findByTableNameAndTableTypeOrderByIdAsc(tableName, tableType);
	}

	public long insertOrUpdateTableIndex(PFFile pfFile, PFRecordFormat recordFormat, PFKey key, int index) {
		String tableName = pfFile.getName();
		String defaultIndexName = tableName + ResolverConst.PF_DEFAULT_INDEX_SUFFIX;
		String keyName = key.getName();

		TableIndex dbTableIndex = tableIndexRepository.findOneByTableNameAndTableTypeAndKeyNameAndColumnName(tableName,
				ResolverConst.PF_FILE_TYPE, defaultIndexName, keyName);
		if (dbTableIndex == null) {
			dbTableIndex = TableIndex
					.getBuilder(tableName, ResolverConst.PF_FILE_TYPE, defaultIndexName, keyName, index).build();
			copyToTableIndex(pfFile, key, dbTableIndex, index);
		} else {
			copyToTableIndex(pfFile, key, dbTableIndex, index);
		}
		tableIndexRepository.save(dbTableIndex);
		return 1;
	}

	public long insertOrUpdateTableIndex(LFFile lfFile, LFRecordFormat recordFormat, LFKey key, int index) {
		String tableName = lfFile.getName();
		String defaultIndexName = tableName + ResolverConst.LF_DEFAULT_INDEX_SUFFIX;
		String keyName = key.getName();

		TableIndex dbTableIndex = tableIndexRepository.findOneByTableNameAndTableTypeAndKeyNameAndColumnName(tableName,
				ResolverConst.LF_FILE_TYPE, defaultIndexName, keyName);
		if (dbTableIndex == null) {
			dbTableIndex = TableIndex
					.getBuilder(tableName, ResolverConst.LF_FILE_TYPE, defaultIndexName, keyName, index).build();
			copyToTableIndex(lfFile, key, dbTableIndex, index);
		} else {
			copyToTableIndex(lfFile, key, dbTableIndex, index);
		}
		tableIndexRepository.save(dbTableIndex);
		return 1;
	}

	public void deleteTableIndex(PFFile pfFile, PFRecordFormat recordFormat, String indexName) {
		String tableName = pfFile.getName();
		String defaultIndexName = tableName + ResolverConst.PF_DEFAULT_INDEX_SUFFIX;
		if (indexName == null) {
			indexName = defaultIndexName;
		}

		List<TableIndex> dbTableIndexs = tableIndexRepository.findByTableNameAndTableTypeAndKeyName(tableName,
				ResolverConst.PF_FILE_TYPE, indexName);
		if (dbTableIndexs != null) {
			tableIndexRepository.delete(dbTableIndexs);
		}
	}

	public void deleteTableIndex(LFFile lfFile, LFRecordFormat recordFormat) {
		String tableName = lfFile.getName();

		List<TableIndex> dbTableIndexs = tableIndexRepository.findByTableNameAndTableType(tableName,
				ResolverConst.LF_FILE_TYPE);
		if (dbTableIndexs != null) {
			tableIndexRepository.delete(dbTableIndexs);
		}
	}

	private void copyToTableIndex(PFFile pfFile, PFKey key, TableIndex dbTableIndex, int seqInIndex) {
		dbTableIndex.setSeqInIndex(seqInIndex);
		if (!StringUtils.isNullOrEmpty(pfFile.getUnique())) {
			dbTableIndex.setNonUnique((short) 0);
		} else {
			dbTableIndex.setNonUnique((short) 1);
		}
		dbTableIndex.setColumnName(key.getName());

		if (!StringUtils.isNullOrEmpty(key.getDescEnd())) {
			dbTableIndex.setCollation(ResolverConst.INDEX_DESC);
		} else {
			dbTableIndex.setCollation(ResolverConst.INDEX_ABSC);
		}

		if (!StringUtils.isNullOrEmpty(key.getAbsVal())) {
			dbTableIndex.setAbsVal(ResolverConst.INDEX_ABSVAL);
		}

		dbTableIndex.setIndexComment(key.getComment());
	}

	private void copyToTableIndex(LFFile lfFile, LFKey key, TableIndex dbTableIndex, int seqInIndex) {
		dbTableIndex.setSeqInIndex(seqInIndex);
		if (!StringUtils.isNullOrEmpty(lfFile.getUnique())) {
			dbTableIndex.setNonUnique((short) 0);
		} else {
			dbTableIndex.setNonUnique((short) 1);
		}
		dbTableIndex.setColumnName(key.getName());

		if (!StringUtils.isNullOrEmpty(key.getDescEnd())) {
			dbTableIndex.setCollation(ResolverConst.INDEX_DESC);
		} else {
			dbTableIndex.setCollation(ResolverConst.INDEX_ABSC);
		}

		if (!StringUtils.isNullOrEmpty(key.getAbsVal())) {
			dbTableIndex.setAbsVal(ResolverConst.INDEX_ABSVAL);
		}

		dbTableIndex.setIndexComment(key.getComment());
	}

}
