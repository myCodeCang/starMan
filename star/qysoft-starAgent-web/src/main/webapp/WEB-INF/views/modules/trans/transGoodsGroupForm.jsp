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
		<li><a href="${ctx}/trans/transGoodsGroup/">艺术品列表</a></li>
		<li class="active"><a href="${ctx}/trans/transGoodsGroup/form?id=${transGoodsGroup.id}">艺术品<shiro:hasPermission name="user:transGoodsGroup:edit">${not empty transGoodsGroup.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="trans:transGoodsGroup:edit"></shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="transGoodsGroup" action="${ctx}/trans/transGoodsGroup/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">艺术品名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">上传艺术品:</label>
			<div class="controls">
				<form:hidden id="nameImage" path="photo" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="nameImage" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">标题：</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="500" class="required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">初始价格：</label>
			<div class="controls">
				<form:input path="startMoney" htmlEscape="false" maxlength="500" class="required" type="number"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">状态</label>
			<div class="controls">
				<form:radiobuttons path="status" items="${fns:getDictList('qy_trans_group')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">详情：</label>
			<div class="controls">
				<form:textarea id="detail" htmlEscape="false" path="detail" rows="4" maxlength="200" class="input-xxlarge"/>
				<sys:ckeditor replace="detail" uploadPath="/trans/transGoodsGroup" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="user:transGoodsGroup:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>