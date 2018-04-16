
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>承销商管理</title>
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
		<li class="active"><a href="${ctx}/user/userUserinfo/list?isUsercenter=1">经纪人列表</a></li>
		<shiro:hasPermission name="user:userCenter:edit"><li><a href="${ctx}/user/userUserinfo/userAgentform">指定经纪人</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="userUserinfo" action="${ctx}/user/userUserinfo/userAgent" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>账号：</label>
				<form:input path="userName" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>手机号：</label>
				<form:input path="mobile" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>
			<!-- <li><label>等级：</label>
				<form:input path="usercenterLevel" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li> -->
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>经纪人编号</th>
				<th>经纪人用户名</th>
				<th>号段</th>
				<th>手机号</th>
				<th>真实姓名</th>
				<th>昵称</th>
				<th>备注</th>
				<shiro:hasPermission name="user:userCenter:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="userUserinfo">
			<tr>
				<td>
					${userUserinfo.id}
				</td>
				<td>
					${userUserinfo.userName}
				</td>
				<td>
						${userUserinfo.shopId}
				</td>
				<td>
					${userUserinfo.mobile}
				</td>
				<td>
					${userUserinfo.trueName}
				</td>
				<td>
					${userUserinfo.userNicename}
				</td>
				<td>
					${userUserinfo.remarks}
				</td>
				<shiro:hasPermission name="user:userCenter:edit"><td>
    				
					<c:if test="${userUserinfo.isShop==1}">
					<a href="${ctx}/user/userUserinfo/updateIsShop?userName=${userUserinfo.userName}&isShop=0" onclick="return confirmx('确认要撤销该承销商资格吗？', this.href)">撤销</a>
					</c:if>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
	
	<script type="text/javascript">
	
	 layui.use('qyframe', function() {
		var qyframe = layui.qyframe;
	 }); 
	 
	</script>
</body>
</html>