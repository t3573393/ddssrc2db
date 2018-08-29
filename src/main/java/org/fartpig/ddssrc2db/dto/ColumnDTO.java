package org.fartpig.ddssrc2db.dto;

import java.util.Date;

public class ColumnDTO {

	private long id;

	private String tableName;

	private String tableType;

	private String columnName;

	private int ordinalPosition;

	private String columnDefault;

	private String colHdg;

	private String sst;

	private String isNullable;

	private String dataType;

	private int characterMaximumLength;

	private String edtWrd;

	private String characterVarLength;

	private int numberPrecision;

	private int numberScale;

	private Date creationTime;

	private Date updateTime;

	private String columnComment;

	private String usage;

	private String tableRef;

	private String columnAlias;

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

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
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

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

}
