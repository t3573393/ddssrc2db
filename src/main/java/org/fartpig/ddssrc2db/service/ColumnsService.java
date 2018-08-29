package org.fartpig.ddssrc2db.service;

import java.util.List;

import org.fartpig.ddssrc2db.consts.ResolverConst;
import org.fartpig.ddssrc2db.model.Columns;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFField;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFFile;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFRecordFormat;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFField;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFFile;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFRecordFormat;
import org.fartpig.ddssrc2db.repository.ColumnsRepository;
import org.fartpig.ddssrc2db.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ColumnsService {

	private static Logger log = LoggerFactory.getLogger(ColumnsService.class);

	@Autowired
	private ColumnsRepository columnsRepository;

	public Page<Columns> findByTableNameAndTableTypeAndColumnName(String tableName, String tableType, String columnName,
			Pageable requestPage) {
		return columnsRepository.findByTableNameAndTableTypeAndColumnNameStartingWith(tableName, tableType, columnName,
				requestPage);
	}

	public Page<Columns> findByColumnName(String columnName, Pageable requestPage) {
		return columnsRepository.findByColumnNameStartingWith(columnName, requestPage);
	}

	public Page<Columns> findByTableNameAndTableType(String tableName, String tableType, Pageable requestPage) {
		return columnsRepository.findByTableNameStartingWithAndTableType(tableName, tableType, requestPage);
	}

	@Cacheable("columns")
	public List<Columns> findAllByTableNameAndTableType(String tableName, String tableType) {
		return columnsRepository.findByTableNameAndTableTypeOrderByOrdinalPositionAsc(tableName, tableType);
	}

	public Page<Columns> findAll(Pageable requestPage) {
		return columnsRepository.findAll(requestPage);
	}

	public long insertOrUpdateColumn(PFFile pfFile, PFRecordFormat recordFormat, PFField field, int index) {
		String tableName = pfFile.getName();
		String columnName = field.getName();
		String dataType = field.getDataType();

		log.debug("handle PF table:" + tableName + "- column:" + columnName);

		Columns dbColumns = columnsRepository.findFirstByTableNameAndTableTypeAndColumnNameOrderByIdAsc(tableName,
				ResolverConst.PF_FILE_TYPE, columnName);
		if (dbColumns == null) {
			dbColumns = Columns.getBuilder(tableName, ResolverConst.PF_FILE_TYPE, columnName, dataType).build();
			copyToColumn(field, dbColumns, index);
		} else {
			copyToColumn(field, dbColumns, index);
		}
		columnsRepository.save(dbColumns);
		return 1;
	}

	public long insertOrUpdateColumn(LFFile lfFile, LFRecordFormat recordFormat, LFField field, int index) {
		String tableName = lfFile.getName();
		String columnName = field.getName();
		String dataType = field.getDataType();

		log.debug("handle LF table:" + tableName + "- column:" + columnName);

		Columns dbColumns = columnsRepository.findFirstByTableNameAndTableTypeAndColumnNameOrderByIdAsc(tableName,
				ResolverConst.LF_FILE_TYPE, columnName);
		if (dbColumns == null) {
			dbColumns = Columns.getBuilder(tableName, ResolverConst.LF_FILE_TYPE, columnName, dataType).build();
			copyToColumn(field, dbColumns, index);
		} else {
			copyToColumn(field, dbColumns, index);
		}
		columnsRepository.save(dbColumns);
		return 1;
	}

	public void deleteColumns(PFFile pfFile, PFRecordFormat recordFormat, PFField field) {
		String tableName = pfFile.getName();
		String columnName = field.getName();

		Columns dbColumns = columnsRepository.findFirstByTableNameAndTableTypeAndColumnNameOrderByIdAsc(tableName,
				ResolverConst.PF_FILE_TYPE, columnName);
		if (dbColumns != null) {
			columnsRepository.delete(dbColumns);
		}
	}

	public void deleteColumns(LFFile lfFile, LFRecordFormat recordFormat, LFField field) {
		String tableName = lfFile.getName();
		String columnName = field.getName();

		Columns dbColumns = columnsRepository.findFirstByTableNameAndTableTypeAndColumnNameOrderByIdAsc(tableName,
				ResolverConst.LF_FILE_TYPE, columnName);
		if (dbColumns != null) {
			columnsRepository.delete(dbColumns);
		}
	}

	public void deleteAllColumns(PFFile lfFile, PFRecordFormat recordFormat) {
		String tableName = lfFile.getName();
		List<Columns> dbColumns = columnsRepository.findByTableNameAndTableType(tableName, ResolverConst.PF_FILE_TYPE);
		if (dbColumns != null) {
			columnsRepository.delete(dbColumns);
		}
	}

	public void deleteAllColumns(LFFile lfFile, LFRecordFormat recordFormat) {
		String tableName = lfFile.getName();
		List<Columns> dbColumns = columnsRepository.findByTableNameAndTableType(tableName, ResolverConst.LF_FILE_TYPE);
		if (dbColumns != null) {
			columnsRepository.delete(dbColumns);
		}
	}

	private void copyToColumn(PFField field, Columns dbColumns, int index) {
		String dataType = field.getDataType();

		dbColumns.setOrdinalPosition(index);

		if (StringUtils.isNullOrEmpty(field.getColHdg())) {
			dbColumns.setColHdg(field.getName());
		} else {
			dbColumns.setColHdg(field.getColHdg());
		}

		if (!StringUtils.isNullOrEmpty(field.getEdtWrd())) {
			dbColumns.setEdtWrd(field.getEdtWrd());
		}

		// 默认为字符型
		if (StringUtils.isNullOrEmpty(dataType)) {
			dataType = ResolverConst.DATA_TYPE_A;
			dbColumns.setDataType(dataType);
		}

		if (ResolverConst.DATA_TYPE_A.equals(dataType)) {
			// 字符
			dbColumns.setCharacterMaximumLength(Integer.valueOf(field.getLength()));
			if (!StringUtils.isNullOrEmpty(field.getVarLen())) {
				dbColumns.setCharacterVarLength(field.getVarLen());
			}
		} else if (!ResolverConst.DATA_TYPE_L.equals(dataType) && !ResolverConst.DATA_TYPE_T.equals(dataType)
				&& !ResolverConst.DATA_TYPE_Z.equals(dataType)) {
			// 数字
			dbColumns.setNumberScale(Integer.valueOf(field.getLength()));
			if (!StringUtils.isNullOrEmpty(field.getDecimalPosition())) {
				dbColumns.setNumberPrecision(Integer.valueOf(field.getDecimalPosition()));
			}
		} else {
			// 其他
			if (!StringUtils.isNullOrEmpty(field.getLength())) {
				dbColumns.setCharacterMaximumLength(Integer.valueOf(field.getLength()));
			}
		}
		dbColumns.setColumnComment(field.getComment());
		dbColumns.setUsage(field.getUse());
	}

	private void copyToColumn(LFField field, Columns dbColumns, int index) {
		String dataType = field.getDataType();

		dbColumns.setOrdinalPosition(index);

		if (StringUtils.isNullOrEmpty(field.getColHdg())) {
			dbColumns.setColHdg(field.getName());
		} else {
			dbColumns.setColHdg(field.getColHdg());
		}

		if (!StringUtils.isNullOrEmpty(field.getSst())) {
			dbColumns.setSst(field.getSst());
		}

		if (!StringUtils.isNullOrEmpty(field.getLength())) {
			dbColumns.setCharacterMaximumLength(Integer.valueOf(field.getLength()));
		}

		dbColumns.setColumnComment(field.getText());
		if (dbColumns.getColumnComment() != null) {
			if (field.getComment() != null) {
				dbColumns
						.setColumnComment(dbColumns.getColumnComment() + ResolverConst.LINE_BREAK + field.getComment());
			}
		} else {
			dbColumns.setColumnComment(field.getComment());
		}

		dbColumns.setUsage(field.getUse());
		dbColumns.setTableRef(field.getjRef());
		dbColumns.setColumnAlias(field.getRename());
	}

}
