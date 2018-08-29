package org.fartpig.ddssrc2db.dto;

public class TableDetailDTO {

	private Long id;
	private String recordFormatName;
	private String tableName;
	private String tableType;
	private String tableComment;
	private String refTables;
	private String joinFields;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecordFormatName() {
		return recordFormatName;
	}

	public void setRecordFormatName(String recordFormatName) {
		this.recordFormatName = recordFormatName;
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

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public String getRefTables() {
		return refTables;
	}

	public void setRefTables(String refTables) {
		this.refTables = refTables;
	}

	public String getJoinFields() {
		return joinFields;
	}

	public void setJoinFields(String joinFields) {
		this.joinFields = joinFields;
	}

}
