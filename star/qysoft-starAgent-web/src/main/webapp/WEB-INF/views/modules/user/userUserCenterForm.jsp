<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>会员管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/user/userUserinfo/list?isUsercenter=1">承销商列表</a></li>
		<shiro:hasPermission name="user:userCenter:edit"><li><a href="${ctx}/user/userUserinfo/userCenterform">指定承销商</a></li></shiro:hasPermission>
	</ul>
	<form:form id="inputForm" modelAttribute="userUserinfo" action="${ctx}/user/userUserinfo/updateUserTypeByName" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		
		<div class="control-group">
			<label class="control-label">用户帐号：</label>
			<div class="controls">
				<sys:userinfoSelect id="userName" title="用户选择" cssClass="required"  value="${userName}" isUsercenter="0"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<%--<div class="control-group">--%>
			<%--<label class="control-label">指定级别：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:select path="userType" class="input-xlarge required">--%>
					<%--<form:option value="" label="请选择"/>--%>
					<%--<form:options items="${userTeamLevelList}" itemLabel="teamName" itemValue="teamCode" htmlEscape="false" />--%>
				<%--</form:select>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>

		<!-- <div class="control-group">
			<label class="control-label">报单中心等级: </label>
			<div class="controls">
				<%--<form:select path="usercenterLevel" class="input-xlarge required">--%>
					<%--<form:option value="1" label="一级报单中心"/>--%>
					<%--<form:option value="2" label="二级报单中心"/>--%>
					<%--<form:option value="3" label="三级报单中心"/>--%>
				<%--</form:select>--%>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div> -->
		<%--<div class="control-group">--%>
			<%--<label class="control-label">报单中心地址: </label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="usercenterAddress" htmlEscape="false" maxlength="60" class="input-xlarge required" type="text"/>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		
		
		
		<div class="form-actions">
			<shiro:hasPermission name="user:userUserinfo:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>