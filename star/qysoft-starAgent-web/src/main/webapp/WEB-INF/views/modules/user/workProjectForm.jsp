<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目管理</title>
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
		<li><a href="${ctx}/user/workProject/">升级商品列表</a></li>
		<li class="active"><a href="${ctx}/user/workProject/form?id=${workProject.id}">商品<shiro:hasPermission name="user:workProject:edit">${not empty workProject.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="user:workProject:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="workProject" action="${ctx}/user/workProject/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">商品名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>

		</div>

		<div class="control-group">
			<label class="control-label">商品简介：</label>
			<div class="controls">
				<form:input path="message" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>

		</div>
		<div class="control-group">
			<label class="control-label">商品单价：</label>
			<div class="controls">
				<input  type="number" name="money" value="${workProject.money}" htmlEscape="false" maxlength="255" class="input-xlarge  required" />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>

		</div>
		<div class="control-group">
			<label class="control-label">商品缩略图：</label>
			<div class="controls">
				<form:hidden id="picture" path="picture" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="picture" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">轮播图1：</label>
			<div class="controls">
				<form:hidden id="image1" path="image1" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="image1" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">轮播图2：</label>
			<div class="controls">
				<form:hidden id="image2" path="image2" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="image2" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">轮播图3：</label>
			<div class="controls">
				<form:hidden id="image3" path="image3" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="image3" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
			</div>
		</div>
		<input type="hidden" name="statue" value="1"/>
		<div class="control-group">
			<label class="control-label">详情：</label>
			<div class="controls">
				<form:textarea id="detail" htmlEscape="false" path="detail" rows="4" maxlength="200" class="input-xxlarge"/>
				<sys:ckeditor replace="detail" uploadPath="/cms/article" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="user:workProject:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>