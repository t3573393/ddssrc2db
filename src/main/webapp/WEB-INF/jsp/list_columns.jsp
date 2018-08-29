<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html lang="en">
<head>
<title>文件字段查询</title>
</head>

<body>
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>文件字段查询</h3>
			</div>
		</div>
		<div class="clearfix"></div>
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
						<br />
						<form id="search-columns-form" data-parsley-validate
							class="form-horizontal form-label-left"
							action="<c:url value="/search_columns" />" name="searchDto">
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="tableName">文件名称 </label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="tableName" name="tableName"
										class="form-control col-md-7 col-xs-12"
										value="${searchDto.tableName}" />
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="tableType">文件类型 </label>
								<div class="col-md-2 col-sm-2 col-xs-12">
									<select class="form-control" name="tableType"
										class="form-control col-md-2 col-xs-12">
										<option value="PF"
											<c:if test="${searchDto.tableType ==  'PF'}">selected="selected"</c:if>>物理文件</option>
										<option value="LF"
											<c:if test="${searchDto.tableType ==  'LF'}">selected="selected"</c:if>>逻辑文件</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="control-label col-md-3 col-sm-3 col-xs-12"
									for="tableName">字段名称 </label>
								<div class="col-md-6 col-sm-6 col-xs-12">
									<input type="text" id="columnName" name="columnName"
										class="form-control col-md-7 col-xs-12"
										value="${searchDto.columnName}" />
								</div>
							</div>
							<div class="ln_solid"></div>
							<div class="form-group">
								<div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
									<button type="submit" class="btn btn-success">查询</button>
									<button class="btn btn-primary" type="reset">重置</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>


		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="x_panel">
					<div class="x_title">
						<h2>查询结果</h2>
						<ul class="nav navbar-right panel_toolbox">
							<li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
							</li>
						</ul>
						<div class="clearfix"></div>
					</div>
					<div class="x_content">
						<table id="my-datatable" class="table table-striped table-bordered">
							<thead>
								<tr>
								<c:if test="${searchDto.tableName != null || searchDto == null }">
									<th>名称</th>
									<th>类型</th>
								</c:if>
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
								<c:forEach items="${resultDto}" var="aDto">
									<tr>
									<c:if test="${searchDto.tableName != null || searchDto == null}">										
										<td>
										<c:choose>
											<c:when test="${aDto.tableId !=null }">
											<a href="<c:url value="/load_table_detail"/>?id=<c:out value="${aDto.tableId}" />" target="_blank"><span class="glyphicon glyphicon-file" aria-hidden="true"></span><c:out value="${aDto.tableName}" /> </a></c:when>
											<c:otherwise><c:out value="${aDto.tableName}"></c:out></c:otherwise>
										</c:choose>
										</td>
										<td><c:out value="${aDto.tableType}"></c:out></td>
									</c:if>
										<td><c:out value="${aDto.columnName}"></c:out></td>
										<td><c:out value="${aDto.ordinalPosition}"></c:out></td>
										<td><c:out value="${aDto.colHdg}"></c:out></td>
										<td><c:out value="${aDto.sst}"></c:out></td>
										<td><c:out value="${aDto.dataType}"></c:out></td>
										<td><c:out value="${aDto.tableRef}"></c:out></td>
										<td><c:out value="${aDto.columnAlias}"></c:out></td>
										<td><c:out escapeXml="false" value="${aDto.columnComment}"></c:out></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>

		</div>
</body>
<content tag="scripts" >
	<script type="text/javascript">
	   $(function(){
		   $('#my-datatable').dataTable({
				'paginng': true,
				'pageLength':50,
				'bSort': false
			});
		   
		   $('#list_columns_menu_item').addClass('current-page');
	   });
	</script>
</content>
</html>

