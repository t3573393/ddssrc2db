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
import org.fartpig.ddssrc2db.pojo.lf.LFFileContent;
import org.fartpig.ddssrc2db.pojo.lf.LFLine;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFComment;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFCondition;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFConditionTree;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFField;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFFile;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFJoin;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFKey;
import org.fartpig.ddssrc2db.pojo.lf.logic.LFRecordFormat;
import org.fartpig.ddssrc2db.service.ColumnsService;
import org.fartpig.ddssrc2db.service.TableIndexService;
import org.fartpig.ddssrc2db.service.TablesService;
import org.fartpig.ddssrc2db.service.ViewConditionService;
import org.fartpig.ddssrc2db.util.ResolverException;
import org.fartpig.ddssrc2db.util.ResolverUtils;
import org.fartpig.ddssrc2db.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LFResolver {

	@Autowired
	ColumnsService columnsService;

	@Autowired
	TableIndexService tableIndexService;

	@Autowired
	TablesService tablesService;

	@Autowired
	ViewConditionService viewConditionService;

	private static Logger log = LoggerFactory.getLogger(LFResolver.class);

	public LFFileContent parseFile(File file) {
		LFFileContent fileContent = new LFFileContent();
		fileContent.setFileName(file.getName());
		fileContent.setFileType(FilenameUtils.getExtension(file.getName()));
		try {
			List<String> lines = FileUtils.readLines(file, Charset.forName("GBK"));

			LFLine lastFunctionLine = null;
			List<LFLine> lfLines = new ArrayList<LFLine>();
			fileContent.setLines(lfLines);
			for (String aLine : lines) {
				log.info("hanle line:" + aLine);
				LFLine line = new LFLine();
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

				lfLines.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
			log.error("error:" + e.getMessage());
		}
		return fileContent;
	}

	private String retrieveFunctionValue(String functionStr) {

		// 特殊处理 VALUES
		boolean needTrimQuotation = true;
		if (functionStr.startsWith("VALUES")) {
			needTrimQuotation = false;
		}

		int startIndex = functionStr.indexOf("(");
		int endIndex = functionStr.lastIndexOf(")");

		String tempResult = functionStr.substring(startIndex + 1, endIndex);
		if (needTrimQuotation) {
			// 除去 '
			startIndex = tempResult.indexOf("\'");
			if (startIndex != -1) {
				endIndex = tempResult.lastIndexOf("\'");
				if (endIndex > startIndex) {
					tempResult = tempResult.substring(startIndex + 1, endIndex);
				}
			}
		}

		return tempResult;
	}

	private void fillLFFileFunctions(String functionsColStr, LFFile file) {
		if (functionsColStr.startsWith("UNIQUE")) {
			file.setUnique("UNIQUE");
		}
	}

	private void fillLFRecordFormat(String functionsColStr, LFRecordFormat recordFormat) {
		if (functionsColStr.startsWith("FORMAT")) {
			String formatRecord = retrieveFunctionValue(functionsColStr);
			recordFormat.setFormat(formatRecord.trim());
		} else if (functionsColStr.startsWith("TEXT")) {
			String text = retrieveFunctionValue(functionsColStr);
			recordFormat.setText(text.trim());
		} else if (functionsColStr.startsWith("JFILE")) {
			String jFile = retrieveFunctionValue(functionsColStr);
			recordFormat.setjFile(jFile);
		} else if (functionsColStr.startsWith("PFILE")) {
			String pFile = retrieveFunctionValue(functionsColStr);
			recordFormat.setpFile(pFile);
		}
	}

	private void fillLFField(String functionsColStr, LFField field) {
		if (functionsColStr.startsWith("COLHDG")) {
			String colHdg = retrieveFunctionValue(functionsColStr);
			field.setColHdg(colHdg.trim());
		} else if (functionsColStr.startsWith("SST")) {
			String sst = retrieveFunctionValue(functionsColStr);
			field.setSst(sst);
		} else if (functionsColStr.startsWith("JREF")) {
			String jRef = retrieveFunctionValue(functionsColStr);
			field.setjRef(jRef);
		} else if (functionsColStr.startsWith("RENAME")) {
			String rename = retrieveFunctionValue(functionsColStr);
			field.setRename(rename);
		} else if (functionsColStr.startsWith("TEXT")) {
			String text = retrieveFunctionValue(functionsColStr);
			field.setText(text);
		}
	}

	private void fillLFKey(String functionsColStr, LFKey key) {
		if (functionsColStr.startsWith("DESCEND")) {
			key.setDescEnd("DESCEND");
		} else if (functionsColStr.startsWith("ABSVAL")) {
			key.setAbsVal("ABSVAL");
		}
	}

	private void fillLFJoin(String functionsColStr, LFJoin join) {
		if (functionsColStr.startsWith("JOIN")) {
			String joinStr = retrieveFunctionValue(functionsColStr);
			join.setJoin(joinStr);
		} else if (functionsColStr.startsWith("JFLD")) {
			String jFld = retrieveFunctionValue(functionsColStr);
			join.setjFld(jFld);
		}
	}

	private void fillLFCondition(String functionsColStr, LFCondition condition) {
		condition.setExpr(functionsColStr);
	}

	private void handleContextComment(LFComment commentTemp, IComment targetComment) {
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

	private void appendComment(IComment commentTarget, LFComment comment) {
		StringBuilder temp = new StringBuilder();
		String oldComment = commentTarget.getComment();
		if (oldComment != null) {
			temp.append(oldComment).append(ResolverConst.LINE_BREAK);
		}
		temp.append(comment.getAll());
		commentTarget.setComment(temp.toString());
	}

	private class LFStatusMachine {

		LFRecordFormat recordFormatTemp = null;
		LFJoin joinTemp = null;
		LFField fieldTemp = null;
		LFKey keyTemp = null;

		LFConditionTree lastConditionTreeTemp = null;
		LFCondition conditionTemp = null;

		String lastLineType = "FILE";

		public LFStatusMachine(LFRecordFormat recordFormat) {
			this.recordFormatTemp = recordFormat;
		}

		public void restToRecord(LFRecordFormat recordFormat) {
			this.recordFormatTemp = recordFormat;
			lastLineType = "RECORD";

			joinTemp = null;
			fieldTemp = null;
			keyTemp = null;

			conditionTemp = null;
		}

		public String handleStatus(String tCol, LFLine line) {
			// 进入连接级
			if ("J".equals(tCol)) {
				lastLineType = "JOIN";

				joinTemp = new LFJoin();
				recordFormatTemp.getJoins().add(joinTemp);

				String functionsColStr = line.getFunctionsCol();
				if (!StringUtils.isNullOrEmpty(functionsColStr)) {
					fillLFJoin(functionsColStr, joinTemp);
				}
			} else if ("K".equals(tCol)) {
				// 进入关键字级
				lastLineType = "KEY";

				keyTemp = new LFKey();
				recordFormatTemp.getKeys().add(keyTemp);

				keyTemp.setName(line.getNameCol());
				String functionsColStr = line.getFunctionsCol();
				if (!StringUtils.isNullOrEmpty(functionsColStr)) {
					fillLFKey(functionsColStr, keyTemp);
				}
			} else if ("S".equals(tCol) || "O".equals(tCol)) {
				// 进入条件级
				lastLineType = "CONDITION";

				conditionTemp = new LFCondition();
				conditionTemp.setName(line.getNameCol());
				String functionsColStr = line.getFunctionsCol();
				if (!StringUtils.isNullOrEmpty(functionsColStr)) {
					fillLFCondition(functionsColStr, conditionTemp);
				}

				// 构建条件表达式树, 先将所有的条件整理到根节点下，最后进行 语法树的构建
				if (recordFormatTemp.getConditionTree() == null) {
					LFConditionTree rootConditionTree = new LFConditionTree();
					recordFormatTemp.setConditionTree(rootConditionTree);
				}

				lastConditionTreeTemp = new LFConditionTree();
				recordFormatTemp.getConditionTree().getConditionChilds().add(lastConditionTreeTemp);

				if ("S".equals(tCol)) {
					lastConditionTreeTemp.setConditionType(ResolverConst.CONDITION_TYPE_S);
				} else {
					lastConditionTreeTemp.setConditionType(ResolverConst.CONDITION_TYPE_O);
				}
				lastConditionTreeTemp.setOperationType(ResolverConst.OPERATIONTYPE_AND);
				lastConditionTreeTemp.getLeafs().add(conditionTemp);
			} else {

				if ("CONDITION".equals(lastLineType)) {
					// 进入条件级
					conditionTemp = new LFCondition();
					conditionTemp.setName(line.getNameCol());
					String functionsColStr = line.getFunctionsCol();
					if (!StringUtils.isNullOrEmpty(functionsColStr)) {
						fillLFCondition(functionsColStr, conditionTemp);
					}

					lastConditionTreeTemp.getLeafs().add(conditionTemp);
				} else {
					// 进入字段级
					lastLineType = "FIELD";

					fieldTemp = new LFField();
					recordFormatTemp.getFields().add(fieldTemp);

					fieldTemp.setName(line.getNameCol());
					fieldTemp.setDataType(line.gettDataTypeCol());
					fieldTemp.setLength(line.getLenCol());
					fieldTemp.setDecimalPosition(line.getDpCol());
					fieldTemp.setUse(line.getUsageCol());

					String functionsColStr = line.getFunctionsCol();
					if (!StringUtils.isNullOrEmpty(functionsColStr)) {
						fillLFField(functionsColStr, fieldTemp);
					}
				}
			}

			return lastLineType;
		}
	}

	private void reConstructConditionTree(LFConditionTree rootConditionTree) {
		if (rootConditionTree == null) {
			return;
		}
		// AND : COUNT(S) <=1 COUNT(O)>=0
		// OR : COUNT(S) > 1 COUNT(O)==0
		// AND OR : COUNT(S) > 1 COUNT(O)>0
		int sCount = 0, oCount = 0;
		LFConditionTree allTree = null;
		for (LFConditionTree aTree : rootConditionTree.getConditionChilds()) {
			if (aTree.getLeafs().size() > 0 && "ALL".equals(aTree.getLeafs().get(0).getExpr())) {
				allTree = aTree;
				continue;
			}

			if ("S".equals(aTree.getConditionType())) {
				sCount++;
			} else if ("O".equals(aTree.getConditionType())) {
				oCount++;
			}
		}

		String conditionType = ResolverConst.CONDITION_TYPE_S;
		if (sCount > 0 && oCount == 0) {
			conditionType = ResolverConst.CONDITION_TYPE_S;
		} else if (sCount == 0 && oCount > 0) {
			conditionType = ResolverConst.CONDITION_TYPE_O;
		} else if (sCount > 0 && oCount > 0) {
			conditionType = ResolverConst.CONDITION_TYPE_SO;
		}
		rootConditionTree.setConditionType(conditionType);

		if (sCount <= 1 && oCount >= 0) {
			if ((sCount == 1 && oCount == 0) || (sCount == 0 && oCount == 1)) {
				// 迁移子 S 到根上
				LFConditionTree sConditionTree = rootConditionTree.getConditionChilds().get(0);
				rootConditionTree.setOperationType(sConditionTree.getOperationType());
				rootConditionTree.setConditionType(sConditionTree.getConditionType());
				rootConditionTree.setComment(sConditionTree.getComment());

				rootConditionTree.getConditionChilds().clear();
				rootConditionTree.getLeafs().addAll(sConditionTree.getLeafs());
			} else {
				rootConditionTree.setOperationType(ResolverConst.OPERATIONTYPE_AND);
			}
		} else if (sCount > 1 && oCount == 0) {
			rootConditionTree.setOperationType(ResolverConst.OPERATIONTYPE_OR);
		} else {
			// 重新排列 S 都放到一个 OR 里面中， O和 重排后的S AND
			LFConditionTree sConditionTree = new LFConditionTree();
			sConditionTree.setConditionType(ResolverConst.CONDITION_TYPE_S);
			sConditionTree.setOperationType(ResolverConst.OPERATIONTYPE_OR);

			List<LFConditionTree> newChilds = new ArrayList<LFConditionTree>();
			newChilds.add(sConditionTree);

			for (LFConditionTree aTree : rootConditionTree.getConditionChilds()) {
				if (aTree.getLeafs().size() > 0 && "ALL".equals(aTree.getLeafs().get(0).getExpr())) {
					allTree = aTree;
					continue;
				}

				if ("S".equals(aTree.getConditionType())) {
					sConditionTree.getConditionChilds().add(aTree);
				} else if ("O".equals(aTree.getConditionType())) {
					newChilds.add(aTree);
				}
			}

			if (allTree != null) {
				newChilds.add(allTree);
			}

			rootConditionTree.setOperationType(ResolverConst.OPERATIONTYPE_AND);
			rootConditionTree.setConditionChilds(newChilds);
		}

	}

	public LFFile convertToLFFile(LFFileContent content) {
		LFFile file = new LFFile();
		LFRecordFormat recordFormatTemp = null;

		LFStatusMachine statusMachine = null;

		// 上一行类型: FILE RECORD JOIN FIELD KEY CONDITION
		String lastLineType = "FILE";

		file.setName(FilenameUtils.getBaseName(content.getFileName()));

		LFComment commentTemp = new LFComment();
		for (LFLine line : content.getLines()) {
			String aCol = line.getaCol();
			if (ResolverUtils.isComment(aCol, ResolverConst.LF_FILE_TYPE)) {
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
							fillLFFileFunctions(functionsColStr, file);
						}
					} else if ("R".equals(tCol)) {
						// 记录格式
						lastLineType = "RECORD";

						if (recordFormatTemp != null) {
							reConstructConditionTree(recordFormatTemp.getConditionTree());
						}

						recordFormatTemp = new LFRecordFormat();
						file.getRecordFormats().add(recordFormatTemp);
						recordFormatTemp.setName(line.getNameCol());
						recordFormatTemp.setComment(commentTemp.getAll());
						commentTemp.reset();

						String functionsColStr = line.getFunctionsCol();
						if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillLFRecordFormat(functionsColStr, recordFormatTemp);
						}

						if (statusMachine == null) {
							statusMachine = new LFStatusMachine(recordFormatTemp);
						} else {
							statusMachine.restToRecord(recordFormatTemp);
						}

					}
				} else if ("RECORD".equals(lastLineType)) {
					String name = line.getNameCol();
					// 这里为记录级之下的内容, 排除 J 类型
					if (!"J".equals(tCol) && (name == null || name.length() == 0)) {
						// functions 加入记录级中
						String functionsColStr = line.getFunctionsCol();
						if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillLFRecordFormat(functionsColStr, recordFormatTemp);
						}
					} else {
						// 拼接记录格式的注释
						if (commentTemp.size() > 0) {
							// 处理特殊的向下注释的结构
							handleContextComment(commentTemp, recordFormatTemp);
						}

						lastLineType = statusMachine.handleStatus(tCol, line);
					}
				} else if ("JOIN".equals(lastLineType)) {
					String name = line.getNameCol();
					// 这里为记录级之下的内容
					if (name == null || name.length() == 0) {
						// functions 加入记录级中
						String functionsColStr = line.getFunctionsCol();
						if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillLFJoin(functionsColStr, statusMachine.joinTemp);
						}
					} else {
						// 拼接连接的注释
						if (commentTemp.size() > 0) {
							// 处理特殊的向下注释的结构
							handleContextComment(commentTemp, statusMachine.joinTemp);
						}

						lastLineType = statusMachine.handleStatus(tCol, line);
					}
				} else if ("FIELD".equals(lastLineType)) {
					String name = line.getNameCol();
					// 这里为字段级之下的内容
					if (name == null || name.length() == 0) {
						// functions 加入字段级中
						String functionsColStr = line.getFunctionsCol();
						if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillLFField(functionsColStr, statusMachine.fieldTemp);
						}
					} else {
						// 拼接字段级的注释
						if (commentTemp.size() > 0) {
							// 处理特殊的向下注释的结构
							handleContextComment(commentTemp, statusMachine.fieldTemp);
						}

						lastLineType = statusMachine.handleStatus(tCol, line);
					}
				} else if ("KEY".equals(lastLineType)) {
					String name = line.getNameCol();
					// 这里为关键字级之下的内容
					if (name == null || name.length() == 0) {
						// functions 加入字段级中
						String functionsColStr = line.getFunctionsCol();
						if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillLFKey(functionsColStr, statusMachine.keyTemp);
						}
					} else {
						// 拼接关键字级的注释
						if (commentTemp.size() > 0) {
							appendComment(statusMachine.keyTemp, commentTemp);

							commentTemp.reset();
						}

						lastLineType = statusMachine.handleStatus(tCol, line);
					}
				} else if ("CONDITION".equals(lastLineType)) {
					String name = line.getNameCol();
					// 这里为条件级之下的内容
					if (name == null || name.length() == 0) {
						// functions 加入记录级中
						String functionsColStr = line.getFunctionsCol();

						// 特殊处理 ALL 关键字
						if ("ALL".equals(functionsColStr)) {

							LFCondition conditionTemp = new LFCondition();
							conditionTemp.setExpr(functionsColStr);

							// 构建条件表达式树, 先将所有的条件整理到根节点下，最后进行 语法树的构建
							if (recordFormatTemp.getConditionTree() == null) {
								LFConditionTree rootConditionTree = new LFConditionTree();
								recordFormatTemp.setConditionTree(rootConditionTree);
							}

							LFConditionTree lastConditionTreeTemp = new LFConditionTree();
							recordFormatTemp.getConditionTree().getConditionChilds().add(lastConditionTreeTemp);

							if ("S".equals(tCol)) {
								lastConditionTreeTemp.setConditionType(ResolverConst.CONDITION_TYPE_S);
							} else {
								lastConditionTreeTemp.setConditionType(ResolverConst.CONDITION_TYPE_O);
							}
							lastConditionTreeTemp.setOperationType(ResolverConst.OPERATIONTYPE_AND);
							lastConditionTreeTemp.getLeafs().add(conditionTemp);

						} else if (!StringUtils.isNullOrEmpty(functionsColStr)) {
							fillLFCondition(functionsColStr, statusMachine.conditionTemp);
						}
					} else {
						// 拼接记录格式的注释
						if (commentTemp.size() > 0) {
							// 处理特殊的向下注释的结构
							handleContextComment(commentTemp, statusMachine.conditionTemp);
						}

						lastLineType = statusMachine.handleStatus(tCol, line);
					}
				}
			}
		}

		if (recordFormatTemp != null) {
			reConstructConditionTree(recordFormatTemp.getConditionTree());
		}

		// 将多余的注释放到最后一个元素中
		if (commentTemp.size() > 0) {
			if (statusMachine != null) {
				IComment targetComment = null;
				String lineType = statusMachine.lastLineType;

				if ("FILE".equals(lastLineType)) {
					targetComment = statusMachine.recordFormatTemp;
				} else if ("RECORD".equals(lineType)) {
					targetComment = statusMachine.recordFormatTemp;
				} else if ("JOIN".equals(lineType)) {
					targetComment = statusMachine.joinTemp;
				} else if ("FIELD".equals(lineType)) {
					targetComment = statusMachine.fieldTemp;
				} else if ("KEY".equals(lineType)) {
					targetComment = statusMachine.keyTemp;
				} else if ("CONDITION".equals(lineType)) {
					targetComment = statusMachine.conditionTemp;
				}

				if (targetComment != null) {
					handleContextComment(commentTemp, targetComment);
				}
			}

		}

		return file;
	}

	public void shareFormat(Map<String, LFFile> allLfFiles) throws CloneNotSupportedException {
		// 当前只处理 共享 逻辑文件之间的记录格式
		Set<String> filledFiles = new HashSet<String>();

		Stack<String> workStack = new Stack<String>();
		for (Map.Entry<String, LFFile> fileEnty : allLfFiles.entrySet()) {
			String lfFileName = fileEnty.getKey();
			if (filledFiles.contains(lfFileName)) {
				continue;
			}

			LFFile lfFile = fileEnty.getValue();
			for (LFRecordFormat recordFormat : lfFile.getRecordFormats()) {

				if (!StringUtils.isNullOrEmpty(recordFormat.getFormat())) {
					workStack.push(fileEnty.getKey());
					workStack.push(recordFormat.getFormat());

					// 找到根节点
					while (true) {
						String lastOne = workStack.peek();
						LFFile lastPFFile = allLfFiles.get(lastOne);
						if (lastPFFile == null) {
							throw new ResolverException("shareFormat", "LFFILE:" + lastOne + " IS NOT NULL");
						}

						boolean isPush = false;
						for (LFRecordFormat lastRecordFormat : lastPFFile.getRecordFormats()) {
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

						LFFile lastLFFile = allLfFiles.get(lastOne);
						LFFile currentLFFile = allLfFiles.get(currentOne);
						for (LFRecordFormat currentRecordFormat : currentLFFile.getRecordFormats()) {
							// 获取第一个记录格式进行复制
							LFRecordFormat lastRecordFormat = lastLFFile.getRecordFormats().get(0);
							for (LFField field : lastRecordFormat.getFields()) {
								currentRecordFormat.getFields().add((LFField) field.clone());
							}
						}

						lastOne = currentOne;
					}

				}
			}
		}

	}

	@Transactional
	public int deleteFromDB(LFFile lfFile) {
		int removeNum = 0;
		for (LFRecordFormat recordFormat : lfFile.getRecordFormats()) {
			tablesService.deleteTables(lfFile, recordFormat);
			columnsService.deleteAllColumns(lfFile, recordFormat);
			tableIndexService.deleteTableIndex(lfFile, recordFormat);
			viewConditionService.deleteViewConditions(lfFile, recordFormat);
			removeNum++;
		}
		return removeNum;
	}

	@Transactional
	public void saveToDB(LFFile lfFile) {
		for (LFRecordFormat recordFormat : lfFile.getRecordFormats()) {
			try {
				tablesService.insertOrUpdateTable(lfFile, recordFormat);

				int index = 1;
				for (LFField field : recordFormat.getFields()) {
					columnsService.insertOrUpdateColumn(lfFile, recordFormat, field, index);
					index++;
				}

				index = 1;
				for (LFKey key : recordFormat.getKeys()) {
					tableIndexService.insertOrUpdateTableIndex(lfFile, recordFormat, key, index);
					index++;
				}

				viewConditionService.insertViewCondition(lfFile, recordFormat);
			} catch (Exception e) {
				log.error("save PFFile:" + lfFile.getName() + " - PFRecordFormat:" + recordFormat.getName()
						+ " to db fail", e);
			}
		}

	}
}
