package org.fartpig.ddssrc2db.pojo.lf.logic;

import java.util.ArrayList;
import java.util.List;

public class LFFile {

	private String name;

	private String unique;
	private List<LFRecordFormat> recordFormats = new ArrayList<LFRecordFormat>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
	}

	public List<LFRecordFormat> getRecordFormats() {
		return recordFormats;
	}

	public void setRecordFormats(List<LFRecordFormat> recordFormats) {
		this.recordFormats = recordFormats;
	}

}
