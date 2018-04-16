\<%@ page contentType="text/html;charset=UTF-8" %>
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
		<li class="active"><a href="${ctx}/trans/transGoods/publish">承销商发货</a></li>
	</ul>

	<form:form id="inputForm" modelAttribute="transGoods" action="${ctx}/trans/transGoods/saveTransGoods" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">承销商：</label>
			<div class="controls">
				<sys:userinfoSelect id="userName" title="用户选择" cssClass="required"  value="${transGoods.userName}" isUsercenter="1"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">产品名称：</label>
			<div class="controls">
				<sys:baseGoodsGroup id="groupName" value="${transGoods.name}" labelValue="${transGoods.groupId}" title="选择产品" cssClass="required"  />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">价格(单价)：</label>
			<div class="controls">
				<input type="number" min="0.01" name="buyMoney" htmlEscape="false" class="input-xlarge " value="${transGoods.buyMoney}"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数量(秒数)：</label>
			<div class="controls">
				<input type="number" min="1" htmlEscape="false" name="num" maxlength="11" class="input-xlarge " value="${transGoods.num == 0?"":transGoods.num}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="user:transGoods:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>