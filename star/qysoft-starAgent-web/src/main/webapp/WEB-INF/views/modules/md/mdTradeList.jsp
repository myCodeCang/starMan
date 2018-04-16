<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>交易管理</title>
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
		<li class="active"><a href="${ctx}/md/mdTrade/">交易列表</a></li>
		<%--<shiro:hasPermission name="user:mdTrade:edit"><li><a href="${ctx}/md/mdTrade/form">交易添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="mdTrade" action="${ctx}/md/mdTrade/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>明星编号：</label>
				<form:input path="groupId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>交易类型：</label>
				<form:select path="type" class="input-medium">
					<form:option value="0" label="全部"/>
					<form:options items="${fns:getDictList('md_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>状态</label>
				<form:select path="state" class="input-medium">
					<form:option value="0" label="全部"/>
					<form:options items="${fns:getDictList('md_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
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
					
				<th>明星编号</th>
					
				<%--<th>用户id</th>--%>
					
				<th>用户名</th>

				<th>真实姓名</th>
					
				<th>单价</th>
					
				<th>发布数量</th>
					
				<th>剩余数量</th>
					
				<th>交易类型</th>
					
				<%--<th>消费金额</th>--%>
					
				<th>状态</th>
					
				<th>发布日期</th>
					
					
					
				<%--<shiro:hasPermission name="user:mdTrade:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mdTrade">
			<tr>

				<td>
					${mdTrade.id}
				</td>
				<td>
					${mdTrade.groupId}
				</td>
				<%--<td>--%>
					<%--${mdTrade.userId}--%>
				<%--</td>--%>
				<td>
					${mdTrade.userName}
				</td>
				<td>
						${mdTrade.trueName}
				</td>
				<td>
					${mdTrade.price}
				</td>
				<td>
					${mdTrade.publishNum}
				</td>
				<td>
					${mdTrade.remainNum}
				</td>
				<td>
					${fns:getDictLabel(mdTrade.type, 'md_type', '')}
				</td>
				<%--<td>--%>
					<%--${mdTrade.bond+mdTrade.profit}--%>
				<%--</td>--%>
				<td>
					${fns:getDictLabel(mdTrade.state, 'md_status', '')}
				</td>
				<td>
					<fmt:formatDate value="${mdTrade.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<shiro:hasPermission name="user:mdTrade:edit"><td>--%>
    				<%--<a href="${ctx}/md/mdTrade/form?id=${mdTrade.id}">修改</a>--%>
					<%--<a href="${ctx}/md/mdTrade/delete?id=${mdTrade.id}" onclick="return confirmx('确认要删除该交易吗？', this.href)">删除</a>--%>
				<%--</td></shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>