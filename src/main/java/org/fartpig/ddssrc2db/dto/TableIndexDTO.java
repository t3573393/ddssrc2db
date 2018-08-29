package org.fartpig.ddssrc2db.dto;

public class TableIndexDTO {

	private long id;

	private String tableName;

	private String tableType;

	private short nonUnique;

	private String keyName;

	private int seqInIndex;

	private String columnName;

	private String collation;

	private String absVal;

	private String indexComment;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public short getNonUnique() {
		return nonUnique;
	}

	public void setNonUnique(short nonUnique) {
		this.nonUnique = nonUnique;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public int getSeqInIndex() {
		return seqInIndex;
	}

	public void setSeqInIndex(int seqInIndex) {
		this.seqInIndex = seqInIndex;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getCollation() {
		return collation;
	}

	public void setCollation(String collation) {
		this.collation = collation;
	}

	public String getAbsVal() {
		return absVal;
	}

	public void setAbsVal(String absVal) {
		this.absVal = absVal;
	}

	public String getIndexComment() {
		return indexComment;
	}

	public void setIndexComment(String indexComment) {
		this.indexComment = indexComment;
	}

}
