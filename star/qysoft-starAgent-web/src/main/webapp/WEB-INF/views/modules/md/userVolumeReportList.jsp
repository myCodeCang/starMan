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
		<li class="active"><a href="${ctx}/md/mdTradeLog/userVolumeReport/">会员成交额统计</a></li>
		<%--<shiro:hasPermission name="user:mdTradeLog:edit">--%>
			<%--<li><a href="${ctx}/md/mdTradeLog/form">撮合记录添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="mdTradeLog" action="${ctx}/md/mdTradeLog/userVolumeReport/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">

			<li><label>用户名：</label>
				<form:input path="userName" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>日期：</label>
				<input name="createDateBegin" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${createDateBegin}" pattern="yyyy-MM-dd"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/> -
				<input name="createDateEnd" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					   value="<fmt:formatDate value="${createDateEnd}" pattern="yyyy-MM-dd"/>"
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

				<th>用户名</th>

				<th>真实姓名</th>

				<th>成交额</th>

				<th>代理等级</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="mdTradeLog">
			<tr>
				<td>
						${mdTradeLog.userName}
				</td>
				<td>
						${mdTradeLog.trueName}
				</td>
				<td>
					<fmt:formatNumber type="number" value="${mdTradeLog.money6}" pattern="0.00" maxFractionDigits="2"/>
				</td>
				<td>
						${mdTradeLog.userType==0?"普通会员":mdTradeLog.userType==1?"二级代理":mdTradeLog.userType==2?"一级代理":"运营中心"}
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>