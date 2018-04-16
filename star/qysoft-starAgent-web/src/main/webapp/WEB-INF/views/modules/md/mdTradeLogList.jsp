<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>撮合记录管理</title>
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
		<li class="active"><a href="${ctx}/md/mdTradeLog/">撮合记录列表</a></li>
		<%--<shiro:hasPermission name="user:mdTradeLog:edit">--%>
			<%--<li><a href="${ctx}/md/mdTradeLog/form">撮合记录添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="mdTradeLog" action="${ctx}/md/mdTradeLog/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

			<li><label>交易用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>明星编号：</label>
				<form:input path="groupId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>交易方向：</label>
				<form:select path="type" class="input-medium">
					<form:option value="0" label="全部"/>
					<form:options items="${fns:getDictList('md_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>创建时间：</label>
				<input name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${mdTradeLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
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
					
				<%--<th>交易用户id</th>--%>
					
				<th>交易用户名</th>

				<th>真实姓名</th>
					
				<th>明星编号</th>
					
				<th>交易方向</th>
					
				<th>交易数量</th>
					
				<th>单价金额</th>

				<th>交易总额</th>
					
				<th>手续费</th>
					
				<th>描述信息</th>
					
				<th>创建时间</th>

				<%--<shiro:hasPermission name="user:mdTradeLog:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mdTradeLog">
			<tr>

				<td>
					${mdTradeLog.id}
				</td>
				<%--<td>--%>
					<%--${mdTradeLog.userId}--%>
				<%--</td>--%>
				<td>
					${mdTradeLog.userName}
				</td>
				<td>
						${mdTradeLog.trueName}
				</td>
				<td>
					${mdTradeLog.groupId}
				</td>
				<td>
					${fns:getDictLabel(mdTradeLog.type, 'md_type', '')}
				</td>
				<td>
					${mdTradeLog.amount}
				</td>
				<td>
					${mdTradeLog.price}
				</td>
				<td>
						${mdTradeLog.price*mdTradeLog.amount}
				</td>
				<td>
					${mdTradeLog.profit}
				</td>
				<td>
					${mdTradeLog.message}
				</td>
				<td>
					<fmt:formatDate value="${mdTradeLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<%--<shiro:hasPermission name="user:mdTradeLog:edit"><td>--%>
    				<%--<a href="${ctx}/md/mdTradeLog/form?id=${mdTradeLog.id}">修改</a>--%>
					<%--<a href="${ctx}/md/mdTradeLog/delete?id=${mdTradeLog.id}" onclick="return confirmx('确认要删除该撮合记录吗？', this.href)">删除</a>--%>
				<%--</td></shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>