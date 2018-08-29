package org.fartpig.ddssrc2db.pojo.lf.logic;

import java.util.ArrayList;
import java.util.List;

import org.fartpig.ddssrc2db.pojo.IComment;

public class LFRecordFormat implements IComment {

	private String name;
	private List<LFField> fields = new ArrayList<LFField>();
	private List<LFKey> keys = new ArrayList<LFKey>();
	private List<LFJoin> joins = new ArrayList<LFJoin>();
	private LFConditionTree conditionTree;

	private String comment;

	// 共享其他文件记录格式
	private String format;

	// 说明
	private String text;

	private String jFile;
	private String pFile;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<LFField> getFields() {
		return fields;
	}

	public void setFields(List<LFField> fields) {
		this.fields = fields;
	}

	public List<LFKey> getKeys() {
		return keys;
	}

	public void setKeys(List<LFKey> keys) {
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

	public String getjFile() {
		return jFile;
	}

	public void setjFile(String jFile) {
		this.jFile = jFile;
	}

	public String getpFile() {
		return pFile;
	}

	public void setpFile(String pFile) {
		this.pFile = pFile;
	}

	public List<LFJoin> getJoins() {
		return joins;
	}

	public void setJoins(List<LFJoin> joins) {
		this.joins = joins;
	}

	public LFConditionTree getConditionTree() {
		return conditionTree;
	}

	public void setConditionTree(LFConditionTree conditionTree) {
		this.conditionTree = conditionTree;
	}

}
