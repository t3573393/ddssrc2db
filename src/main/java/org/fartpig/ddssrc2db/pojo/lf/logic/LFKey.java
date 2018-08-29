package org.fartpig.ddssrc2db.pojo.lf.logic;

import org.fartpig.ddssrc2db.pojo.IComment;

public class LFKey implements IComment {

	private String name;
	private String descEnd;
	private String absVal;

	private String comment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescEnd() {
		return descEnd;
	}

	public void setDescEnd(String descEnd) {
		this.descEnd = descEnd;
	}

	public String getAbsVal() {
		return absVal;
	}

	public void setAbsVal(String absVal) {
		this.absVal = absVal;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
