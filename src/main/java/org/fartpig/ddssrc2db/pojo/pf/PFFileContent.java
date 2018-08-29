package org.fartpig.ddssrc2db.pojo.pf;

import java.util.List;

public class PFFileContent {

	private String fileName;
	private String fileType;
	private List<PFLine> lines;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public List<PFLine> getLines() {
		return lines;
	}

	public void setLines(List<PFLine> lines) {
		this.lines = lines;
	}

}
