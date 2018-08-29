package org.fartpig.ddssrc2db.pojo.pf.logic;

import java.util.ArrayList;
import java.util.List;

import org.fartpig.ddssrc2db.consts.ResolverConst;

public class PFComment {

	private List<String> comments = new ArrayList<String>();

	// 是否包含上面带下来的注释
	private boolean withLastComment = false;

	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	public void append(String comment) {
		comments.add(comment);
	}

	public void reset() {
		comments.clear();
		withLastComment = false;
	}

	public int size() {
		return comments.size();
	}

	public String getComment(int index) {
		return comments.get(index);
	}

	public String getAll() {
		StringBuilder sb = new StringBuilder();
		for (String aComment : comments) {
			sb.append(aComment + ResolverConst.LINE_BREAK);
		}
		return sb.toString();
	}

	public boolean isWithLastComment() {
		return withLastComment;
	}

	public void setWithLastComment(boolean withLastComment) {
		this.withLastComment = withLastComment;
	}

}
