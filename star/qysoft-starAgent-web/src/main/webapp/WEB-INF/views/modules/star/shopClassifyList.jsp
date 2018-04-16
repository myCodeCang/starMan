<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分类管理</title>
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
		<li class="active"><a href="${ctx}/star/shopClassify/">分类列表</a></li>
		<shiro:hasPermission name="user:shopClassify:edit"><li><a href="${ctx}/star/shopClassify/form">分类添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="shopClassify" action="${ctx}/star/shopClassify/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>分类名称：</label>
				<form:input path="classifyname" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>

					
				<th>id</th>
					
				<th>分类名称</th>
					
				<%--<th>分类图标</th>--%>
					
				<%--<th>是否使用</th>--%>
					
					
				<th>创建时间</th>
					
					
					
				<th>分类注释</th>
					
				<shiro:hasPermission name="user:shopClassify:edit"><th >操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="shopClassify">
			<tr>

				<td>
					${shopClassify.id}
				</td>
				<td>
					${shopClassify.classifyname}
				</td>
				<%--<td>--%>
					<%--<img src="${shopClassify.classifyimg}">--%>
				<%--</td>--%>
				<%--<td>--%>
					<%--${shopClassify.isuser}--%>
				<%--</td>--%>
				<td>
					<fmt:formatDate value="${shopClassify.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${shopClassify.remarks}
				</td>
				<shiro:hasPermission name="user:shopClassify:edit"><td>
    				<a href="${ctx}/star/shopClassify/form?id=${shopClassify.id}" style="display:inline-block;float: left;" class="layui-btn layui-btn-mini layui-btn-normal">修改</a>
					<a href="${ctx}/star/shopClassify/delete?id=${shopClassify.id}" style="display:inline-block;float: left;" class="layui-btn layui-btn-mini layui-btn-normal" onclick="return confirmx('确认要删除该分类吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>