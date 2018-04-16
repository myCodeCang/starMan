<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收藏管理</title>
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
		<li class="active"><a href="${ctx}/star/userColection/">收藏列表</a></li>
		<shiro:hasPermission name="star:userColection:edit"><li><a href="${ctx}/star/userColection/form">收藏添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="userColection" action="${ctx}/star/userColection/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>user_name：</label>
				<form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>

					
					
				<th>user_name</th>
					
				<th>group_id</th>
					
				<th>message</th>
					
				<th>create_by</th>
					
				<th>create_date</th>
					
					
				<th>update_date</th>
					
				<th>remarks</th>
					
				<shiro:hasPermission name="star:userColection:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userColection">
			<tr>

				<td><a href="${ctx}/star/userColection/form?id=${userColection.id}">
					${userColection.userName}
				</a></td>
				<td>
					${userColection.groupId}
				</td>
				<td>
					${userColection.message}
				</td>
				<td>
					${userColection.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${userColection.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${userColection.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${userColection.remarks}
				</td>
				<shiro:hasPermission name="star:userColection:edit"><td>
    				<a href="${ctx}/star/userColection/form?id=${userColection.id}">修改</a>
					<a href="${ctx}/star/userColection/delete?id=${userColection.id}" onclick="return confirmx('确认要删除该收藏吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>