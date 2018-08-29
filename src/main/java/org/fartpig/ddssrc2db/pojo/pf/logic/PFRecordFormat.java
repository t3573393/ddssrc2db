package org.fartpig.ddssrc2db.pojo.pf.logic;

import java.util.ArrayList;
import java.util.List;

import org.fartpig.ddssrc2db.pojo.IComment;

public class PFRecordFormat implements IComment {

	private String name;
	private List<PFField> fields = new ArrayList<PFField>();
	private List<PFKey> keys = new ArrayList<PFKey>();
	private String comment;

	// 共享其他文件记录格式
	private String format;

	// 说明
	private String text;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PFField> getFields() {
		return fields;
	}

	public void setFields(List<PFField> fields) {
		this.fields = fields;
	}

	public List<PFKey> getKeys() {
		return keys;
	}

	public void setKeys(List<PFKey> keys) {
		this.keys = keys;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
