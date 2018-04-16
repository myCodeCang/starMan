<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>明星管理</title>
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
		<li><a href="${ctx}/star/baseGoodsGroup/">明星列表</a></li>
		<li class="active"><a href="${ctx}/star/baseGoodsGroup/form?id=${baseGoodsGroup.id}">明星<shiro:hasPermission name="star:baseGoodsGroup:edit">${not empty baseGoodsGroup.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="star:baseGoodsGroup:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="baseGoodsGroup" action="${ctx}/star/baseGoodsGroup/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<blockquote class="layui-elem-quote">明星基本信息配置</blockquote>
		<div class="control-group">
			<label class="control-label">所属分类：</label>
			<div class="controls">
				<form:select path="categoryId" class="input-xlarge required">
					<form:option value="" label="请选择"/>
					<form:options items="${categoryList}" itemLabel="classifyname" itemValue="id" htmlEscape="false" />
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">明星姓名：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">明星描述：</label>
			<div class="controls">
				<textarea name="description" style="width: 500px;" rows="5">${baseGoodsGroup.description}</textarea>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">明星缩略图：</label>
			<div class="controls">
				<form:hidden id="thumb" path="thumb" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="thumb" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
				<span class="help-inline"><font color="red">*(每张图片大小保持一致,建议尺寸300*300)</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">首页展示图：</label>
			<div class="controls">
				<form:hidden id="image" path="image" htmlEscape="false" maxlength="255" class="required"/>
				<sys:ckfinder input="image" type="images" uploadPath="/photo" selectMultiple="false" maxWidth="100" maxHeight="100"/>
				<span class="help-inline"><font color="red">*(建议尺寸1080*1920)</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">明星详情：</label>
			<div class="controls">
				<form:textarea id="detail" htmlEscape="false" path="detail" rows="4" maxlength="200" class="input-xxlarge"/>
				<sys:ckeditor replace="detail" uploadPath="/trans/transGoodsGroup" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否首页展示：</label>
			<div class="controls">
				<form:radiobuttons path="isShow" items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" class=""/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">交易规则：</label>
			<div class="controls">
				<form:textarea id="transRules" htmlEscape="false" path="transRules" rows="4" maxlength="200" class="input-xxlarge"/>
				<sys:ckeditor replace="transRules" uploadPath="/trans/transGoodsGroup" />
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="user:baseGoodsGroup:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>