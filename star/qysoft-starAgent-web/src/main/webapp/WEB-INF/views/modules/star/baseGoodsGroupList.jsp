<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>明星管理</title>
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
		<li class="active"><a href="${ctx}/star/baseGoodsGroup/">明星列表</a></li>
		<shiro:hasPermission name="user:baseGoodsGroup:edit"><li><a href="${ctx}/star/baseGoodsGroup/form">明星添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="baseGoodsGroup" action="${ctx}/star/baseGoodsGroup/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>姓名：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>描述：</label>
				<form:input path="description" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>明星编号</th>
				<th>明星姓名</th>
				<th>明星简介</th>
				<th>明星缩略图</th>
				<th>是否展示</th>
				<th>创建时间</th>
				<shiro:hasPermission name="user:baseGoodsGroup:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="baseGoodsGroup">
			<tr>

				<td>
						${baseGoodsGroup.id}
				</td>
				<td>
					${baseGoodsGroup.name}
				</td>
				<td>
						${baseGoodsGroup.description}
				</td>
				<td>
						<img src="${baseGoodsGroup.thumb}" width="50" height="50"/>
				</td>
				<td>
						${fns:getDictLabel(baseGoodsGroup.isShow, 'yes_no', '')}
				</td>
				<td>
					<fmt:formatDate value="${baseGoodsGroup.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<shiro:hasPermission name="user:baseGoodsGroup:edit"><td>
    				<a href="${ctx}/star/baseGoodsGroup/form?id=${baseGoodsGroup.id}">修改</a>
					<a href="${ctx}/star/baseGoodsGroup/delete?id=${baseGoodsGroup.id}" onclick="return confirmx('确认要删除该明星产品吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>