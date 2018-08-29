package org.fartpig.ddssrc2db.pojo.pf.logic;

import java.util.ArrayList;
import java.util.List;

public class PFFile {

	private String name;

	private String ref;
	private String unique;
	// FIFO FCFO LIFO
	private String order;

	private List<PFRecordFormat> recordFormats = new ArrayList<PFRecordFormat>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public List<PFRecordFormat> getRecordFormats() {
		return recordFormats;
	}

	public void setRecordFormats(List<PFRecordFormat> recordFormats) {
		this.recordFormats = recordFormats;
	}

}
