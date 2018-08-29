package org.fartpig.ddssrc2db.pojo.lf.logic;

import org.fartpig.ddssrc2db.pojo.IComment;

public class LFJoin implements IComment {

	private String join;
	private String jFld;

	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	public String getjFld() {
		return jFld;
	}

	public void setjFld(String jFld) {
		this.jFld = jFld;
	}

}
