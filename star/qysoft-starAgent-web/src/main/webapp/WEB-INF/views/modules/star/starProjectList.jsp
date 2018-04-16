<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>明星产品管理</title>
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
		<li class="active"><a href="${ctx}/star/starProject/">明星产品列表</a></li>
		<shiro:hasPermission name="user:starProject:edit"><li><a href="${ctx}/star/starProject/form">明星产品添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="starProject" action="${ctx}/star/starProject/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>编号：</label>
				<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>明星名称：</label>
				<form:input path="starName" htmlEscape="false" maxlength="11" class="input-medium"/>
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
					
				<th>明星名称</th>

				<th>产品名称</th>

				<th>是否上架</th>
					
				<th>大图</th>
					
				<th>所需时间</th>

				<th>产品介绍</th>
					
				<th>创建时间</th>
					
					
					
					
				<shiro:hasPermission name="user:starProject:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="starProject">
			<tr>

				<td>
					${starProject.id}
				</td>
				<td>
					${starProject.starName}
				</td>
				<td>
						${starProject.name}
				</td>
				<td>
						${(starProject.status == '1') ? "<span style='color: green'>上架</span>" : "<span style='color: red'>下架</span>"}
				</td>
				<td>
					<img src="${starProject.image}" style="height:120px;">
				</td>
				<td>
					${starProject.time}
				</td>
				<td>
						${starProject.detail}
				</td>
				<td>
					<fmt:formatDate value="${starProject.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="user:starProject:edit"><td>
    				<a href="${ctx}/star/starProject/form?id=${starProject.id}">修改</a>
					<a href="${ctx}/star/starProject/delete?id=${starProject.id}" onclick="return confirmx('确认要删除该明星产品吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>