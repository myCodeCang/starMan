<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>拍卖管理</title>
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
		<li class="active"><a href="${ctx}/star/auctionGuarantee/">拍卖列表</a></li>
		<shiro:hasPermission name="user:auctionGuarantee:edit"><li><a href="${ctx}/star/auctionGuarantee/form">拍卖添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="auctionGuarantee" action="${ctx}/star/auctionGuarantee/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<%--<li><label>user_name：</label>--%>
				<%--<form:input path="userName" htmlEscape="false" maxlength="64" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li><label>status：</label>--%>
				<%--<form:input path="status" htmlEscape="false" maxlength="1" class="input-medium"/>--%>
			<%--</li>--%>
			<%--<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>--%>
			<%--<li class="clearfix"></li>--%>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>

					
				<%--<th>id</th>--%>
					
				<th>拍卖编号</th>
					
				<th>机构编号</th>
					
				<th>保证金金额</th>
					
				<th>是否退回</th>
					
				<%--<th>create_by</th>--%>
					
				<th>交付时间</th>
					
				<%--<th>update_by</th>--%>
					<%----%>
				<%--<th>update_date</th>--%>
					<%----%>
				<%--<th>remarks</th>--%>
					<%----%>
				<%--<shiro:hasPermission name="user:auctionGuarantee:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="auctionGuarantee">
			<tr>

				<%--<td><a href="${ctx}/star/auctionGuarantee/form?id=${auctionGuarantee.id}">--%>
					<%--${auctionGuarantee.id}--%>
				<%--</a></td>--%>
				<td>
					${auctionGuarantee.auctionId}
				</td>
				<td>
					${auctionGuarantee.userName}
				</td>
				<td>
					${auctionGuarantee.money}
				</td>
				<td>
					${auctionGuarantee.status=="0"?'未退还':'已退还'}
				</td>
				<%--<td>--%>
					<%--${auctionGuarantee.createBy.id}--%>
				<%--</td>--%>
				<td>
					<fmt:formatDate value="${auctionGuarantee.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<td>--%>
					<%--${auctionGuarantee.updateBy.id}--%>
				<%--</td>--%>
				<%--<td>--%>
					<%--<fmt:formatDate value="${auctionGuarantee.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>--%>
				<%--</td>--%>
				<%--<td>--%>
					<%--${auctionGuarantee.remarks}--%>
				<%--</td>--%>
				<%--<shiro:hasPermission name="user:auctionGuarantee:edit"><td>--%>
    				<%--<a href="${ctx}/star/auctionGuarantee/form?id=${auctionGuarantee.id}">修改</a>--%>
					<%--<a href="${ctx}/star/auctionGuarantee/delete?id=${auctionGuarantee.id}" onclick="return confirmx('确认要删除该拍卖吗？', this.href)">删除</a>--%>
				<%--</td></shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>