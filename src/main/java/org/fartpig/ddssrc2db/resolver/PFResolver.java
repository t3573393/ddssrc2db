package org.fartpig.ddssrc2db.resolver;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.fartpig.ddssrc2db.consts.ResolverConst;
import org.fartpig.ddssrc2db.pojo.IComment;
import org.fartpig.ddssrc2db.pojo.pf.PFFileContent;
import org.fartpig.ddssrc2db.pojo.pf.PFLine;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFComment;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFField;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFFile;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFKey;
import org.fartpig.ddssrc2db.pojo.pf.logic.PFRecordFormat;
import org.fartpig.ddssrc2db.service.ColumnsService;
import org.fartpig.ddssrc2db.service.TableIndexService;
import org.fartpig.ddssrc2db.service.TablesService;
import org.fartpig.ddssrc2db.util.ResolverException;
import org.fartpig.ddssrc2db.util.ResolverUtils;
import org.fartpig.ddssrc2db.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PFResolver {

	@Autowired
	ColumnsService columnsService;

	@Autowired
	TableIndexService tableIndexService;

	@Autowired
	TablesService tablesService;

	private static Logger log = LoggerFactory.getLogger(PFResolver.class);

	public PFFileContent parseFile(File file) {
		PFFileContent fileContent = new PFFileContent();
		fileContent.setFileName(file.getName());
		fileContent.setFileType(FilenameUtils.getExtension(file.getName()));
		try {
			List<String> lines = FileUtils.readLines(file, Charset.forName("GBK"));

			PFLine lastFunctionLine = null;
			List<PFLine> pfLines = new ArrayList<PFLine>();
			fileContent.setLines(pfLines);
			for (String aLine : lines) {
				log.info("hanle line:" + aLine);
				PFLine line = new PFLine();
				String lineNum = aLine.substring(0, 12);
				String rawLine = aLine.substring(12);
				line.setLineNum(lineNum);
				line.setRawLine(rawLine);

				String aCol = StringUtils.subStr(aLine, 12, 28);
				String aColChar = StringUtils.subStr(aLine, 17, 18);
				aCol = aCol.trim();
				line.setaCol(aCol);

				// 处理未注释和特殊的包含名称前缀和无标记的行
				if ("A".equals(aCol) || "A".equals(aColChar) || " ".equals(aColChar)) {
					log.debug("resolve A line:" + aLine);
					String tCol = StringUtils.subStr(aLine, 28, 30).trim();
					String nameCol = StringUtils.subStr(aLine, 30, 40).trim();
					String rCol = StringUtils.subStr(aLine, 40, 41).trim();
					String lenCol = StringUtils.subStr(aLine, 41, 46).trim();
					String tDataTypeCol = StringUtils.subStr(aLine, 46, 47).trim();
					String dpCol = StringUtils.subStr(aLine, 47, 49).trim();
					String usageCol = StringUtils.subStr(aLine, 49, 50).trim();
					String functionsCol = StringUtils.subStr(aLine, 56).trim();

					// 解决 FunctionsCol的跨行问题
					if (StringUtils.isNullOrEmpty(nameCol) && !StringUtils.isNullOrEmpty(functionsCol)) {
						if (lastFunctionLine != null) {
							String lastFunctionCol = lastFunctionLine.getFunctionsCol();
							// 上一个 functionsCol 不完整
							if (lastFunctionCol.indexOf("(") != -1 && lastFunctionCol.lastIndexOf(")") == -1) {

								lastFunctionLine.setFunctionsCol(lastFunctionCol + functionsCol);
								// 当前functionsCol 已经补充完了
								if (functionsCol.lastIndexOf(")") != -1) {
									lastFunctionLine = null;
								}
								continue;
							}
						}
					}

					line.settCol(tCol);
					line.setNameCol(nameCol);
					line.setrCol(rCol);
					line.setLenCol(lenCol);
					line.settDataTypeCol(tDataTypeCol);
					line.setDpCol(dpCol);
					line.setUsageCol(usageCol);
					line.setFunctionsCol(functionsCol);

					if (!StringUtils.isNullOrEmpty(functionsCol)) {
						lastFunctionLine = line;
					}

				} else if (ResolverUtils.isComment(aCol, ResolverConst.PF_FILE_TYPE)) {
					// commont line
					log.debug("resolve comment line:" + aLine);

				} else {

					if (aCol != null && aCol.length() > 0) {
						log.error("other unresolve line:" + aLine);
						log.error("aCol:" + aCol);
						log.error("aColChar:" + aColChar);
					}
					// other unresolve line
					// if (aCol.contains("*")) {
					// log.debug("resolve comment line:" + aLine);
					// } else {
					// log.error("other unresolve line:" + aLine);
					// log.error("aCol:" + aCol);
					// log.error("aColChar:" + aColChar);
					// }
				}

				pfLines.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
			log.error("error:" + e.getMessage());
		}
		return fileContent;
	}

	private String retrieveFunctionValue(String functionStr) {

		int startIndex = functionStr.indexOf("(");
		int endIndex = functionStr.lastIndexOf(")");

		String tempResult = functionStr.substring(startIndex + 1, endIndex);
		// 除去 '
		startIndex = tempResult.indexOf("\'");
		if (startIndex != -1) {
			endIndex = tempResult.lastIndexOf("\'");
			if (endIndex > startIndex) {
				tempResult = tempResult.substring(startIndex + 1, endIndex);
			}
		}

		return tempResult;
	}

	private void fillPFFileFunctions(String functionsColStr, PFFile file) {
		if (functionsColStr.startsWith("REF")) {
			String refFile = retrieveFunctionValue(functionsColStr);
			file.setRef(refFile.trim());
		} else if (functionsColStr.startsWith("UNIQUE")) {
			file.setUnique("UNIQUE");
		} else if (functionsColStr.startsWith("FIFO")) {
			file.setOrder("FIFO");
		} else if (functionsColStr.startsWith("FCFO")) {
			file.setOrder("FCFO");
		} else if (functionsColStr.startsWith("LIFO")) {
			file.setOrder("LIFO");
		}
	}

	private void fillPFRecordFormat(String functionsColStr, PFRecordFormat recordFormat) {
		if (functionsColStr.startsWith("FORMAT")) {
			String formatRecord = retrieveFunctionValue(functionsColStr);
			recordFormat.setFormat(formatRecord.trim());
		} else if (functionsColStr.startsWith("TEXT")) {
			String text = retrieveFunctionValue(functionsColStr);
			recordFormat.setText(text.trim());
		}
	}

	private void fillPFField(String functionsColStr, PFField field) {
		if (functionsColStr.startsWith("COLHDG")) {
			String colHdg = retrieveFunctionValue(functionsColStr);
			field.setColHdg(colHdg.trim());
		} else if (functionsColStr.startsWith("REFFLD")) {
			String refFld = retrieveFunctionValue(functionsColStr);
			field.setRefFld(refFld.trim());
		} else if (functionsColStr.startsWith("VARLEN")) {
			field.setVarLen("VARLEN");
		} else if (functionsColStr.startsWith("EDTWRD")) {
			String edtWrd = retrieveFunctionValue(functionsColStr);
			field.setEdtWrd(edtWrd.trim());
		}
	}

	private void fillPFKey(String functionsColStr, PFKey key) {
		if (functionsColStr.startsWith("DESCEND")) {
			key.setDescEnd("DESCEND");
		} else if (functionsColStr.startsWith("ABSVAL")) {
			key.setAbsVal("ABSVAL");
		}
	}

	private void appendComment(IComment commentTarget, PFComment comment) {
		StringBuilder temp = new StringBuilder();
		String oldComment = commentTarget.getComment();
		if (oldComment != null) {
			temp.append(oldComment).append(ResolverConst.LINE_BREAK);
		}
		temp.append(comment.getAll());
		commentTarget.setComment(temp.toString());
	}

	private void handleContextComment(PFComment commentTemp, IComment targetComment) {
		// 处理特殊的向下注释的结构
		List<String> nextComments = null;
		if (commentTemp.isWithLastComment() == false) {
			int startIndex = -1, endIndex = -1;
			for (int i = 0; i < commentTemp.size(); i++) {
				String comment = commentTemp.getComment(i);
				if (comment.endsWith("-*")) {
					if (startIndex == -1) {
						startIndex = i;
					} else {
						endIndex = i;
					}
				}
			}

			if (startIndex != -1) {
				List<String> lastComments = new ArrayList<String>(commentTemp.getComments().subList(0, startIndex));
				nextComments = new ArrayList<String>(
						commentTemp.getComments().subList(startIndex, commentTemp.getComments().size()));

				commentTemp.reset();
				commentTemp.setComments(lastComments);
			}
		}

		appendComment(targetComment, commentTemp);

		commentTemp.reset();
		if (nextComments != null) {
			commentTemp.setComments(nextComments);
			commentTemp.setWithLastComment(true);
		}

	}

	public PFFile convertToPFFile(PFFileContent content) {
		PFFile file = new PFFile();
		PFRecordFormat recordFormatTemp = null;
		PFField fieldTemp = null;
		PFKey keyTemp = null;

		// 上一行类型: FILE RECORD FIELD KEY
		String lastLineType = "FILE";

		file.setName(FilenameUtils.getBaseName(content.getFileName()));

		PFComment commentTemp = new PFComment();
		for (PFLine line : content.getLines()) {
			String aCol = line.getaCol();
			if (ResolverUtils.isComment(aCol, ResolverConst.PF_FILE_TYPE)) {
				commentTemp.append(line.getRawLine());
			} else {
				String tCol = line.gettCol();
				// 当前只处理单个 记录格式的文件
				if ("FILE".equals(lastLineType)) {
					if (StringUtils.isNullOrEmpty(tCol)) {
						// 文件级
						// 这里为文件级中的空行
						String functionsColStr = line.getFunctionsCol();
						if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillPFFileFunctions(functionsColStr, file);
						}
					} else if ("R".equals(tCol)) {
						// 记录格式
						lastLineType = "RECORD";

						recordFormatTemp = new PFRecordFormat();
						file.getRecordFormats().add(recordFormatTemp);

						recordFormatTemp.setName(line.getNameCol());
						recordFormatTemp.setComment(commentTemp.getAll());
						commentTemp.reset();

						String functionsColStr = line.getFunctionsCol();
						if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillPFRecordFormat(functionsColStr, recordFormatTemp);
						}
					}
				} else if ("RECORD".equals(lastLineType)) {
					String name = line.getNameCol();
					// 这里为记录级之下的内容
					if (name == null || name.length() == 0) {
						// functions 加入记录级中
						String functionsColStr = line.getFunctionsCol();
						if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillPFRecordFormat(functionsColStr, recordFormatTemp);
						}
					} else {
						// 拼接记录格式的注释
						if (commentTemp.size() > 0) {
							// 处理特殊的向下注释的结构
							handleContextComment(commentTemp, recordFormatTemp);
						}

						// 直接从记录级进入关键字级
						if ("K".equals(tCol)) {
							lastLineType = "KEY";

							keyTemp = new PFKey();
							recordFormatTemp.getKeys().add(keyTemp);

							keyTemp.setName(line.getNameCol());
							String functionsColStr = line.getFunctionsCol();
							if (!StringUtils.isNullOrEmpty(functionsColStr)) {
								fillPFKey(functionsColStr, keyTemp);
							}
						} else {
							// 字段级
							lastLineType = "FIELD";

							fieldTemp = new PFField();
							recordFormatTemp.getFields().add(fieldTemp);

							fieldTemp.setName(line.getNameCol());
							fieldTemp.setUse(line.getUsageCol());

							// 处理R类型
							if ("R".equals(line.getrCol())) {
								fieldTemp.setRefFld(line.getNameCol());
							} else {
								fieldTemp.setDataType(line.gettDataTypeCol());
								fieldTemp.setLength(line.getLenCol());
								fieldTemp.setDecimalPosition(line.getDpCol());
							}

							String functionsColStr = line.getFunctionsCol();
							if (!StringUtils.isNullOrEmpty(functionsColStr)) {
								fillPFField(functionsColStr, fieldTemp);
							}
						}
					}
				} else if ("FIELD".equals(lastLineType)) {
					String name = line.getNameCol();
					// 这里为字段级之下的内容
					if (name == null || name.length() == 0) {
						// functions 加入字段级中
						String functionsColStr = line.getFunctionsCol();
						if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillPFField(functionsColStr, fieldTemp);
						}
					} else {
						// 拼接字段级的注释
						if (commentTemp.size() > 0) {
							// 处理特殊的向下注释的结构
							handleContextComment(commentTemp, fieldTemp);
						}

						// 从字段级进入关键字级
						if ("K".equals(tCol)) {
							lastLineType = "KEY";

							keyTemp = new PFKey();
							recordFormatTemp.getKeys().add(keyTemp);

							keyTemp.setName(line.getNameCol());
							String functionsColStr = line.getFunctionsCol();
							if (!StringUtils.isNullOrEmpty(functionsColStr)) {
								fillPFKey(functionsColStr, keyTemp);
							}
						} else {
							// 字段级
							lastLineType = "FIELD";

							fieldTemp = new PFField();
							recordFormatTemp.getFields().add(fieldTemp);

							fieldTemp.setName(line.getNameCol());
							fieldTemp.setUse(line.getUsageCol());
							// 处理R类型
							if ("R".equals(line.getrCol())) {
								fieldTemp.setRefFld(line.getNameCol());
							} else {
								fieldTemp.setDataType(line.gettDataTypeCol());
								fieldTemp.setLength(line.getLenCol());
								fieldTemp.setDecimalPosition(line.getDpCol());
							}

							String functionsColStr = line.getFunctionsCol();
							if (!StringUtils.isNullOrEmpty(functionsColStr)) {
								fillPFField(functionsColStr, fieldTemp);
							}
						}
					}
				} else if ("KEY".equals(lastLineType)) {
					String name = line.getNameCol();
					// 这里为关键字级之下的内容
					if (name == null || name.length() == 0) {
						// functions 加入字段级中
						String functionsColStr = line.getFunctionsCol();
						if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillPFKey(functionsColStr, keyTemp);
						}
					} else {
						// 拼接关键字级的注释
						if (commentTemp.size() > 0) {
							appendComment(keyTemp, commentTemp);

							commentTemp.reset();
						}

						// 从关键字级进入关键字级
						if ("K".equals(tCol)) {
							lastLineType = "KEY";

							keyTemp = new PFKey();
							recordFormatTemp.getKeys().add(keyTemp);

							keyTemp.setName(line.getNameCol());
							String functionsColStr = line.getFunctionsCol();
							if (!StringUtils.isNullOrEmpty(functionsColStr)) {
								fillPFKey(functionsColStr, keyTemp);
							}
						} else {
							// 未知行
							log.error("unparse line:" + line.getRawLine());
						}
					}
				}

			}
		}

		// 拼接字段级的注释
		if (commentTemp.size() > 0) {
			IComment targetComment = null;

			if ("FILE".equals(lastLineType)) {
				targetComment = recordFormatTemp;
			} else if ("RECORD".equals(lastLineType)) {
				targetComment = recordFormatTemp;
			} else if ("FIELD".equals(lastLineType)) {
				targetComment = fieldTemp;
			} else if ("KEY".equals(lastLineType)) {
				targetComment = keyTemp;
			}
			// 处理特殊的向下注释的结构
			if (targetComment != null) {
				handleContextComment(commentTemp, targetComment);
			}

		}

		return file;
	}

	public void fillRefFiles(Map<String, PFFile> allPfFiles) {
		Set<String> filledFiles = new HashSet<String>();

		Stack<String> workStack = new Stack<String>();
		for (Map.Entry<String, PFFile> fileEnty : allPfFiles.entrySet()) {
			String pfFileName = fileEnty.getKey();
			if (filledFiles.contains(pfFileName)) {
				continue;
			}

			PFFile pfFile = fileEnty.getValue();
			if (!StringUtils.isNullOrEmpty(pfFile.getRef())) {
				workStack.push(fileEnty.getKey());
				workStack.push(pfFile.getRef());

				// 找到根节点
				while (true) {
					String lastOne = workStack.peek();
					PFFile lastPFFile = allPfFiles.get(lastOne);
					if (lastPFFile == null) {
						throw new ResolverException("fillRefFiles", "PFFILE:" + lastOne + " IS NOT NULL");
					}

					if (!StringUtils.isNullOrEmpty(lastPFFile.getRef())) {
						workStack.push(lastPFFile.getRef());
					} else {
						break;
					}
				}

				// 逆序处理引用关系
				String lastOne = workStack.pop();
				filledFiles.add(lastOne);
				while (workStack.size() > 0) {
					String currentOne = workStack.pop();
					filledFiles.add(currentOne);

					PFFile lastPFFile = allPfFiles.get(lastOne);
					PFFile currentPFFile = allPfFiles.get(currentOne);
					for (PFRecordFormat recordFormat : currentPFFile.getRecordFormats()) {
						for (PFField field : recordFormat.getFields()) {

							if (!StringUtils.isNullOrEmpty(field.getRefFld())) {
								String refFld = field.getRefFld();
								boolean isFind = false;
								for (PFRecordFormat lastOneRecordFormat : lastPFFile.getRecordFormats()) {
									for (PFField lastOneField : lastOneRecordFormat.getFields()) {
										if (refFld.equals(lastOneField.getName())) {

											field.setDataType(lastOneField.getDataType());
											field.setDecimalPosition(lastOneField.getDecimalPosition());
											field.setEdtWrd(lastOneField.getEdtWrd());
											field.setLength(lastOneField.getLength());
											field.setVarLen(lastOneField.getVarLen());
											isFind = true;
											break;
										}
									}

									if (isFind) {
										break;
									}
								}
							}
						}
					}

					lastOne = currentOne;
				}
			}
		}
	}

	public void shareFormat(Map<String, PFFile> allPfFiles) throws CloneNotSupportedException {
		Set<String> filledFiles = new HashSet<String>();

		Stack<String> workStack = new Stack<String>();
		for (Map.Entry<String, PFFile> fileEnty : allPfFiles.entrySet()) {
			String pfFileName = fileEnty.getKey();
			if (filledFiles.contains(pfFileName)) {
				continue;
			}

			PFFile pfFile = fileEnty.getValue();
			for (PFRecordFormat recordFormat : pfFile.getRecordFormats()) {

				if (!StringUtils.isNullOrEmpty(recordFormat.getFormat())) {
					workStack.push(fileEnty.getKey());
					workStack.push(recordFormat.getFormat());

					// 找到根节点
					while (true) {
						String lastOne = workStack.peek();
						PFFile lastPFFile = allPfFiles.get(lastOne);
						if (lastPFFile == null) {
							throw new ResolverException("shareFormat", "PFFILE:" + lastOne + " IS NOT NULL");
						}

						boolean isPush = false;
						for (PFRecordFormat lastRecordFormat : lastPFFile.getRecordFormats()) {
							if (!StringUtils.isNullOrEmpty(lastRecordFormat.getFormat())) {
								workStack.push(lastRecordFormat.getFormat());
								isPush = true;
								break;
							}
						}

						if (!isPush) {
							break;
						}
					}

					// 逆序处理引用关系
					String lastOne = workStack.pop();
					filledFiles.add(lastOne);
					while (workStack.size() > 0) {
						String currentOne = workStack.pop();
						filledFiles.add(currentOne);

						PFFile lastPFFile = allPfFiles.get(lastOne);
						PFFile currentPFFile = allPfFiles.get(currentOne);
						for (PFRecordFormat currentRecordFormat : currentPFFile.getRecordFormats()) {
							// 获取第一个记录格式进行复制
							PFRecordFormat lastRecordFormat = lastPFFile.getRecordFormats().get(0);
							for (PFField field : lastRecordFormat.getFields()) {
								currentRecordFormat.getFields().add((PFField) field.clone());
							}
						}

						lastOne = currentOne;
					}

				}
			}
		}

	}

	@Transactional
	public int deleteFromDB(PFFile pfFile) {
		int removeNum = 0;
		for (PFRecordFormat recordFormat : pfFile.getRecordFormats()) {
			tablesService.deleteTables(pfFile, recordFormat);
			columnsService.deleteAllColumns(pfFile, recordFormat);
			tableIndexService.deleteTableIndex(pfFile, recordFormat,
					ResolverUtils.getDefaultTableIndex(recordFormat.getName()));
			removeNum++;
		}
		return removeNum;
	}

	@Transactional
	public void saveToDB(PFFile pfFile) {
		for (PFRecordFormat recordFormat : pfFile.getRecordFormats()) {
			try {
				tablesService.insertOrUpdateTable(pfFile, recordFormat);

				int index = 1;
				for (PFField field : recordFormat.getFields()) {
					columnsService.insertOrUpdateColumn(pfFile, recordFormat, field, index);
					index++;
				}

				index = 1;
				for (PFKey key : recordFormat.getKeys()) {
					tableIndexService.insertOrUpdateTableIndex(pfFile, recordFormat, key, index);
					index++;
				}
			} catch (Exception e) {
				log.error("save PFFile:" + pfFile.getName() + " - PFRecordFormat:" + recordFormat.getName()
						+ " to db fail", e);
			}
		}

	}
}
