package org.fartpig.ddssrc2db.pojo.lf;

import java.util.List;

public class LFFileContent {

	private String fileName;
	private String fileType;
	private List<LFLine> lines;

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

	public List<LFLine> getLines() {
		return lines;
	}

	public void setLines(List<LFLine> lines) {
		this.lines = lines;
	}

}
