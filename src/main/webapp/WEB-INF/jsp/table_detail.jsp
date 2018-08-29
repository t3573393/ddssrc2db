<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html lang="en">
<head>
<title>文件明细:${tableDetailDTO.tableName}</title>
</head>

<body>
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h2>文件明细:${tableDetailDTO.tableName}</h2>
			</div>
		</div>
		<div class="clearfix"></div>

		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="x_title">
						<h2>文件</h2>
						<ul class="nav navbar-right panel_toolbox">
							<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>
					<div class="x_content">
						<div class="table-responsive">
							<table class="table">
								<tbody>
									<tr>
										<th style="width: 50%">名称</th>
										<td><c:out value="${tableDetailDTO.tableName}" /></td>
									</tr>
									<tr>
										<th>类型</th>
										<td><c:out value="${tableDetailDTO.tableType}" /></td>
									</tr>
									<tr>
										<th>记录格式名称</th>
										<td><c:out value="${tableDetailDTO.recordFormatName}" /></td>
									</tr>
									<tr>
										<th>引用文件</th>
										<td>
										<c:choose>
										<c:when test="${refTablesId == null}"><c:out value="${tableDetailDTO.refTables}" /></c:when>
										<c:otherwise><a href="<c:url value="/load_table_detail"/>?id=<c:out value="${refTablesId }" />" target="_blank"><span class="glyphicon glyphicon-file" aria-hidden="true"></span><c:out value="${tableDetailDTO.refTables}" /></a></c:otherwise></c:choose>
										</td>
									</tr>
									<tr>
										<th>连接文件</th>
										<td><c:out value="${tableDetailDTO.joinFields}" /></td>
									</tr>
									<tr>
										<th>注释</th>
										<td><c:out value="${tableDetailDTO.tableComment}"
												escapeXml="false" /></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>

		<c:if test="${fn:length(tableIndexs) > 0}">
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="x_title">
						<h2>关键字<c:if test="${ tableIndexs[0].nonUnique == 0}" >(Unique)</c:if></h2>
						<ul class="nav navbar-right panel_toolbox">
							<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>
					<div class="x_content">
						<table class="table table-striped table-bordered">
							<thead>
								<tr>
									<th>字段名称</th>
									<th>序号</th>
									<th>排序方式</th>
									<th>绝对值</th>
									<th>注释</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${tableIndexs}" var="aDto">
									<tr>
										<td><c:out value="${aDto.columnName}"></c:out></td>
										<td><c:out value="${aDto.seqInIndex}"></c:out></td>
										<td><c:out value="${aDto.collation}"></c:out></td>
										<td><c:out value="${aDto.absVal}"></c:out></td>
										<td><c:out value="${aDto.indexComment}" escapeXml="false"></c:out></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		</c:if>
		
		<c:if test="${fn:length(viewConditions) > 0}">
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="x_title">
						<h2>查询条件</h2>
						<ul class="nav navbar-right panel_toolbox">
							<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>
					<div class="x_content">
						<table class="table table-striped table-bordered">
							<thead>
								<tr>
									<th>过滤方式</th>
									<th>逻辑运算符</th>
									<th>字段名称</th>
									<th>表达式</th>
									<th>注释</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${viewConditions}" var="aDto">
									<tr>
										<td><c:out value="${aDto.conditionType}"></c:out></td>
										<td><c:out value="${aDto.operationType}"></c:out></td>
										<td><c:out value="${aDto.keyName}"></c:out></td>
										<td><c:out value="${aDto.expr}"></c:out></td>
										<td><c:out value="${aDto.conditionComment}" escapeXml="false"></c:out></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		</c:if>
		
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="x_title">
						<h2>文件字段</h2>
						<ul class="nav navbar-right panel_toolbox">
							<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>
					<div class="x_content">
						<table id="my-datatable"
							class="table table-striped table-bordered">
							<thead>
								<tr>
									<th>字段名称</th>
									<th>顺序</th>
									<th>列描述</th>
									<th>sst</th>
									<th>数据类型</th>
									<th>引用表</th>
									<th>字段别名</th>
									<th>注释</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${columns}" var="aDto">
									<tr>
										<td><c:out value="${aDto.columnName}"></c:out></td>
										<td><c:out value="${aDto.ordinalPosition}"></c:out></td>
										<td><c:out value="${aDto.colHdg}"></c:out></td>
										<td><c:out value="${aDto.sst}"></c:out></td>
										<td><c:out value="${aDto.dataType}"></c:out></td>
										<td><c:out value="${aDto.tableRef}"></c:out></td>
										<td><c:out value="${aDto.columnAlias}"></c:out></td>
										<td><c:out escapeXml="false"
												value="${aDto.columnComment}"></c:out></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
</body>
<content tag="scripts"> <script type="text/javascript">
	$(function() {
		$('#my-datatable').dataTable({
			'paginng' : true,
			'pageLength' : 100,
			'bSort': false
		});
		
		$BODY.toggleClass('nav-md nav-sm');
	});
</script> </content>
</html>

