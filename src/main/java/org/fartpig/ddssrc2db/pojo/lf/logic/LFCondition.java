package org.fartpig.ddssrc2db.pojo.lf.logic;

import org.fartpig.ddssrc2db.pojo.IComment;

public class LFCondition implements IComment {

	private String comment;

	private String name;
	private String expr;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

}
