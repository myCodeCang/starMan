<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户交易订单管理</title>
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
		<li><a href="${ctx}/trans/transBuyLog/">用户交易订单列表</a></li>
		<li class="active"><a href="${ctx}/trans/transBuyLog/form?id=${transBuyLog.id}">用户交易订单<shiro:hasPermission name="user:transBuyLog:edit">${not empty transBuyLog.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="user:transBuyLog:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="transBuyLog" action="${ctx}/trans/transBuyLog/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">艺术品名称：</label>
			<div class="controls">
				<sys:transGoodsGroup id="groupName" value="${transApply.transGoodsGroup.name}" labelValue="${transApply.groupId}" title="艺术品" cssClass="required"  />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">用户名：</label>
			<div class="controls">
				<input type="text" name="userName" value="admin" htmlEscape="false" maxlength="255" class="input-xlarge"  readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易对方用户名：</label>
			<div class="controls">
				<input type="text" name="transUserName" value="admin" htmlEscape="false" maxlength="255" class="input-xlarge" readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易数量：</label>
			<div class="controls">
				<form:input path="num" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">单价金额：</label>
			<div class="controls">
				<form:input path="money" htmlEscape="false" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易类型：</label>
			<div class="controls">
				<input type="hidden" name="type"  value="4"/>
				<input type="text"  htmlEscape="false" maxlength="255" class="input-xlarge" readonly="true" value="后台交易"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易时间：</label>
			<div class="controls">
				<input name="createDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required "
					   value="<fmt:formatDate value="${transApply.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">描述信息：</label>
			<div class="controls">
				<form:input path="message" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="user:transBuyLog:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>