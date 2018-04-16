<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>艺术品管理</title>
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
	<li><a href="${ctx}/trans/transBuyDayTrend/">艺术品列表</a></li>
	<li class="active"><a href="${ctx}/trans/transBuyDayTrend/form?id=${transBuyDayTrend.id}">艺术品<shiro:hasPermission name="user:transBuyDayTrend:edit">${not empty transBuyDayTrend.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="trans:transBuyDayTrend:edit"></shiro:lacksPermission></a></li>
</ul><br/>
<form:form id="inputForm" modelAttribute="transBuyDayTrend" action="${ctx}/trans/transBuyDayTrend/save" method="post" class="form-horizontal">
	<form:hidden path="id"/>
	<sys:message content="${message}"/>
	<div class="control-group">
		<label class="control-label">当前价：</label>
		<div class="controls">
			<form:input path="nowMoney" htmlEscape="false" maxlength="500" class="required" type="number"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">开盘价：</label>
		<div class="controls">
			<form:input path="startMoney" htmlEscape="false" maxlength="500" class="required" type="number"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">收盘价：</label>
		<div class="controls">
			<form:input path="endMoney" htmlEscape="false" maxlength="500" class="required" type="number"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">最高价：</label>
		<div class="controls">
			<form:input path="higMoney" htmlEscape="false" maxlength="500" class="required" type="number"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">最低价：</label>
		<div class="controls">
			<form:input path="lowMoney" htmlEscape="false" maxlength="500" class="required" type="number"/>
			<span class="help-inline"><font color="red">*</font> </span>
		</div>
	</div>
	<div class="form-actions">
		<shiro:hasPermission name="user:transBuyDayTrend:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
</form:form>
</body>
</html>