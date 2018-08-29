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
@Table(name = "t_view_condition")
public class ViewCondition {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "table_name", nullable = false)
	private String tableName;

	@Column(name = "table_type", nullable = false)
	private String tableType;

	@Column(name = "condition_type", nullable = true)
	private String conditionType;

	@Column(name = "operation_type", nullable = true)
	private String operationType;

	@Column(name = "key_name", nullable = true)
	private String keyName;

	@Column(name = "expr", nullable = true)
	private String expr;

	@Column(name = "condition_comment", nullable = true)
	private String conditionComment;

	@Column(name = "parent_id", nullable = true)
	private Long parentId;

	@Column(name = "creation_time", nullable = false)
	private Date creationTime;

	@Column(name = "update_time", nullable = false)
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public static Builder getBuilder(String tableName, String tableType) {
		return new Builder(tableName, tableType);
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
		ViewCondition built;

		/**
		 * Creates a new Builder instance.
		 * 
		 * @param firstName
		 *            The first name of the created Person object.
		 * @param lastName
		 *            The last name of the created Person object.
		 */
		Builder(String tableName, String tableType) {
			built = new ViewCondition();
			built.tableName = tableName;
			built.tableType = tableType;
		}

		/**
		 * Builds the new Person object.
		 * 
		 * @return The created Person object.
		 */
		public ViewCondition build() {
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

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String conditionType) {
		this.conditionType = conditionType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getConditionComment() {
		return conditionComment;
	}

	public void setConditionComment(String conditionComment) {
		this.conditionComment = conditionComment;
	}

}
