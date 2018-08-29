package org.fartpig.ddssrc2db.util;

import org.fartpig.ddssrc2db.consts.ResolverConst;
import org.fartpig.ddssrc2db.dto.ColumnDTO;
import org.fartpig.ddssrc2db.dto.SearchResultDTO;

public final class ViewUtils {

	public static String renderComment(String comment) {
		if (comment == null) {
			return "";
		}
		return comment.replaceAll("\r\n", "<br/>");
	}

	private static String renderDataType(String dataType, int characterMaximumLength, int numberScale,
			int numberPrecision) {
		StringBuilder sb = new StringBuilder();
		if (dataType != null) {
			if (ResolverConst.DATA_TYPE_A.equals(dataType)) {
				// 字符
				sb.append(dataType);
				sb.append("(");
				sb.append(characterMaximumLength);
				sb.append(")");
			} else if (!ResolverConst.DATA_TYPE_L.equals(dataType) && !ResolverConst.DATA_TYPE_T.equals(dataType)
					&& !ResolverConst.DATA_TYPE_Z.equals(dataType)) {
				// 数字
				sb.append(dataType);
				sb.append("(");
				sb.append(numberScale);
				if (numberPrecision != 0) {
					sb.append(",");
					sb.append(numberPrecision);
				}
				sb.append(")");
			} else {
				// 其他
				sb.append(dataType);

				if (characterMaximumLength != 0) {
					sb.append("(");
					sb.append(characterMaximumLength);
					sb.append(")");
				}
			}
		}

		return sb.toString();
	}

	public static String renderDataType(ColumnDTO dto) {
		return renderDataType(dto.getDataType(), dto.getCharacterMaximumLength(), dto.getNumberScale(),
				dto.getNumberPrecision());
	}

	public static String renderDataType(SearchResultDTO dto) {
		return renderDataType(dto.getDataType(), dto.getCharacterMaximumLength(), dto.getNumberScale(),
				dto.getNumberPrecision());
	}

}
