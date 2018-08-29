package org.fartpig.ddssrc2db.pojo.pf;

public class PFLine {

	private String lineNum;
	private String rawLine;

	private String aCol;
	private String tCol;
	private String nameCol;
	private String rCol;
	private String lenCol;
	private String tDataTypeCol;
	private String dpCol;
	private String usageCol;
	private String functionsCol;

	public String toString() {
		return "lineNum :" + lineNum + "\r\n" + "rawLine :" + rawLine + "\r\n" + "aCol :" + aCol + "\r\n" + "tCol :"
				+ tCol + "\r\n" + "nameCol :" + nameCol + "\r\n" + "rCol :" + rCol + "\r\n" + "lenCol :" + lenCol
				+ "\r\n" + "tDataTypeCol :" + tDataTypeCol + "\r\n" + "dpCol :" + dpCol + "\r\n" + "usageCol :"
				+ usageCol + "\r\n" + "functionsCol :" + functionsCol + "\r\n";
	}

	public String getRawLine() {
		return rawLine;
	}

	public void setRawLine(String rawLine) {
		this.rawLine = rawLine;
	}

	public String getLineNum() {
		return lineNum;
	}

	public void setLineNum(String lineNum) {
		this.lineNum = lineNum;
	}

	public String getaCol() {
		return aCol;
	}

	public void setaCol(String aCol) {
		this.aCol = aCol;
	}

	public String gettCol() {
		return tCol;
	}

	public void settCol(String tCol) {
		this.tCol = tCol;
	}

	public String getNameCol() {
		return nameCol;
	}

	public void setNameCol(String nameCol) {
		this.nameCol = nameCol;
	}

	public String getrCol() {
		return rCol;
	}

	public void setrCol(String rCol) {
		this.rCol = rCol;
	}

	public String getLenCol() {
		return lenCol;
	}

	public void setLenCol(String lenCol) {
		this.lenCol = lenCol;
	}

	public String gettDataTypeCol() {
		return tDataTypeCol;
	}

	public void settDataTypeCol(String tDataTypeCol) {
		this.tDataTypeCol = tDataTypeCol;
	}

	public String getDpCol() {
		return dpCol;
	}

	public void setDpCol(String dpCol) {
		this.dpCol = dpCol;
	}

	public String getFunctionsCol() {
		return functionsCol;
	}

	public void setFunctionsCol(String functionsCol) {
		this.functionsCol = functionsCol;
	}

	public String getUsageCol() {
		return usageCol;
	}

	public void setUsageCol(String usageCol) {
		this.usageCol = usageCol;
	}

}
