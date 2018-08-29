package org.fartpig.ddssrc2db.pojo.lf.logic;

import java.util.ArrayList;
import java.util.List;

import org.fartpig.ddssrc2db.pojo.IComment;

public class LFConditionTree implements IComment {

	private String comment;

	// select or ommit
	private String conditionType;

	// and or
	private String operationType;

	private List<LFConditionTree> conditionChilds = new ArrayList<LFConditionTree>();
	private List<LFCondition> leafs = new ArrayList<LFCondition>();

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public List<LFConditionTree> getConditionChilds() {
		return conditionChilds;
	}

	public void setConditionChilds(List<LFConditionTree> conditionChilds) {
		this.conditionChilds = conditionChilds;
	}

	public List<LFCondition> getLeafs() {
		return leafs;
	}

	public void setLeafs(List<LFCondition> leafs) {
		this.leafs = leafs;
	}

}
