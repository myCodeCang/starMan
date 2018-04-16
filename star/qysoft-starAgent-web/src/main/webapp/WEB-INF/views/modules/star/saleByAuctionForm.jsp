<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>拍卖产品管理</title>
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
		<li><a href="${ctx}/star/saleByAuction/">拍卖产品列表</a></li>
		<li class="active"><a href="${ctx}/star/saleByAuction/form?id=${saleByAuction.id}">拍卖产品<shiro:hasPermission name="user:saleByAuction:edit">${not empty saleByAuction.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="user:saleByAuction:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="saleByAuction" action="${ctx}/star/saleByAuction/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">产品名称：</label>
			<div class="controls">
				<sys:baseGoodsGroup id="groupName" value="${saleByAuction.baseGoodsGroup.name}" labelValue="${saleByAuction.groupId}" title="选择产品" cssClass="required"  />
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">数量：</label>
			<div class="controls">
				<input name="num" type="number" min="1" step="1" htmlEscape="false" maxlength="11" class="input-xlarge required" value="${saleByAuction.num}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">拍卖价格：</label>
			<div class="controls">
				<input name="money" type="number" min="0.01" step="0.01" htmlEscape="false" class="input-xlarge required" value="${saleByAuction.money}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">标准竞拍价：</label>
			<div class="controls">
				<input name="normMoney" type="number" step="0.01" min="0.01" htmlEscape="false" class="input-xlarge required" value="${saleByAuction.normMoney}"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				<input name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${saleByAuction.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${saleByAuction.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<input type="hidden" name="status" value="0">
		<%--<div class="control-group">--%>
			<%--<label class="control-label">状态：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:select path="status" class="input-auto required">--%>
					<%--<form:options items="${fns:getDictList('qy_auction_status')}" itemLabel="label" itemValue="value" htmlEscape="false" cssStyle="width: 50px"/>--%>
				<%--</form:select>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">获拍人：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="succeedUser" htmlEscape="false" maxlength="64" class="input-xlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">拍卖规则：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:input path="detail" htmlEscape="false" class="input-xlarge required"/>--%>
				<%--<span class="help-inline"><font color="red">*</font> </span>--%>
			<%--</div>--%>
		<%--</div>--%>
		<%--<div class="control-group">--%>
			<%--<label class="control-label">remarks：</label>--%>
			<%--<div class="controls">--%>
				<%--<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="form-actions">
			<shiro:hasPermission name="user:saleByAuction:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>