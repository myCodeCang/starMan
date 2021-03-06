<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>团队等级管理</title>
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
		<li class="active"><a href="${ctx}/help/userTeamLevel/">代理级别列表</a></li>
		<shiro:hasPermission name="user:userTeamLevel:edit"><li><a href="${ctx}/help/userTeamLevel/form">代理级别添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="userTeamLevel" action="${ctx}/help/userTeamLevel/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>等级名称：</label>
				<form:input path="teamName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>等级代码：</label>
				<form:input path="teamCode" htmlEscape="false" maxlength="10" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>

					
				<th>编号</th>

				<th>等级代码</th>
					
				<th>等级名称</th>

				<th>分成比例</th>

				<th>备注</th>
				<shiro:hasPermission name="user:userTeamLevel:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userTeamLevel">
			<tr>

				<td><a href="${ctx}/help/userTeamLevel/form?id=${userTeamLevel.id}">
					${userTeamLevel.id}
				</a></td>
				<td>
						${userTeamLevel.teamCode}
				</td>
				<td>
					${userTeamLevel.teamName}
				</td>

				<td>
					${userTeamLevel.directEarnings}
				</td>

				<td>
						${userTeamLevel.remarks}
				</td>

				<shiro:hasPermission name="user:userTeamLevel:edit"><td>
    				<a href="${ctx}/help/userTeamLevel/form?id=${userTeamLevel.id}">修改</a>
					<a href="${ctx}/help/userTeamLevel/delete?id=${userTeamLevel.id}" onclick="return confirmx('确认要删除该团队等级吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>