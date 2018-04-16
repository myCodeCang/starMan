<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>明星产品管理</title>
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
		<li><a href="${ctx}/star/starProject/">明星产品列表</a></li>
		<li class="active"><a href="${ctx}/star/starProject/form?id=${starProject.id}">明星产品<shiro:hasPermission name="user:starProject:edit">${not empty starProject.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="user:starProject:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="starProject" action="${ctx}/star/starProject/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">明星名称：</label>
			<div class="controls">
				<sys:baseGoodsGroup id="starName" title="选择产品" cssClass="required" labelValue="${starProject.groupId}"  value="${starProject.starName}" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" type="text" class="required "/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态：</label>
			<div class="controls">
				<form:radiobuttons path="status" items="${fns:getDictList('qy_trans_group')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品图片：</label>
			<div class="controls">
				<form:hidden id="image" path="image" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="image" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
				<span class="help-inline"><font color="red">*(建议尺寸1080*540)</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">所需时间：</label>
			<div class="controls">
				<form:input path="time" htmlEscape="false" type="number" class="required "/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品介绍：</label>
			<div class="controls">
				<form:textarea id="detail" htmlEscape="false" path="detail" rows="4" maxlength="500" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="user:starProject:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>