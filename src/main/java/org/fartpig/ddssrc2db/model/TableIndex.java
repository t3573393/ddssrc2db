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
@Table(name = "t_table_index")
public class TableIndex {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "table_name", nullable = false)
	private String tableName;

	@Column(name = "table_type", nullable = false)
	private String tableType;

	@Column(name = "non_unique", nullable = true)
	private short nonUnique;

	@Column(name = "key_name", nullable = false)
	private String keyName;

	@Column(name = "seq_in_index", nullable = false)
	private int seqInIndex;

	@Column(name = "column_name", nullable = false)
	private String columnName;

	// 排序方式
	@Column(name = "collation", nullable = true)
	private String collation;

	@Column(name = "abs_val", nullable = true)
	private String absVal;

	@Column(name = "creation_time", nullable = false)
	private Date creationTime;

	@Column(name = "update_time", nullable = false)
	private Date updateTime;

	@Column(name = "index_comment", nullable = true, length = 5000, columnDefinition = "CLOB")
	private String indexComment;

	public Long getId() {
		return id;
	}

	/**
	 * Gets a builder which is used to create Person objects.
	 * 
	 * @param firstName
	 *            The first name of the created user.
	 * @param lastName
	 *            The last name of the created user.
	 * @return A new Builder instance.
	 */
	public static Builder getBuilder(String tableName, String tableType, String keyName, String columnName,
			int seqInIndex) {
		return new Builder(tableName, tableType, keyName, columnName, seqInIndex);
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
		TableIndex built;

		/**
		 * Creates a new Builder instance.
		 * 
		 * @param firstName
		 *            The first name of the created Person object.
		 * @param lastName
		 *            The last name of the created Person object.
		 */
		Builder(String tableName, String tableType, String keyName, String columnName, int seqInIndex) {
			built = new TableIndex();
			built.tableName = tableName;
			built.tableType = tableType;
			built.keyName = keyName;
			built.columnName = columnName;
			built.seqInIndex = seqInIndex;
		}

		/**
		 * Builds the new Person object.
		 * 
		 * @return The created Person object.
		 */
		public TableIndex build() {
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

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

}
