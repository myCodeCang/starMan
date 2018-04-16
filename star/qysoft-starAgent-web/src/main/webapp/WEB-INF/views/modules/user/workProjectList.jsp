<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/user/workProject/">升级商品列表</a></li>
		<shiro:hasPermission name="user:workProject:edit"><li><a href="${ctx}/user/workProject/form">升级商品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="workProject" action="${ctx}/user/workProject/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>商品名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>

				<th>商品编号</th>

				<th>商品名称</th>
					
				<th>商品图片</th>
					
				<th>商品单价</th>

				<th>创建时间</th>
					
					
				<%--<th>更新时间</th>--%>
					
					
				<shiro:hasPermission name="user:workProject:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="workProject">
			<tr>
				<td>
					${workProject.id}
				</td>
				<td><a href="${ctx}/user/workProject/form?id=${workProject.id}">
					${workProject.name}
				</a></td>
				<td>
					<img src="${workProject.picture}" style="display: block;margin:0 auto;width:200px;"/>
				</td>
				<td>
					${workProject.money}
				</td>
				<td>
					<fmt:formatDate value="${workProject.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<td>--%>
					<%--<fmt:formatDate value="${workProject.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
				<%--</td>--%>
				<shiro:hasPermission name="user:workProject:edit"><td>
    				<a href="${ctx}/user/workProject/form?id=${workProject.id}" class="layui-btn layui-btn-mini layui-btn-normal">修改</a>
					<a href="${ctx}/user/workProject/delete?id=${workProject.id}" class="layui-btn layui-btn-mini layui-btn-normal" onclick="return confirmx('确认要删除该项目吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>