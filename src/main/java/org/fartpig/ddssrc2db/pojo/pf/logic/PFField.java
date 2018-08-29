package org.fartpig.ddssrc2db.pojo.pf.logic;

import org.fartpig.ddssrc2db.pojo.IComment;

public class PFField implements IComment, Cloneable {

	private String name;
	private String length;
	private String dataType;
	private String decimalPosition;
	private String use;

	private String colHdg;
	private String refFld;
	private String varLen;
	private String comment;
	private String edtWrd;

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

	public String getRefFld() {
		return refFld;
	}

	public void setRefFld(String refFld) {
		this.refFld = refFld;
	}

	public String getVarLen() {
		return varLen;
	}

	public void setVarLen(String varLen) {
		this.varLen = varLen;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEdtWrd() {
		return edtWrd;
	}

	public void setEdtWrd(String edtWrd) {
		this.edtWrd = edtWrd;
	}

	public String getUse() {
		return use;
	}

	public void setUse(String use) {
		this.use = use;
	}

}
