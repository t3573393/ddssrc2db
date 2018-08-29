package org.fartpig.ddssrc2db.pojo.lf.logic;

import org.fartpig.ddssrc2db.pojo.IComment;

public class LFField implements IComment, Cloneable {

	private String name;
	private String length;
	private String dataType;
	private String decimalPosition;
	private String use;

	private String colHdg;
	private String sst;
	private String jRef;
	private String comment;
	private String rename;
	private String text;

	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDecimalPosition() {
		return decimalPosition;
	}

	public void setDecimalPosition(String decimalPosition) {
		this.decimalPosition = decimalPosition;
	}

	public String getColHdg() {
		return colHdg;
	}

	public void setColHdg(String colHdg) {
		this.colHdg = colHdg;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSst() {
		return sst;
	}

	public void setSst(String sst) {
		this.sst = sst;
	}

	public String getjRef() {
		return jRef;
	}

	public void setjRef(String jRef) {
		this.jRef = jRef;
	}

	public String getRename() {
		return rename;
	}

	public void setRename(String rename) {
		this.rename = rename;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

}
