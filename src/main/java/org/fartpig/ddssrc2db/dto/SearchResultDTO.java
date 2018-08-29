package org.fartpig.ddssrc2db.dto;

public class SearchResultDTO {

	private long id;

	private String recordFormatName;
	private String tableName;
	private String tableType;
	private String tableComment;
	private String refTables;
	private String joinFields;

	private Long tableId;

	private String columnName;

	private int ordinalPosition;

	private String columnDefault;

	private String colHdg;

	private String sst;

	private String dataType;

	private int characterMaximumLength;

	private String edtWrd;

	private String characterVarLength;

	private int numberPrecision;

	private int numberScale;

	private String columnComment;

	private String usage;

	private String tableRef;

	private String columnAlias;

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

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getOrdinalPosition() {
		return ordinalPosition;
	}

	public void setOrdinalPosition(int ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}

	public String getColumnDefault() {
		return columnDefault;
	}

	public void setColumnDefault(String columnDefault) {
		this.columnDefault = columnDefault;
	}

	public String getColHdg() {
		return colHdg;
	}

	public void setColHdg(String colHdg) {
		this.colHdg = colHdg;
	}

	public String getSst() {
		return sst;
	}

	public void setSst(String sst) {
		this.sst = sst;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getCharacterMaximumLength() {
		return characterMaximumLength;
	}

	public void setCharacterMaximumLength(int characterMaximumLength) {
		this.characterMaximumLength = characterMaximumLength;
	}

	public String getEdtWrd() {
		return edtWrd;
	}

	public void setEdtWrd(String edtWrd) {
		this.edtWrd = edtWrd;
	}

	public String getCharacterVarLength() {
		return characterVarLength;
	}

	public void setCharacterVarLength(String characterVarLength) {
		this.characterVarLength = characterVarLength;
	}

	public int getNumberPrecision() {
		return numberPrecision;
	}

	public void setNumberPrecision(int numberPrecision) {
		this.numberPrecision = numberPrecision;
	}

	public int getNumberScale() {
		return numberScale;
	}

	public void setNumberScale(int numberScale) {
		this.numberScale = numberScale;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getTableRef() {
		return tableRef;
	}

	public void setTableRef(String tableRef) {
		this.tableRef = tableRef;
	}

	public String getColumnAlias() {
		return columnAlias;
	}

	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getTableId() {
		return tableId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

}
