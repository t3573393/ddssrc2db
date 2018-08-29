package org.fartpig.ddssrc2db.util;

import org.fartpig.ddssrc2db.consts.ResolverConst;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class ResolverUtils {

	public static boolean isComment(String aCol, String fileType) {
		if (aCol == null) {
			return false;
		}

		aCol = aCol.trim();
		if (aCol.contains("A*") || aCol.startsWith("*") || aCol.contains("*")) {
			return true;
		}

		return false;
	}

	public static String getDefaultTableIndex(String tableName) {
		String defaultIndexName = tableName + ResolverConst.PF_DEFAULT_INDEX_SUFFIX;
		return defaultIndexName;
	}

	public static Pageable createPageable(int pageIndex, int pageSize, Sort sort) {
		Pageable pageSpecification = new PageRequest(pageIndex, pageSize, sort);
		return pageSpecification;
	}

	public static Sort createTablesSort() {
		Sort tablesOrder = new Sort(Direction.ASC, "tableName", "tableType");
		return tablesOrder;
	}

	public static Sort createTableColumnsSort() {
		Sort columnOrder = new Sort(Direction.ASC, "tableName", "tableType", "ordinalPosition");
		return columnOrder;
	}

}
