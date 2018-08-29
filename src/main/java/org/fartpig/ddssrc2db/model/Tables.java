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
@Table(name = "t_tables")
public class Tables {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "record_format_name", nullable = false)
	private String recordFormatName;

	@Column(name = "table_name", nullable = false)
	private String tableName;

	@Column(name = "table_type", nullable = false)
	private String tableType;

	@Column(name = "version", nullable = false)
	private int version;

	@Column(name = "creation_time", nullable = false)
	private Date creationTime;

	@Column(name = "update_time", nullable = false)
	private Date updateTime;

	@Column(name = "table_comment", nullable = true, length = 5000, columnDefinition = "CLOB")
	private String tableComment;

	@Column(name = "ref_tables", nullable = true)
	private String refTables;

	@Column(name = "join_fields", nullable = true)
	private String joinFields;

	public Long getId() {
		return id;
	}

	public static Builder getBuilder(String recordFormatName, String tableName, String tableType) {
		return new Builder(recordFormatName, tableName, tableType);
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
		Tables built;

		Builder(String recordFormatName, String tableName, String tableType) {
			built = new Tables();
			built.recordFormatName = recordFormatName;
			built.tableName = tableName;
			built.tableType = tableType;
		}

		/**
		 * Builds the new Person object.
		 * 
		 * @return The created Person object.
		 */
		public Tables build() {
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

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public String getRecordFormatName() {
		return recordFormatName;
	}

	public void setRecordFormatName(String recordFormatName) {
		this.recordFormatName = recordFormatName;
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
