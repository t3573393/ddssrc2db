package org.fartpig.ddssrc2db.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "t_columns")
public class Columns {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "table_name", nullable = false)
	private String tableName;

	@Column(name = "table_type", nullable = false)
	private String tableType;

	@Column(name = "column_name", nullable = false)
	private String columnName;

	@Column(name = "ordinal_position", nullable = false)
	private int ordinalPosition;

	@Column(name = "column_default", nullable = true)
	private String columnDefault;

	@Column(name = "col_hdg", nullable = false)
	private String colHdg;

	@Column(name = "sst", nullable = true)
	private String sst;

	@Column(name = "is_nullable", nullable = true)
	private String isNullable;

	@Column(name = "data_type", nullable = true)
	private String dataType;

	@Column(name = "character_maximum_length", nullable = true)
	private int characterMaximumLength;

	@Column(name = "edt_wrd", nullable = true)
	private String edtWrd;

	@Column(name = "character_var_length", nullable = true)
	private String characterVarLength;

	@Column(name = "number_precision", nullable = true)
	private int numberPrecision;

	@Column(name = "number_scale", nullable = true)
	private int numberScale;

	@Column(name = "creation_time", nullable = false)
	private Date creationTime;

	@Column(name = "update_time", nullable = false)
	private Date updateTime;

	@Column(name = "column_comment", nullable = true, length = 5000, columnDefinition = "CLOB")
	private String columnComment;

	@Column(name = "usage", nullable = true)
	private String usage;

	@Column(name = "table_ref", nullable = true)
	private String tableRef;

	@Column(name = "column_alias", nullable = true)
	private String columnAlias;

	public Long getId() {
		return id;
	}

	public static Builder getBuilder(String tableName, String tableType, String columnName, String dataType) {
		return new Builder(tableName, tableType, columnName, dataType);
	}

	public Date getCreationTime() {
		return creationTime;
	}

	@PreUpdate
	public void preUpdate() {
		updateTime = new Date();
	}

	@PrePersist
	public void prePersist() {
		Date now = new Date();
		creationTime = now;
		updateTime = now;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * A Builder class used to create new Person objects.
	 */
	public static class Builder {
		Columns built;

		/**
		 * Creates a new Builder instance.
		 * 
		 * @param firstName
		 *            The first name of the created Person object.
		 * @param lastName
		 *            The last name of the created Person object.
		 */
		Builder(String tableName, String tableType, String columnName, String dataType) {
			built = new Columns();
			built.tableName = tableName;
			built.tableType = tableType;
			built.columnName = columnName;
			built.dataType = dataType;
		}

		/**
		 * Builds the new Person object.
		 * 
		 * @return The created Person object.
		 */
		public Columns build() {
			return built;
		}
	}

	/**
	 * This setter method should only be used by unit tests.
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
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

	public String getColHdg() {
		return colHdg;
	}

	public void setColHdg(String colHdg) {
		this.colHdg = colHdg;
	}

	public String getCharacterVarLength() {
		return characterVarLength;
	}

	public void setCharacterVarLength(String characterVarLength) {
		this.characterVarLength = characterVarLength;
	}

	public String getEdtWrd() {
		return edtWrd;
	}

	public void setEdtWrd(String edtWrd) {
		this.edtWrd = edtWrd;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
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

	public String getSst() {
		return sst;
	}

	public void setSst(String sst) {
		this.sst = sst;
	}

}
