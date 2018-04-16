<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>日持仓管理</title>
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
		<li class="active"><a href="${ctx}/star/everydayHold/">日持仓列表</a></li>
		<%--<shiro:hasPermission name="user:everydayHold:edit"><li><a href="${ctx}/star/everydayHold/form">日均持仓添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="everydayHold" action="${ctx}/star/everydayHold/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<%--<li><label>id：</label>--%>
				<%--<form:input path="id" htmlEscape="false" maxlength="11" class="input-medium"/>--%>
			<%--</li>--%>
			<li><label>产品代码：</label>
				<form:input path="groupId" htmlEscape="false" maxlength="11" class="input-medium"/>
			</li>
			<li><label>用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>持仓量大于：</label>
				<input name="numBegin" type="text" value="${everydayHold.numBegin}" htmlEscape="false" maxlength="100" class="input-medium"/>
			</li>
			<li><label>日期：</label>
				<input name="createDateTimeBegin" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${everydayHold.createDateTimeBegin}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -
				<input name="createDateTimeEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${everydayHold.createDateTimeEnd}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
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
					
				<th>产品代码</th>
					
				<th>用户名</th>

				<th>真实姓名</th>
					
				<%--<th>价格</th>--%>
					
				<th>持有量</th>

				<th>持仓金额</th>

				<th>创建时间</th>

				<th>备注</th>
					
				<%--<shiro:hasPermission name="user:everydayHold:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="everydayHold">
			<tr>

				<td>
					${everydayHold.id}
				</td>
				<td>
					${everydayHold.groupId}
				</td>
				<td>
					${everydayHold.userName}
				</td>
				<td>
						${everydayHold.trueName}
				</td>
				<%--<td>--%>
					<%--${everydayHold.money}--%>
				<%--</td>--%>
				<td>
						${everydayHold.num}
				</td>
				<td>
					<fmt:formatNumber type="number" value="${everydayHold.money*everydayHold.num}" pattern="0.00" maxFractionDigits="2"/>
				</td>
				<td>
					<fmt:formatDate value="${everydayHold.createDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${everydayHold.remarks}
				</td>
				<%--<shiro:hasPermission name="user:everydayHold:edit"><td>--%>
    				<%--<a href="${ctx}/star/everydayHold/form?id=${everydayHold.id}">修改</a>--%>
					<%--<a href="${ctx}/star/everydayHold/delete?id=${everydayHold.id}" onclick="return confirmx('确认要删除该日均持仓吗？', this.href)">删除</a>--%>
				<%--</td></shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>