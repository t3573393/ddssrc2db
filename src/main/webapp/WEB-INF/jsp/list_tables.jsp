<%@ page contentType="text/html;charset=UTF-8" language="java"
	session="false"%>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<html lang="en">
<head>
<title>文件查询</title>
</head>

<body>
	<div class="">
		<div class="page-title">
			<div class="title_left">
				<h3>文件查询</h3>
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
						<form id="search-tables-form" data-parsley-validate
							class="form-horizontal form-label-left"
							action="<c:url value="/search_tables" />" name="searchDto">
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
									<th>名称</th>
									<th>类型</th>
									<th>记录格式名称</th>
									<th>引用文件</th>
									<th>连接文件</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${resultDto}" var="aDto">
									<tr>
										<td><a
											href="<c:url value="/load_table_detail"/>?id=<c:out value="${aDto.id}" />" target="_blank"><span class="glyphicon glyphicon-file" aria-hidden="true"></span><c:out value="${aDto.tableName}"></c:out></a></td>
										<td><c:out value="${aDto.tableType}"></c:out></td>
										<td><c:out value="${aDto.recordFormatName}"></c:out></td>
										<td><c:out value="${aDto.refTables}"></c:out></td>
										<td><c:out value="${aDto.joinFields}"></c:out></td>
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
		   
		   $('#list_tables_menu_item').addClass('current-page');
	   });
	</script>
</content>
</html>

