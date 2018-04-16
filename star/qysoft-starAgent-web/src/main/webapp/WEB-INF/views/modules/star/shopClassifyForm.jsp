<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分类管理</title>
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
		<li><a href="${ctx}/star/shopClassify/">分类列表</a></li>
		<li class="active"><a href="${ctx}/star/shopClassify/form?id=${shopClassify.id}">分类<shiro:hasPermission name="extra_user:shopClassify:edit">${not empty shopClassify.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="extra_user:shopClassify:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="shopClassify" action="${ctx}/star/shopClassify/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">分类名称：</label>
			<div class="controls">
				<form:input path="classifyname" htmlEscape="false" maxlength="255"  class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">分类图标:</label>--%>
			<%--<div class="controls">--%>
				<%--<form:hidden id="classifyimg" path="classifyimg" htmlEscape="false" maxlength="255" class="required"/>--%>
				<%--<sys:ckfinder input="classifyimg" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100" />--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">是否使用:</label>--%>
			<%--<div class="controls">--%>
				<%--<form:radiobuttons path="isuser" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>--%>
				<%--<span class="help-inline"></span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="control-group">
			<label class="control-label">分类描述：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="user:shopClassify:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>